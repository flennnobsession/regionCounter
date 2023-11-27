package org.flennn.database;

import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class DiamondPotKillsDatabase {

    private final Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public DiamondPotKillsDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS DiamondPotkills (" +
                    "player_uuid TEXT PRIMARY KEY," +
                    "username TEXT NOT NULL," +
                    "kills INTEGER NOT NULL DEFAULT 0," +
                    "deaths INTEGER NOT NULL DEFAULT 0" +
                    ")");

        }

    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void addPlayer(Player player) throws SQLException{
        try(PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO DiamondPotkills (player_uuid, username) VALUES (?, ?)")){
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.executeUpdate();
        }
    }

    public boolean playerExists(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM DiamondPotkills WHERE player_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void addKill(Player player) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }

        int currentKills = getKills(player);
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE DiamondPotkills SET kills = ? WHERE player_uuid = ?")) {
            preparedStatement.setInt(1, currentKills + 1);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public void addDeath(Player player) throws SQLException {
        if (!playerExists(player)) {
            addPlayer(player);
        }

        int currentDeaths = getDeaths(player);
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE DiamondPotkills SET deaths = ? WHERE player_uuid = ?")) {
            preparedStatement.setInt(1, currentDeaths + 1);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();

        }
    }

    public int getKills(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills FROM DiamondPotkills WHERE player_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("kills");
            }
        }
        return 0;
    }

    public int getDeaths(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT deaths FROM DiamondPotkills WHERE player_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("deaths");
            }
        }
        return 0;
    }
}
