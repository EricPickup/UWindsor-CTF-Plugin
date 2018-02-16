package com.pickuperic;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandSetTeam {
	
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments: /teams add <player> <team>");
			
		} else {
			
			String playerName = args[1];
			String team = args[2].toUpperCase();
			Player player = Bukkit.getServer().getPlayerExact(playerName);
			
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Invalid user.");
			} else if (!Teams.containsTeam(team)) {
				sender.sendMessage(ChatColor.RED + "Invalid team name. Please choose from: " + String.join(", ", Teams.teams.keySet()));
			} else {
				Teams.teams.get(team.toUpperCase()).addPlayer(player);
				sender.sendMessage(ChatColor.GREEN + "Successfully added " + player.getDisplayName() + ChatColor.GREEN + " to team " + Teams.teams.get(team.toUpperCase()).printTeamName() + ChatColor.GREEN + ".");
			}
		}
	
		return true;
	}
	
	

}
