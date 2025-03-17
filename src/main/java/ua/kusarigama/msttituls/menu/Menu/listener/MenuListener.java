package ua.kusarigama.msttituls.menu.Menu.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ua.kusarigama.msttituls.menu.Menu.Menu;
import ua.kusarigama.msttituls.menu.Menu.MenuHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof MenuHolder holder) {
            Menu menu = holder.getMenu();
            menu.handleClick(event, (Player) event.getWhoClicked());
        }
    }
}