package org.flennn.Listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.flennn.RegionCounter;
import org.flennn.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.bukkit.Bukkit.getLogger;
import static org.flennn.utils.RegionUtils.getPlayerRegion;

public class KillListener implements Listener {

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

    public KillListener(RegionCounter plugin) {
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

    }



    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();
        assert killer != null;
        RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(killer.getWorld()));

        assert regionManager != null;

        String regionName = String.valueOf(getPlayerRegion(killer.getLocation()));

        getLogger().info("DEBUG: Region name found: " + regionName);

        assert regionName != null;
        if (regionName.equals(smpRegionName)) {
            getLogger().info("DEBUG: Player is in " + regionName + " region");
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            smpKillsDatabase.addKill(killer.getUniqueId(), timestamp);
        } else if (regionName.equals(crystalpvpRegionName)){
            getLogger().info("DEBUG: Player is in " + regionName + " region");
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            crystalpvpDatabase.addKill(killer.getUniqueId(), timestamp);
        } else if (regionName.equals(diamondpotRegionName)){
            getLogger().info("DEBUG: Player is in " + regionName + " region");
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            diamondpotDatabase.addKill(killer.getUniqueId(), timestamp);

        } else if (regionName.equals(tankRegionName)){
            getLogger().info("DEBUG: Player is in " + regionName + " region");
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            tankDatabase.addKill(killer.getUniqueId(), timestamp);

        } else if (regionName.equals(netheritepotRegionName)){
            getLogger().info("DEBUG: Player is in " + regionName + " region");
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            netheritepotDatabase.addKill(killer.getUniqueId(), timestamp);
        } else if (regionName.equals(axeRegionName)){
            getLogger().info("DEBUG: Player is in " + regionName + " region");
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            axeDatabase.addKill(killer.getUniqueId(), timestamp);
        }

    }





}
