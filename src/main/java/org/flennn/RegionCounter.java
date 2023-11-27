package org.flennn;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.flennn.Listeners.statsListener;
import org.flennn.cmds.RegionCommand;
import org.flennn.database.*;
import org.flennn.placeholders.*;

import java.io.File;
import java.io.IOException;
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
    private UHCKillsPlaceholder uhckillsPlaceholder;
    private CartPVPKillsPlaceholder cartpvpkillsPlaceholder;



    private static SMPKillsDatabase smpkillsDatabase;

    public static SMPKillsDatabase getSmpkillsDatabase() {
        return smpkillsDatabase;
    }


    private static DiamondPotKillsDatabase diamondpotkillsDatabase;
    private static NetheritePotKillsDatabase netheritepotkillsDatabase;
    private static CrystalPVPKillsDatabase crystaalpvpkillsDatabase;
    private static AxeKillsDatabase axekillsDatabase;
    private static TankKillsDatabase tankkillsDatabase;
    private static UHCKillsDatabase uhckillsDatabase;
    private static CartPVPKillsDatabase cartpvpkillsDatabase;


    @Override
    public void onEnable() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        getServer().getPluginManager().registerEvents(this, this);

        saveDefaultConfig();
        reloadConfig();
        handleRegionName("smpRegionName");
        handleRegionName("axeRegionName");
        handleRegionName("netheritepotRegionName");
        handleRegionName("tankRegionName");
        handleRegionName("crystalpvpRegionName");
        handleRegionName("diamodpotRegionName");
        handleRegionName("UHCRegionName");
        handleRegionName("CartPVPRegionName");

        this.smpkillsPlaceholder = new SMPKillsPlaceholder(this);
        this.tankkillsPlaceholder = new TankKillsPlaceholder(this);
        this.axekillsPlaceholder = new AxeKillsPlaceholder(this);
        this.diamondpotPlaceholder = new DiamondPotKillsPlaceholder(this);
        this.crystaalpvpPlaceholder = new CrystalPVPKillsPlaceholder(this);
        this.netheritepotPlaceholder = new NetheritePotKillsPlaceholder(this);
        this.regionPlaceholder = new RegionPlaceholder(this);
        this.uhckillsPlaceholder = new UHCKillsPlaceholder(this);
        this.cartpvpkillsPlaceholder = new CartPVPKillsPlaceholder(this);


        try {
            File dataFolder = getDataFolder();
            File databasesFolder = new File(dataFolder, "databases");

            if (!databasesFolder.exists() && !databasesFolder.mkdirs()) {
                throw new IOException("Failed to create the databases folder");
            }

            smpkillsDatabase = new SMPKillsDatabase(new File(databasesFolder, "smpkills.db").getAbsolutePath());
            diamondpotkillsDatabase = new DiamondPotKillsDatabase(new File(databasesFolder, "diamondpotkills.db").getAbsolutePath());
            crystaalpvpkillsDatabase = new CrystalPVPKillsDatabase(new File(databasesFolder, "crystalpvpkills.db").getAbsolutePath());
            axekillsDatabase = new AxeKillsDatabase(new File(databasesFolder, "axekills.db").getAbsolutePath());
            tankkillsDatabase = new TankKillsDatabase(new File(databasesFolder, "tankkills.db").getAbsolutePath());
            netheritepotkillsDatabase = new NetheritePotKillsDatabase(new File(databasesFolder, "netheritepotkills.db").getAbsolutePath());
            uhckillsDatabase = new UHCKillsDatabase(new File(databasesFolder, "uhckills.db").getAbsolutePath());
            cartpvpkillsDatabase = new CartPVPKillsDatabase(new File(databasesFolder, "cartpvpkills.db").getAbsolutePath());

        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("Failed to connect to the database! " + ex.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (plugin instanceof WorldGuardPlugin) {
            this.worldGuard = (WorldGuardPlugin) plugin;
            regionPlaceholder.register();

        } else {
            getLogger().severe("WorldGuard not found! Disabling your plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }



        smpkillsPlaceholder.register();
        tankkillsPlaceholder.register();
        axekillsPlaceholder.register();
        diamondpotPlaceholder.register();
        crystaalpvpPlaceholder.register();
        netheritepotPlaceholder.register();
        uhckillsPlaceholder.register();
        cartpvpkillsPlaceholder.register();

        // Register cmds
        getCommand("rc").setExecutor(new RegionCommand(this));
        // Register Events
        getServer().getPluginManager().registerEvents(new statsListener(this), this);
    }

    private void handleRegionName(String regionNameKey) {
        FileConfiguration config = getConfig();
        if (!config.isSet(regionNameKey)) {
            config.set(regionNameKey, null);
            saveConfig();
        }
    }

    @Override
    public void onDisable() {
        regionPlaceholder.unregister();
        smpkillsPlaceholder.unregister();
        tankkillsPlaceholder.unregister();
        axekillsPlaceholder.unregister();
        diamondpotPlaceholder.unregister();
        crystaalpvpPlaceholder.unregister();
        netheritepotPlaceholder.unregister();
        uhckillsPlaceholder.unregister();
        cartpvpkillsPlaceholder.unregister();

        getLogger().info("RegionCounter is now disabled.");
    }

    public static SMPKillsDatabase getSMPKillsDatabase() {
        return smpkillsDatabase;
    }
    public static AxeKillsDatabase getAxeKillsDatabase() {
        return axekillsDatabase;
    }
    public static DiamondPotKillsDatabase getDiamondPotKillsDatabase() {
        return diamondpotkillsDatabase;
    }
    public static NetheritePotKillsDatabase getNetheritePotKillsDatabase() {
        return netheritepotkillsDatabase;
    }
    public static CrystalPVPKillsDatabase getCrystalPVPDatabase() {
        return crystaalpvpkillsDatabase;
    }
    public  static TankKillsDatabase getTankKillsDatabase() {
        return tankkillsDatabase;
    }
    public  static UHCKillsDatabase getUHCKillsDatabase() {
        return uhckillsDatabase;
    }
    public  static CartPVPKillsDatabase getCartPVPKillsDatabase() {
        return cartpvpkillsDatabase;
    }


}
