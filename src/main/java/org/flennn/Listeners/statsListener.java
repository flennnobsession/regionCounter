package org.flennn.Listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.flennn.RegionCounter;
import org.flennn.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.bukkit.Bukkit.getLogger;
import static org.flennn.utils.RegionUtils.getKillerRegion;
import static org.flennn.utils.RegionUtils.getPlayerRegion;

public class statsListener implements Listener {

    private final SMPKillsDatabase smpKillsDatabase;
    private final CrystalPVPKillsDatabase crystalpvpDatabase;
    private final DiamondPotKillsDatabase diamondpotDatabase;
    private final TankKillsDatabase tankDatabase;
    private final NetheritePotKillsDatabase netheritepotDatabase;
    private final AxeKillsDatabase axeDatabase;

    private final String smpRegionName;
    private final String crystalpvpRegionName;
    private final String diamondpotRegionName;
    private final String tankRegionName;
    private final String netheritepotRegionName;
    private final String axeRegionName;
    private final String uhcRegionName;
    private final UHCKillsDatabase uhcdatabase;
    private String CartPVPRegionName;
    private CartPVPKillsDatabase cartpvpkillsDatabase;


    public statsListener(RegionCounter plugin) {
        this.smpKillsDatabase = plugin.getSMPKillsDatabase();
        this.smpRegionName = plugin.getConfig().getString("smpRegionName");

        this.crystalpvpDatabase = plugin.getKillsCrystalPVPDatabase();
        this.crystalpvpRegionName = plugin.getConfig().getString("crystalpvpRegionName");

        this.diamondpotDatabase = plugin.getDiamondPotKillsDatabase();
        this.diamondpotRegionName = plugin.getConfig().getString("diamodpotRegionName");

        this.tankDatabase = plugin.getTankKillsDatabase();
        this.tankRegionName = plugin.getConfig().getString("tankRegionName");

        this.netheritepotDatabase = plugin.getNetheritePotKillsDatabase();
        this.netheritepotRegionName = plugin.getConfig().getString("netheritepotRegionName");

        this.axeDatabase = plugin.getAxeKillsDatabase();
        this.axeRegionName = plugin.getConfig().getString("axeRegionName");
        this.uhcdatabase = plugin.getUHCKillsDatabase();
        this.uhcRegionName = plugin.getConfig().getString("UHCRegionName");

        this.cartpvpkillsDatabase = plugin.getCartPVPKillsDatabase();
        this.CartPVPRegionName = plugin.getConfig().getString("CartPVPRegionName");


    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if (killer != null) {
            World killerWorld = killer.getWorld();
            RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(killerWorld));
            String regionName = getKillerRegion(killer.getLocation(), regionManager);
            if (regionName != null) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                if (regionName.equals(smpRegionName) || regionName.equals("smpcenter") || regionName.equals("smpforest")) {
                    smpKillsDatabase.addKill(killer.getUniqueId(), timestamp);
                    getLogger().info("Added kill to SMP database.");
                } else if (regionName.equals(crystalpvpRegionName) || regionName.equals("holedown") || regionName.equals("holetop") || regionName.equals("flat")) {
                    crystalpvpDatabase.addKill(killer.getUniqueId(), timestamp);
                    getLogger().info("Added kill to CrystalPVP database.");
                } else if (regionName.equals(diamondpotRegionName) || regionName.equals("diamondpotcenter") || regionName.equals("diamondpotcave") || regionName.equals("diamondpotlake")) {
                    diamondpotDatabase.addKill(killer.getUniqueId(), timestamp);
                    getLogger().info("Added kill to DiamondPot database.");
                } else if (regionName.equals(tankRegionName) || regionName.equals("buildingstank") || regionName.equals("stagetank") || regionName.equals("stairstank")) {
                    tankDatabase.addKill(killer.getUniqueId(), timestamp);
                    getLogger().info("Added kill to Tank database.");
                } else if (regionName.equals(netheritepotRegionName) || regionName.equals("netforest") || regionName.equals("netmiddle")) {
                    netheritepotDatabase.addKill(killer.getUniqueId(), timestamp);
                    getLogger().info("Added kill to NetheritePot database.");
                } else if (regionName.equals(axeRegionName)) {
                    axeDatabase.addKill(killer.getUniqueId(), timestamp);
                    getLogger().info("Added kill to Axe database.");
                } else if (regionName.equals(uhcRegionName)) {
                    uhcdatabase.addKill(killer.getUniqueId(), timestamp);
                    getLogger().info("Added kill to UHC database.");
                } else if (regionName.equals(CartPVPRegionName)) {
                    cartpvpkillsDatabase.addKill(killer.getUniqueId(), timestamp);
                    getLogger().info("Added kill to CartPVP database.");
                }
            } else {
                getLogger().info("Region Name is null.");
            }

        }
        World victimWorld = victim.getWorld();
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(victimWorld));
        String regionName = getKillerRegion(victim.getLocation(), regionManager);

        if (regionName != null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            if (regionName.equals(smpRegionName) || regionName.equals("smpcenter") || regionName.equals("smpforest")) {
                smpKillsDatabase.addDeath(victim.getUniqueId(), timestamp);
                getLogger().info("Added death to SMP database.");
            } else if (regionName.equals(crystalpvpRegionName) || regionName.equals("holedown") || regionName.equals("holetop") || regionName.equals("flat")) {
                crystalpvpDatabase.addDeath(victim.getUniqueId(), timestamp);
                getLogger().info("Added death to CrystalPVP database.");
            } else if (regionName.equals(diamondpotRegionName) || regionName.equals("diamondpotcenter") || regionName.equals("diamondpotcave") || regionName.equals("diamondpotlake")) {
                diamondpotDatabase.addDeath(victim.getUniqueId(), timestamp);
                getLogger().info("Added death to DiamondPot database.");
            } else if (regionName.equals(tankRegionName) || regionName.equals("buildingstank") || regionName.equals("stagetank") || regionName.equals("stairstank")) {
                tankDatabase.addDeath(victim.getUniqueId(), timestamp);
                getLogger().info("Added death to Tank database.");
            } else if (regionName.equals(netheritepotRegionName) || regionName.equals("netforest") || regionName.equals("netmiddle")) {
                netheritepotDatabase.addDeath(victim.getUniqueId(), timestamp);
                getLogger().info("Added death to NetheritePot database.");
            } else if (regionName.equals(axeRegionName)) {
                axeDatabase.addDeath(victim.getUniqueId(), timestamp);
                getLogger().info("Added death to Axe database.");
            } else if (regionName.equals(uhcRegionName)) {
                uhcdatabase.addDeath(victim.getUniqueId(), timestamp);
                getLogger().info("Added death to Axe database.");
            } else if (regionName.equals(CartPVPRegionName)) {
                cartpvpkillsDatabase.addDeath(victim.getUniqueId(), timestamp);
                getLogger().info("Added kill to CartPVP database.");
            }
        } else {
            getLogger().info("Region Name is null.");
        }


    }


}
