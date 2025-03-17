package ua.kusarigama.msttituls.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TextUtil {
    public String formApi(String text, Player p) {
        String s = text;
        if (p != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            s = PlaceholderAPI.setPlaceholders(p, text);
        }

        return s;
    }

    public String format(String text, Player p) {
        String s = text;
        if (p != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            s = PlaceholderAPI.setPlaceholders(p, text);
        }

        return color(s);
    }

    public String color(String text) {
        String[] texts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
        StringBuilder finalText = new StringBuilder();
        for (int i = 0; i < texts.length; ++i) {
            if (texts[i].equalsIgnoreCase("&")) {
                if (texts[++i].charAt(0) == '#') {
                    finalText.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                    continue;
                }
                finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                continue;
            }
            finalText.append(texts[i]);
        }
        return finalText.toString();
    }

    public List<Integer> parseSlotRanges(List<String> slotRanges) {
        List<Integer> slots = new ArrayList<>();

        for (String range : slotRanges) {
            if (range.contains("-")) {
                String[] bounds = range.split("-");
                int start = Integer.parseInt(bounds[0]);
                int end = Integer.parseInt(bounds[1]);

                for (int i = start; i <= end; i++) {
                    slots.add(i);
                }
            } else {
                slots.add(Integer.parseInt(range));
            }
        }

        return slots;
    }
}
