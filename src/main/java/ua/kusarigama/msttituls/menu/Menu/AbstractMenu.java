package ua.kusarigama.msttituls.menu.Menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ua.kusarigama.msttituls.util.TextUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMenu implements Menu {
    private final String title;
    private final int size;
    private final Map<Integer, MenuAction> actions;
    private final Inventory inventory;

    public AbstractMenu(String title, int size) {
        this.title = title;
        this.size = size;
        this.actions = new HashMap<>();
        MenuHolder holder = new MenuHolder(this);
        TextUtil text = new TextUtil();
        this.inventory = Bukkit.createInventory(holder, size, text.color(title));
    }


    protected void setItem(int slot, ItemStack item, MenuAction action) {
        inventory.setItem(slot, item);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void handleClick(InventoryClickEvent event, Player player) {
        int slot = event.getSlot();
        if (actions.containsKey(slot)) {
            actions.get(slot).onClick(player, event);
        }
        event.getClick();
        event.setCancelled(true);
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getSize() {
        return size;
    }
}