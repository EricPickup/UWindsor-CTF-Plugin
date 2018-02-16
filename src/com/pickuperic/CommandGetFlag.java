package com.pickuperic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandGetFlag {

	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player) {
			if (args.length <= 1) {
				sender.sendMessage(ChatColor.RED + "Invalid arguments: /teams getflag <team>");
			} else if (!Teams.teams.get(args[1].toUpperCase()).hasBanner()) {
				sender.sendMessage(ChatColor.RED + "Team does not have a banner placed.");
			} else {
				String team = args[1].toUpperCase();
				args[1] = team;
				Player player = (Player) sender;
				player.sendMessage(Teams.teams.get(team).printTeamName() + ChatColor.GREEN + 
						" team's flag is located at: " + Teams.teams.get(team).getBannerCoordinates());
			}
		}
		return true;
	}

}
