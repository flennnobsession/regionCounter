package org.flennn.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.flennn.RegionCounter;
import org.flennn.database.KillsDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.bukkit.Bukkit.getLogger;

public class KillListener implements Listener {

    private final KillsDatabase killsDatabase;

    public KillListener(RegionCounter plugin) {
        this.killsDatabase = plugin.getKillsDatabase();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if (killer != null) {
            incrementKillCount(killer);
        }
    }

    private void incrementKillCount(Player player) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        killsDatabase.addKill(player.getUniqueId(), timestamp);
    }



}
