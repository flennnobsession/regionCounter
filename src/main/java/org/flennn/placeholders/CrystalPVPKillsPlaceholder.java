package org.flennn.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.flennn.RegionCounter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class CrystalPVPKillsPlaceholder extends PlaceholderExpansion {

    private final RegionCounter plugin;

    public CrystalPVPKillsPlaceholder(RegionCounter plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "crystalpvp";
    }

    @Override
    public String getAuthor() {
        return "flennn";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (params.equals("kills")) {
            try {
                return String.valueOf(plugin.getCrystalPVPDatabase().getKills((Player) offlinePlayer));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (params.equals("deaths")) {
            try {
                return String.valueOf(plugin.getCrystalPVPDatabase().getDeaths((Player) offlinePlayer));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "Invalid parameter";
        }
    }
}
