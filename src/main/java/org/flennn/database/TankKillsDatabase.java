package org.flennn.database;

import java.sql.*;
import java.util.UUID;

public class TankKillsDatabase {

    private final Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public TankKillsDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);


        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS tankkills (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player_uuid TEXT NOT NULL," +
                    "kills INTEGER NOT NULL DEFAULT 0," +
                    "deaths INTEGER NOT NULL DEFAULT 0," +
                    "timestamp TEXT NOT NULL)");

            try {
                statement.execute("ALTER TABLE tankkills ADD COLUMN deaths INTEGER DEFAULT 0;");
            } catch (SQLException e) {
                if (!e.getMessage().contains("duplicate column name")) {
                    throw e;
                }
            }
        }

    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addKill(UUID playerUUID, String timestamp) {
        if (killExists(playerUUID)) {
            int currentKills = getKills(playerUUID);
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE tankkills SET kills = ?, timestamp = ? WHERE player_uuid = ?")) {
                preparedStatement.setInt(1, currentKills + 1);
                preparedStatement.setString(2, timestamp);
                preparedStatement.setString(3, playerUUID.toString());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tankkills (player_uuid, kills, deaths, timestamp) VALUES (?, 1, 0, ?)")) {
                preparedStatement.setString(1, playerUUID.toString());
                preparedStatement.setString(2, timestamp);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDeath(UUID playerUUID, String timestamp) {
        if (killExists(playerUUID)) {
            int currentDeaths = getDeaths(playerUUID);
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE tankkills SET deaths = ?, timestamp = ? WHERE player_uuid = ?")) {
                preparedStatement.setInt(1, currentDeaths + 1);
                preparedStatement.setString(2, timestamp);
                preparedStatement.setString(3, playerUUID.toString());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tankkills (player_uuid, kills, deaths, timestamp) VALUES (?, 0, 1, ?)")) {
                preparedStatement.setString(1, playerUUID.toString());
                preparedStatement.setString(2, timestamp);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean killExists(UUID playerUUID) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tankkills WHERE player_uuid = ?")) {
            preparedStatement.setString(1, playerUUID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getKills(UUID playerUUID) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills FROM tankkills WHERE player_uuid = ?")) {
            preparedStatement.setString(1, playerUUID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("kills");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getDeaths(UUID playerUUID) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT deaths FROM tankkills WHERE player_uuid = ?")) {
            preparedStatement.setString(1, playerUUID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("deaths");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
