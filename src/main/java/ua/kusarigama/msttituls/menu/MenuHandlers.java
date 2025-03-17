package ua.kusarigama.msttituls.menu;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ua.kusarigama.msttituls.main;
import ua.kusarigama.msttituls.util.TextUtil;

public class MenuHandlers {
    private final TextUtil text = new TextUtil();
    private final FileConfiguration config = main.getInstance().getConfig();

    public void onTitulClick(Player player, InventoryClickEvent event, String titul) {
        String coloredTitul = text.color(titul);
        switch (event.getClick()) {
            case LEFT:
            case DOUBLE_CLICK:
                player.closeInventory();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), config.getString("settings.command")
                        .replace("%player%", player.getName())
                        .replace("%titul%", titul));
                break;
            case DROP:
                player.sendMessage(text.color(config.getString("messages.give.give")
                        .replace("%titul%", coloredTitul)));
                player.closeInventory();
                Bukkit.getPluginManager().registerEvents(new Listener() {
                    @EventHandler
                    public void onChat(AsyncPlayerChatEvent chatEvent) {
                        if (chatEvent.getPlayer().equals(player)) {
                            chatEvent.setCancelled(true);
                            String targetPlayerName = chatEvent.getMessage();

                            if (chatEvent.getMessage().equalsIgnoreCase("cancel")) {
                                player.sendMessage(text.color(config.getString("messages.give.cancel")));
                                HandlerList.unregisterAll(this);
                                return;
                            }

                            Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                            if (targetPlayer != null) {
                                if (targetPlayerName.equals(player.getName())){
                                    player.sendMessage(text.color(config.getString("messages.myself")));
                                    return;
                                }

                                player.sendMessage(text.color(config.getString("messages.give.gived")
                                        .replace("%titul%", coloredTitul)
                                        .replace("%player%", targetPlayerName)));
                                targetPlayer.sendMessage(text.color(config.getString("messages.give.gived-target")
                                        .replace("%titul%", coloredTitul)
                                        .replace("%player%", player.getName())));
                                targetPlayer.sendActionBar(text.color(config.getString("messages.give.gived-target")
                                        .replace("%titul%", coloredTitul)
                                        .replace("%player%", player.getName())));
                                main.getInstance().getTitul().addTitul(targetPlayerName, titul);
                            } else {
                                player.sendMessage(text.color(config.getString("messages.playernotfind")
                                        .replace("%player%", targetPlayerName)));
                            }
                            HandlerList.unregisterAll(this);
                        }
                    }
                }, main.getInstance());
                break;
        }
    }

    public void onClearItem(Player player, InventoryClickEvent event) {
        switch (event.getClick()) {
            case LEFT:
            case DOUBLE_CLICK:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), config.getString("settings.clear-command")
                        .replace("%player%", player.getName()));
                break;
        }
    }

    public void onBuyItemClick(Player player, InventoryClickEvent event) {
        switch (event.getClick()) {
            case LEFT:
            case DOUBLE_CLICK:
                if (!main.getInstance().getPointsApi().has(player, config.getInt("settings.price"))) {
                    player.sendMessage(text.color(config.getString("messages.buy.no-enough")
                            .replace("%balance%", main.getInstance().getPointsApi().get(player))
                            .replace("%price%", String.valueOf(config.getInt("settings.price")))
                    ));
                    break;
                }

                player.sendMessage(text.color(config.getString("messages.buy.buy")));
                player.closeInventory();

                Listener chatListener = new Listener() {
                    @EventHandler
                    public void onChat(AsyncPlayerChatEvent chatEvent) {
                        chatEvent.setCancelled(true);
                        Bukkit.getScheduler().runTask(main.getInstance(), () -> {
                            player.closeInventory();
                        });
                        String message = chatEvent.getMessage().trim();

                        if (message.equalsIgnoreCase("cancel")) {
                            player.sendMessage(text.color(config.getString("messages.buy.cancel")));
                            HandlerList.unregisterAll(this);
                            return;
                        }

                        String titul = message;

                        player.sendMessage(text.color(config.getString("messages.buy.success"))
                                .replace("%titul%", text.color(titul)));
                        player.sendActionBar(text.color(config.getString("messages.buy.success"))
                                .replace("%titul%", text.color(titul)));
                        main.getInstance().getTitul().addTitul(player.getName(), titul);
                        main.getInstance().getPointsApi().take(player, config.getInt("settings.price"));

                        HandlerList.unregisterAll(this);
                    }
                };


                Bukkit.getPluginManager().registerEvents(chatListener, main.getInstance());
                break;
        }
    }

    public void emptyAction(Player player, InventoryClickEvent event) {
    }
}
