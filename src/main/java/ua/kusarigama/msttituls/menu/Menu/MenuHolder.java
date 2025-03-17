package ua.kusarigama.msttituls.menu.Menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MenuHolder implements InventoryHolder {
    private final Menu menu;

    public MenuHolder(Menu menu) {
        this.menu = menu;
    }

    @Override
    public Inventory getInventory() {
        return menu.getInventory();
    }

    public Menu getMenu() {
        return menu;
    }
}
