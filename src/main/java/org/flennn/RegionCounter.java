package org.flennn;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.flennn.Listeners.KillListener;
import org.flennn.cmds.RegionCommand;
import org.flennn.database.KillsDatabase;
import org.flennn.placeholders.KillsPlaceholder;
import org.flennn.placeholders.RegionPlaceholder;

import java.sql.SQLException;

public final class RegionCounter extends JavaPlugin implements Listener {

    private WorldGuardPlugin worldGuard;
    private RegionPlaceholder regionPlaceholder;
    private KillsPlaceholder killsPlaceholder;
    private KillsDatabase killsDatabase;

    @Override
    public void onEnable() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        getServer().getPluginManager().registerEvents(this, this);
        try {

            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            killsDatabase = new KillsDatabase(getDataFolder().getAbsolutePath() + "/practicekills.db");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }


        if (plugin instanceof WorldGuardPlugin) {
            this.worldGuard = (WorldGuardPlugin) plugin;
            this.killsPlaceholder = new KillsPlaceholder(this);

            this.regionPlaceholder = new RegionPlaceholder(this);

            killsPlaceholder.register();
            regionPlaceholder.register();
        } else {
            getLogger().severe("WorldGuard not found! Disabling your plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register cmds
        getCommand("rc").setExecutor(new RegionCommand(this));
        // Register Events
        getServer().getPluginManager().registerEvents(new KillListener(this), this);

    }


    public KillsDatabase getKillsDatabase() {
        return killsDatabase;
    }
}
