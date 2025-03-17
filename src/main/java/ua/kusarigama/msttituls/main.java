package ua.kusarigama.msttituls;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import ua.kusarigama.msttituls.menu.Menu.listener.MenuListener;

import ua.kusarigama.msttituls.database.Database;
import ua.kusarigama.msttituls.database.RedisCache;

import ua.kusarigama.msttituls.util.Placeholder;
import ua.kusarigama.msttituls.util.PointsApi;

@Getter
public final class main extends JavaPlugin implements Listener {
    private static main instance;
    private Database database;
    private RedisCache redisCache;
    private TitulManager titul;
    public PointsApi pointsApi;

    @Override
    public void onEnable() {
        instance = this;

        load();
    }

    @Override
    public void onDisable() {
        if (database != null) {
            database.close();
        }
        if (redisCache != null) {
            redisCache.close();
        }
    }


    private void load(){
        saveDefaultConfig();

        FileConfiguration config = getConfig();

        database = new Database();
        try {
            database.initialize(
                    config.getString("database.host"),
                    config.getInt("database.port"),
                    config.getString("database.database"),
                    config.getString("database.username"),
                    config.getString("database.password")
            );
            System.out.println("Database connected successfully.");
        } catch (Exception e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }

        redisCache = new RedisCache();
        try {
            boolean cacheEnabled = config.getBoolean("redis.cache_enabled");
            redisCache.initialize(
                    config.getString("redis.host"),
                    config.getInt("redis.port"),
                    config.getString("redis.username"),
                    config.getString("redis.password"),
                    cacheEnabled
            );
        } catch (Exception e) {
            System.err.println("Failed to connect to Redis.");
            e.printStackTrace();
        }
        titul = new TitulManager();
        pointsApi = new PointsApi();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholder().register();
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            Bukkit.getPluginManager().disablePlugin(this);
        }

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getCommand("msttitul").setExecutor(new CommandHandler());
    }

    public static main getInstance(){return instance;}
}
