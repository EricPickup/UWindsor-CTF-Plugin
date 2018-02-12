package com.pickuperic;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandSetTeam implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments: /setteam <player> <team>");
			
		} else {
			
			String playerName = args[0];
			String team = args[1].toUpperCase();
			Player player = Bukkit.getServer().getPlayerExact(playerName);
			
			if (player == null) {
				sender.sendMessage(ChatColor.RED + "Invalid user.");
			} else if (!CommandPlaceFlag.bannerColors.contains(team)) {
				sender.sendMessage(ChatColor.RED + "Invalid arguments: /setteam <player> <team>");
				sender.sendMessage(ChatColor.RED + "Invalid team name. Please choose from: " + String.join(", ", CommandPlaceFlag.bannerColors));
			} else {
				Teams.addPlayer(player, team);
				sender.sendMessage(ChatColor.GREEN + "Successfully added " + player.getDisplayName() + " to team " + ChatColor.valueOf(team.equals("ORANGE") ? "GOLD" : team) + ChatColor.BOLD + team + 
						ChatColor.RESET + ChatColor.GREEN + ".");
				player.sendMessage(ChatColor.GREEN + "You've been added to team " + ChatColor.valueOf(team.equals("ORANGE") ? "GOLD" : team) + ChatColor.BOLD + team + 
						ChatColor.RESET + ChatColor.GREEN + ".");
			}
		}
	
		return true;
	}
	
	

}
