package ua.kusarigama.msttituls.menu;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ua.kusarigama.msttituls.main;
import ua.kusarigama.msttituls.menu.Menu.AbstractMenu;
import ua.kusarigama.msttituls.util.TextUtil;

import java.util.List;

public class TitulMenu extends AbstractMenu {
    private final TextUtil text = new TextUtil();
    private final FileConfiguration config = main.getInstance().getConfig();

    public TitulMenu(Player player) {
        super(new TextUtil().color(main.getInstance().getConfig().getString("settings.menu-title")), main.getInstance().getConfig().getInt("settings.menu-size"));
        initializeMenu(player);
    }

    private void initializeMenu(Player player) {
        if (main.getInstance().getTitul().hasTituls(player.getName())) {
            List<String> slotRanges = main.getInstance().getConfig().getStringList("items.titulItem.slot");
            List<Integer> slots = text.parseSlotRanges(slotRanges);
            List<String> suffixes = main.getInstance().getTitul().getTituls(player.getName());

            for (int i = 0; i < slots.size() && i < suffixes.size(); i++) {
                int slot = slots.get(i);
                String titul = suffixes.get(i);
                titulitem(titul, slot);
            }
        } else {
            titulNotExist();
        }
        fillItem();
        infoItem();
        buyItem();
        clearItem();
    }

    private void titulitem(String titul, int slot) {
        ItemStack item = new ItemStack(Material.matchMaterial(config.getString("items.titulItem.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(text.color(config.getString("items.titulItem.name").replace("%titul%", titul)));
        List<String> lore = config.getStringList("items.titulItem.lore").stream()
                .map(line -> text.color(line.replace("%titul%", titul)))
                .toList();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM);
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        meta.setLore(lore);
        item.setItemMeta(meta);

        setItem(slot, item, (player, event) -> new MenuHandlers().onTitulClick(player, event, titul));
    }


    private void buyItem(){
        ItemStack item = new ItemStack(Material.matchMaterial(config.getString("items.buyItem.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(text.color(config.getString("items.buyItem.name")));
        List<String> lore = config.getStringList("items.buyItem.lore").stream()
                .map(line -> text.color(line.replace("%price%", String.valueOf(config.getInt("settings.price")))))
                .toList();
        meta.setLore(lore);
        item.setItemMeta(meta);

        for (int slot : config.getIntegerList("items.buyItem.slot")) {
            setItem(slot, item, (player, event) -> new MenuHandlers().onBuyItemClick(player, event));
        }
    }

    private void clearItem(){
        ItemStack item = new ItemStack(Material.matchMaterial(config.getString("items.clearItem.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(text.color(config.getString("items.clearItem.name")));
        List<String> lore = config.getStringList("items.clearItem.lore").stream()
                .map(text::color)
                .toList();
        meta.setLore(lore);
        item.setItemMeta(meta);

        for (int slot : config.getIntegerList("items.clearItem.slot")) {
            setItem(slot, item, (player, event) -> new MenuHandlers().onClearItem(player, event));
        }
    }

    private void titulNotExist(){
        ItemStack item = new ItemStack(Material.matchMaterial(config.getString("items.notExist.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(text.color(config.getString("items.notExist.name")));
        List<String> lore = config.getStringList("items.notExist.lore").stream()
                .map(text::color)
                .toList();
        meta.setLore(lore);
        item.setItemMeta(meta);

        for (int slot : config.getIntegerList("items.notExist.slot")) {
            setItem(slot, item, (player, event) -> new MenuHandlers().onBuyItemClick(player, event));
        }
    }

    private void fillItem(){
        ItemStack item = new ItemStack(Material.matchMaterial(config.getString("items.fillItem.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(text.color(config.getString("items.fillItem.name")));
        List<String> lore = config.getStringList("items.fillItem.lore").stream()
                .map(text::color)
                .toList();
        meta.setLore(lore);
        item.setItemMeta(meta);


        for (int slot : text.parseSlotRanges(config.getStringList("items.fillItem.slot"))) {
            setItem(slot, item, (player, event) -> new MenuHandlers().emptyAction(player, event));
        }
    }

    private void infoItem(){
        ItemStack item = new ItemStack(Material.matchMaterial(config.getString("items.infoItem.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(text.color(config.getString("items.infoItem.name")));
        List<String> lore = config.getStringList("items.infoItem.lore").stream()
                .map(text::color)
                .toList();
        meta.setLore(lore);
        item.setItemMeta(meta);


        for (int slot : text.parseSlotRanges(config.getStringList("items.infoItem.slot"))) {
            setItem(slot, item, (player, event) -> new MenuHandlers().emptyAction(player, event));
        }
    }
}