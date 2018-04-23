package com.pickuperic;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandRemovePlayer {
	
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments: /teams remove <player> <team>");
		} else {
			String playerName = args[1];
			String teamName = args[2].toUpperCase();
			Player player = Bukkit.getServer().getPlayerExact(playerName);
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Invalid user");
			} else if (!Teams.containsTeam(teamName)) {
				sender.sendMessage(ChatColor.RED + "Invalid team name. Please choose from: " + String.join(", ", Teams.getTeamNames()));
			} else {
				Teams.getTeam(teamName).removePlayer(player);
				sender.sendMessage(ChatColor.GREEN + "Removed player from the team.");
			}
		}
		
		return true;
	}

}
