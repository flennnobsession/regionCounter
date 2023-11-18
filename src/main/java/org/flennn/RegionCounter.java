package org.flennn;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.flennn.Listeners.KillListener;
import org.flennn.cmds.RegionCommand;
import org.flennn.database.*;
import org.flennn.placeholders.*;

import java.sql.SQLException;

public final class RegionCounter extends JavaPlugin implements Listener {

    private WorldGuardPlugin worldGuard;
    private RegionPlaceholder regionPlaceholder;


    private SMPKillsPlaceholder smpkillsPlaceholder;
    private TankKillsPlaceholder tankkillsPlaceholder;

    private AxeKillsPlaceholder axekillsPlaceholder;

    private DiamondPotKillsPlaceholder diamondpotPlaceholder;

    private CrystalPVPKillsPlaceholder crystaalpvpPlaceholder;
    private NetheritePotKillsPlaceholder netheritepotPlaceholder;



    private SMPKillsDatabase smpkillsDatabase;
    private DiamondPotKillsDatabase diamondpotkillsDatabase;
    private NetheritePotKillsDatabase netheritepotkillsDatabase;
    private CrystalPVPKillsDatabase crystaalpvpkillsDatabase;
    private AxeKillsDatabase axekillsDatabase;
    private TankKillsDatabase tankkillsDatabase;

    @Override
    public void onEnable() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        getServer().getPluginManager().registerEvents(this, this);

        saveDefaultConfig();
        reloadConfig();

        handleRegionName("smpregionname");
        handleRegionName("axeRegionName");
        handleRegionName("netheritepotRegionName");
        handleRegionName("tankRegionName");
        handleRegionName("crystalpvpRegionName");
        handleRegionName("diamodpotRegionName");

        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }

            // Connect to the database
            smpkillsDatabase = new SMPKillsDatabase(getDataFolder().getAbsolutePath() + "/smpkills.db");
            diamondpotkillsDatabase = new DiamondPotKillsDatabase(getDataFolder().getAbsolutePath() + "/diamondpotkills.db");
            crystaalpvpkillsDatabase = new CrystalPVPKillsDatabase(getDataFolder().getAbsolutePath() + "/crystalpvpkills.db");
            axekillsDatabase = new AxeKillsDatabase(getDataFolder().getAbsolutePath() + "/axekills.db");
            tankkillsDatabase = new TankKillsDatabase(getDataFolder().getAbsolutePath() + "/tankkills.db");
            netheritepotkillsDatabase = new NetheritePotKillsDatabase(getDataFolder().getAbsolutePath() + "/netheritepotkills.db");

        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Failed to connect to the database! " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (plugin instanceof WorldGuardPlugin) {
            this.worldGuard = (WorldGuardPlugin) plugin;
            this.smpkillsPlaceholder = new SMPKillsPlaceholder(this);
            this.regionPlaceholder = new RegionPlaceholder(this);
            this.tankkillsPlaceholder = new TankKillsPlaceholder(this);
            this.axekillsPlaceholder = new AxeKillsPlaceholder(this);
            this.diamondpotPlaceholder = new DiamondPotKillsPlaceholder(this);
            this.crystaalpvpPlaceholder = new CrystalPVPKillsPlaceholder(this);
            this.netheritepotPlaceholder = new NetheritePotKillsPlaceholder(this);

            regionPlaceholder.register();

            smpkillsPlaceholder.register();
            tankkillsPlaceholder.register();
            axekillsPlaceholder.register();
            diamondpotPlaceholder.register();
            crystaalpvpPlaceholder.register();
            netheritepotPlaceholder.register();
        } else {
            getLogger().severe("WorldGuard not found! Disabling your plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Register cmds
        getCommand("rc").setExecutor(new RegionCommand(this));
        // Register Events
        getServer().getPluginManager().registerEvents(new KillListener(this), this);
    }

    private void handleRegionName(String regionNameKey) {
        FileConfiguration config = getConfig();
        if (!config.isSet(regionNameKey)) {
            config.set(regionNameKey, null);
            saveConfig();
        }
    }


    public  SMPKillsDatabase getSMPKillsDatabase() {
        return smpkillsDatabase;
    }
    public AxeKillsDatabase getAxeKillsDatabase() {
        return axekillsDatabase;
    }
    public DiamondPotKillsDatabase getDiamondPotKillsDatabase() {
        return diamondpotkillsDatabase;
    }
    public NetheritePotKillsDatabase getNetheritePotKillsDatabase() {
        return netheritepotkillsDatabase;
    }
    public CrystalPVPKillsDatabase getKillsCrystalPVPDatabase() {
        return crystaalpvpkillsDatabase;
    }
    public  TankKillsDatabase getTankKillsDatabase() {
        return tankkillsDatabase;
    }
}
