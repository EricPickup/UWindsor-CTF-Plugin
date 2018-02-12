package com.pickuperic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandGetFlag implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		String team = args[0].toUpperCase();
		
		if (sender instanceof Player) {
			
			if (args.length > 0 && Teams.containsFlag(team)) {
				args[0] = team;
				Player player = (Player) sender;
				player.sendMessage("" + ChatColor.valueOf(team.equals("ORANGE") ? "GOLD" : team) + ChatColor.BOLD + team + ChatColor.RESET + ChatColor.GREEN + 
						" team's flag is located at: " + Teams.getBannerCoordinates(team));
			}
		}
		
		
		return true;
	}

}
