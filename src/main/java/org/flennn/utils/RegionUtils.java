package org.flennn.utils;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldedit.math.BlockVector3;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.sk89q.worldedit.bukkit.BukkitAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegionUtils {

    public static Object[] getPlayerCountInRegion(Player player, String regionName) {
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(player.getWorld()));

        assert regionManager != null;

        ProtectedRegion region = regionManager.getRegion(regionName);

        if (region != null) {
            BlockVector3 min = region.getMinimumPoint();
            BlockVector3 max = region.getMaximumPoint();

            List<String> playerNames = new ArrayList<>();
            String XYZ = String.valueOf("X : " + min.getBlockX() + " , Y : " + min.getBlockY() + " , Z : " + min.getBlockZ());
            int count = 0;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Location playerLocationCheck = onlinePlayer.getLocation();

                if (isLocationWithinRegion(playerLocationCheck, min, max)) {
                    count++;
                    playerNames.add(onlinePlayer.getName());
                }
            }

            return new Object[]{
                    count,
                    XYZ,
                    playerNames.toArray(new String[0])
            };
        } else {
            return new Object[]{
                    0,
                    "noregion",
            };
        }
    }

    public static boolean isLocationWithinRegion(Location location, BlockVector3 min, BlockVector3 max) {
        double playerX = location.getX();
        double playerY = location.getY();
        double playerZ = location.getZ();

        return playerX >= min.getBlockX() && playerX <= max.getBlockX() &&
                playerY >= min.getBlockY() && playerY <= max.getBlockY() &&
                playerZ >= min.getBlockZ() && playerZ <= max.getBlockZ();
    }
    public static String[] getAllRegionNames() {
        List<String> allRegionNames = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));

            if (regionManager != null) {
                Map<String, ?> regions = regionManager.getRegions();

                for (String regionName : regions.keySet()) {
                    allRegionNames.add(regionName);
                }
            }
        }

        return allRegionNames.toArray(new String[0]);
    }
    public static boolean containsRegion(String[] allRegionNames, String targetRegion) {
        for (String regionName : allRegionNames) {
            if (regionName.equals(targetRegion)) {
                return true;
            }
        }
        return false;
    }

}
