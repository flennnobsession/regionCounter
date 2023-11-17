package org.flennn.database;

import java.sql.*;
import java.util.UUID;

public class KillsDatabase {

    private final Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public KillsDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS kills (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player_uuid TEXT NOT NULL," +
                    "kills INTEGER NOT NULL DEFAULT 0," +
                    "timestamp TEXT NOT NULL)");
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
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE kills SET kills = ?, timestamp = ? WHERE player_uuid = ?")) {
                preparedStatement.setInt(1, currentKills + 1);
                preparedStatement.setString(2, timestamp);
                preparedStatement.setString(3, playerUUID.toString());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO kills (player_uuid, kills, timestamp) VALUES (?, 1, ?)")) {
                preparedStatement.setString(1, playerUUID.toString());
                preparedStatement.setString(2, timestamp);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean killExists(UUID playerUUID) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM kills WHERE player_uuid = ?")) {
            preparedStatement.setString(1, playerUUID.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getKills(UUID playerUUID) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills FROM kills WHERE player_uuid = ?")) {
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


}
