package me.adarsh.ftp.db;

import me.adarsh.ftp.Economy;

import java.sql.*;

public class DatabaseManager {
    private Connection connection;
    private Economy plugin;

    public DatabaseManager(Economy plugin) {
        this.plugin = plugin;
    }

    public void connectDatabase() {
        if (!Economy.getInstance().getConfig().getBoolean("use-mysql")){
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/data.db");

                try (Statement statement = connection.createStatement()) {
                    String createTableSQL = "CREATE TABLE IF NOT EXISTS balances (username TEXT PRIMARY KEY, balance DOUBLE)";
                    statement.executeUpdate(createTableSQL);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            String database = Economy.getInstance().getConfig().getString("mysql.database");
            String host = Economy.getInstance().getConfig().getString("mysql.host");
            String port = Economy.getInstance().getConfig().getString("mysql.port");
            String user = Economy.getInstance().getConfig().getString("mysql.username");
            String password = Economy.getInstance().getConfig().getString("mysql.password");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&zeroDateTimeBehavior=convertToNull";
            try {
                connection = DriverManager.getConnection(url, user, password);

                try (Statement statement = connection.createStatement()) {
                    String createTableSQL = "CREATE TABLE IF NOT EXISTS balances (username TEXT PRIMARY KEY, balance DOUBLE)";
                    statement.executeUpdate(createTableSQL);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public double getBalance(String username) {
        double balance = 0;
        try (PreparedStatement statement = connection.prepareStatement("SELECT balance FROM balances WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public void setBalance(String username, double amount) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO balances (username, balance) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setDouble(2, amount);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBalance(String username, double amount) {
        try {
            double currentBalance = getBalance(username);
            double newBalance = currentBalance + amount;

            try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO balances (username, balance) VALUES (?, ?)")) {
                statement.setString(1, username);
                statement.setDouble(2, newBalance);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
