package org.flennn.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.flennn.RegionCounter;
import org.jetbrains.annotations.NotNull;

public class CartPVPKillsPlaceholder extends PlaceholderExpansion {

    private final RegionCounter plugin;

    public CartPVPKillsPlaceholder(RegionCounter plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "cartpvp";
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
            return String.valueOf(plugin.getCartPVPKillsDatabase().getKills(offlinePlayer.getUniqueId()));
        } else if(params.equals("deaths")){
            return String.valueOf(plugin.getCartPVPKillsDatabase().getDeaths(offlinePlayer.getUniqueId()));
        } else {
            return "Invalid parameter";
        }
    }
}
