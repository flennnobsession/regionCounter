package org.flennn.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.flennn.RegionCounter;
import org.flennn.utils.RegionUtils;

public class RegionCommand implements CommandExecutor {

    private final RegionCounter plugin;

    public RegionCommand(RegionCounter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        if (args.length == 1) {
            String regionName = args[0];
            Player player = (Player) sender;
            Object[] regionInfo = RegionUtils.getPlayerCountInRegion(player, regionName);

            if ((int) regionInfo[0] >= 0 && regionInfo[1] != "noregion") {
                sender.sendMessage("§6-----------------------\n§lName: §e" + regionName + "§6\n§lXYZ: §e" + regionInfo[1] + "§6\n§lPlayers: §e" + regionInfo[0] + "\n§r§6-----------------------");
            } else {
                sender.sendMessage("§4Unknown region name");
            }
        } else {
            sender.sendMessage("Usage: /rc <regionName>");
        }

        return true;
    }
}
