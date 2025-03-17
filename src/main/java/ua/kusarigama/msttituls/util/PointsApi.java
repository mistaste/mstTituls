package ua.kusarigama.msttituls.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;

public class PointsApi {
    public void give(Player player, Integer amount) {
        PlayerPointsAPI api = getAPI();
        if (api != null) {
            api.give(player.getUniqueId(), amount);
        }
    }

    public void take(Player player, Integer amount) {
        PlayerPointsAPI api = getAPI();
        if (api != null) {
            api.take(player.getUniqueId(), amount);
        }
    }

    public boolean has(Player player, Integer amount) {
        PlayerPointsAPI api = getAPI();
        return api != null && api.look(player.getUniqueId()) >= amount;
    }

    public void set(Player player, Integer amount) {
        PlayerPointsAPI api = getAPI();
        if (api != null) {
            api.set(player.getUniqueId(), amount);
        }
    }

    public String get(Player player) {
        PlayerPointsAPI api = getAPI();
        if (api != null) {
            return api.lookFormatted(player.getUniqueId());
        }
        return "error";
    }

    private PlayerPointsAPI getAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlayerPoints") == null) {
            return null;
        }
        return PlayerPoints.getInstance().getAPI();
    }
}
