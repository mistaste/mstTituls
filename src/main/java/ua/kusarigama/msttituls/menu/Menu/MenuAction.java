package ua.kusarigama.msttituls.menu.Menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface MenuAction {
    void onClick(Player player, InventoryClickEvent event);
}