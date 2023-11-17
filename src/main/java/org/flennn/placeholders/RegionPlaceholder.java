package org.flennn.placeholders;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.flennn.RegionCounter;
import org.flennn.utils.RegionUtils;
import org.jetbrains.annotations.NotNull;

import static org.flennn.utils.RegionUtils.containsRegion;

public class RegionPlaceholder extends PlaceholderExpansion {
    private RegionCounter plugin;

    @Override
    public String getIdentifier() {
        return "regioncounter";
    }

    @Override
    public String getAuthor() {
        return "flennn";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    public RegionPlaceholder(RegionCounter plugin){
        this.plugin = plugin;
    }
    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {

        String[] allRegionNames = RegionUtils.getAllRegionNames();

        if (containsRegion(allRegionNames, params)) {
            Object[] regionInfo = RegionUtils.getPlayerCountInRegion((Player) offlinePlayer, params);
            if ((int) regionInfo[0] >= 0 && regionInfo[1] != "noregion") {

                int count = (int) regionInfo[0];
                return String.valueOf(count);
            } else {
                return "Region not found";
            }
        } else {
            return "Region not found";
        }
    }


    private WorldGuardPlugin getWorldGuard() {
        return (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
    }
}
