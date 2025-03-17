package ua.kusarigama.msttituls.database;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisCache {

    private JedisPool jedisPool;
    private boolean cacheEnabled;

    public void initialize(String host, int port, String username, String password, boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            jedisPool = new JedisPool(poolConfig, host, port, 2000, username, password);
        } else if (password != null && !password.isEmpty()) {
            jedisPool = new JedisPool(poolConfig, host, port, 2000, password);
        } else {
            jedisPool = new JedisPool(poolConfig, host, port);
        }

        try (Jedis jedis = jedisPool.getResource()) {
            String pingResponse = jedis.ping();
            if ("PONG".equals(pingResponse)) {
                System.out.println("Redis connection initialized successfully.");
            } else {
                System.err.println("Failed to connect to Redis. Ping response: " + pingResponse);
            }
        } catch (Exception e) {
            System.err.println("Failed to connect to Redis.");
            e.printStackTrace();
        }
    }
    public Jedis getJedis() {
        if (!cacheEnabled) {
            System.out.println("Cache is disabled, returning null.");
            return null;
        }

        try {
            return jedisPool.getResource();
        } catch (NullPointerException e) {
            System.err.println("Failed to connect to Redis, continuing without cache.");
            return null;
        }
    }

    public void close() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    public <K, V> void setMapValue(String mapName, K key, V value) {
        if (!cacheEnabled) {
            System.out.println("Cache is disabled, skipping Redis set.");
            return;
        }

        try (Jedis jedis = getJedis()) {
            if (jedis != null) {
                jedis.hset(mapName, key.toString(), value.toString());
            }
        }
    }

    public <K, V> V getMapValue(String mapName, K key) {
        if (!cacheEnabled) {
            System.out.println("Cache is disabled, skipping Redis get.");
            return null;
        }

        try (Jedis jedis = getJedis()) {
            if (jedis != null) {
                return (V) jedis.hget(mapName, key.toString());
            }
        }
        return null;
    }

    public <K> void removeMapValue(String mapName, K key) {
        if (!cacheEnabled) {
            System.out.println("Cache is disabled, skipping Redis remove.");
            return;
        }

        try (Jedis jedis = getJedis()) {
            if (jedis != null) {
                jedis.hdel(mapName, key.toString());
            }
        }
    }
}
