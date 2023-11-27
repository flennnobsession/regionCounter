package org.flennn.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.flennn.RegionCounter;
import org.flennn.cmds.RegionCommand;

import java.sql.SQLException;

public class playerJoinListener implements Listener {

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) throws SQLException {
        Player player = e.getPlayer();

        if(!player.hasPlayedBefore()){
            RegionCounter.getAxeKillsDatabase().addPlayer(player);
            RegionCounter.getCartPVPKillsDatabase().addPlayer(player);
            RegionCounter.getCrystalPVPDatabase().addPlayer(player);
            RegionCounter.getDiamondPotKillsDatabase().addPlayer(player);
            RegionCounter.getNetheritePotKillsDatabase().addPlayer(player);
            RegionCounter.getSMPKillsDatabase().addPlayer(player);
            RegionCounter.getTankKillsDatabase().addPlayer(player);
            RegionCounter.getUHCKillsDatabase().addPlayer(player);
        }


    }
}
