package ua.kusarigama.msttituls.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private HikariDataSource dataSource;

    public void initialize(String host, int port, String database, String username, String password) {
        System.out.println("Initializing database connection...");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        dataSource = new HikariDataSource(config);

        createTable();

        System.out.println("Database connection initialized successfully.");
    }


    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            System.out.println("DataSource is null when trying to get a connection.");
            throw new SQLException("DataSource not initialized");
        }
        return dataSource.getConnection();
    }


    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    private void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS titles (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "player VARCHAR(36) NOT NULL," +
                "title VARCHAR(255) NOT NULL" +
                ");";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
