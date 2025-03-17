package ua.kusarigama.msttituls;

import ua.kusarigama.msttituls.database.Database;
import ua.kusarigama.msttituls.database.RedisCache;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TitulManager {
    private final Database database = main.getInstance().getDatabase();
    private final RedisCache redisManager = main.getInstance().getRedisCache();

    public boolean hasTituls(String player) {
        String cachedTituls = redisManager.getMapValue("tituls", player);
        if (cachedTituls != null) {
            return !cachedTituls.isEmpty();
        }

        return checkTitulsInDatabase(player);
    }

    private boolean checkTitulsInDatabase(String player) {
        String query = "SELECT COUNT(*) AS count FROM titles WHERE player = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addTitul(String player, String titul) {
        String query = "INSERT INTO titles (player, title) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE title = title";  // Обновляем при дубликате

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player);
            statement.setString(2, titul);
            statement.executeUpdate();

            updateCacheAfterAdd(player, titul);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCacheAfterAdd(String player, String titul) {
        try (Jedis jedis = redisManager.getJedis()) {
            String cachedTituls = jedis.hget("tituls", player);

            List<String> titulsList;
            if (cachedTituls != null && !cachedTituls.isEmpty()) {
                titulsList = new ArrayList<>(List.of(cachedTituls.split(",")));
                if (!titulsList.contains(titul)) {
                    titulsList.add(titul);
                }
            } else {
                titulsList = new ArrayList<>();
                titulsList.add(titul);
            }

            jedis.hset("tituls", player, String.join(",", titulsList));
        }
    }

    public List<String> getTituls(String player) {
        String cachedTituls = redisManager.getMapValue("tituls", player);
        if (cachedTituls != null && !cachedTituls.isEmpty()) {
            return new ArrayList<>(List.of(cachedTituls.split(",")));
        }

        return fetchTitulsFromDatabase(player);
    }

    private List<String> fetchTitulsFromDatabase(String player) {
        String query = "SELECT title FROM titles WHERE player = ?";
        List<String> tituls = new ArrayList<>();
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                tituls.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        redisManager.setMapValue("tituls", player, String.join(",", tituls));
        return tituls;
    }

    public int getTitulCount(String player) {
        String cachedCount = redisManager.getMapValue("titul_count", player);
        if (cachedCount != null) {
            return Integer.parseInt(cachedCount);
        }

        return fetchTitulCountFromDatabase(player);
    }

    private int fetchTitulCountFromDatabase(String player) {
        String query = "SELECT COUNT(*) AS count FROM titles WHERE player = ?";
        int titulCount = 0;
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                titulCount = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        redisManager.setMapValue("titul_count", player, String.valueOf(titulCount));
        return titulCount;
    }

    public void removeTitul(String player, String titul) {
        String query = "DELETE FROM titles WHERE player = ? AND title = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player);
            statement.setString(2, titul);
            statement.executeUpdate();

            updateCacheAfterRemove(player, titul);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCacheAfterRemove(String player, String titul) {
        try (Jedis jedis = redisManager.getJedis()) {
            String cachedTituls = jedis.hget("tituls", player);
            if (cachedTituls != null && !cachedTituls.isEmpty()) {
                List<String> titulsList = new ArrayList<>(List.of(cachedTituls.split(",")));
                if (titulsList.remove(titul)) {
                    if (titulsList.isEmpty()) {
                        jedis.hdel("tituls", player);
                    } else {
                        jedis.hset("tituls", player, String.join(",", titulsList));
                    }
                }
            }
        }
        redisManager.removeMapValue("titul_count", player);
    }
}