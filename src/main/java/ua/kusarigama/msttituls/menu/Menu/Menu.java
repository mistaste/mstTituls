package ua.kusarigama.msttituls.menu.Menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface Menu {
    Inventory getInventory();
    void handleClick(InventoryClickEvent event, Player player);
    void open(Player player);
    String getTitle();
    int getSize();
}