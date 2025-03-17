package ua.kusarigama.msttituls.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ua.kusarigama.msttituls.main;

public class Placeholder extends PlaceholderExpansion {
    static final TextUtil text = new TextUtil();
    public boolean canRegister() {
        return true;
    }

    public @NotNull String getIdentifier() {
        return "msttituls";
    }
    public @NotNull String getAuthor() {
        return main.getInstance().getDescription().getAuthors().toString();
    }
    public @NotNull String getVersion() {
        return main.getInstance().getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equalsIgnoreCase("count")) {
            return String.valueOf(main.getInstance().getTitul().getTitulCount(player.getName()));
        }
        return identifier;
    }
}