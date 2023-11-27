package org.flennn.database;

import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class NetheritePotKillsDatabase {

    private final Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public NetheritePotKillsDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS Netheritepotkills (" +
                    "player_uuid TEXT PRIMARY KEY," +
                    "username TEXT NOT NULL," +
                    "kills INTEGER NOT NULL DEFAULT 0," +
                    "deaths INTEGER NOT NULL DEFAULT 0," +
                    "timestamp TEXT NOT NULL)");

        }

    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void addPlayer(Player player) throws SQLException{
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Netheritepotkills (player_uuid, username) VALUES (?, ?)")){
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.executeUpdate();
        }
    }

    public boolean playerExists(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Netheritepotkills WHERE player_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void addKill(Player player, String timestamp) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }

        int currentKills = getKills(player);
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Netheritepotkills SET kills = ?, timestamp = ? WHERE player_uuid = ?")) {
            preparedStatement.setInt(1, currentKills + 1);
            preparedStatement.setString(2, timestamp);
            preparedStatement.setString(3, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public void addDeath(Player player, String timestamp) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }

        int currentDeaths = getDeaths(player);
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Netheritepotkills SET deaths = ?, timestamp = ? WHERE player_uuid = ?")) {
            preparedStatement.setInt(1, currentDeaths + 1);
            preparedStatement.setString(2, timestamp);
            preparedStatement.setString(3, player.getUniqueId().toString());
            preparedStatement.executeUpdate();

        }
    }

    public int getKills(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills FROM Netheritepotkills WHERE player_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("kills");
            }
        }
        return 0;
    }

    public int getDeaths(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT deaths FROM Netheritepotkills WHERE player_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("deaths");
            }
        }
        return 0;
    }
}
