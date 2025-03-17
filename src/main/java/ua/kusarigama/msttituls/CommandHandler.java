package ua.kusarigama.msttituls;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ua.kusarigama.msttituls.menu.TitulMenu;
import ua.kusarigama.msttituls.util.TextUtil;

public class CommandHandler implements CommandExecutor {
    private final main plugin = main.getInstance();
    private final String prefix = main.getInstance().getConfig().getString("messages.prefix");
    private final FileConfiguration config = main.getInstance().getConfig();
    static final TextUtil text = new TextUtil();

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (s.hasPermission("mstituls.admin")) {
                plugin.reloadConfig();
                s.sendMessage(text.color(prefix + config.getString("messages.reload")));
            } else {
                s.sendMessage(text.color(prefix + config.getString("messages.noperm")));
            }
            return true;
        }

        if (s instanceof Player player) {
            new TitulMenu(player).open(player);
        }

        return false;
    }
}