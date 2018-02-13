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
			} else if (!Teams.containsTeam(team)) {
				sender.sendMessage(ChatColor.RED + "Invalid arguments: /setteam <player> <team>");
				sender.sendMessage(ChatColor.RED + "Invalid team name. Please choose from: " + String.join(", ", Teams.teams.keySet()));
			} else {
				Teams.teams.get(team.toUpperCase()).addPlayer(player);
				sender.sendMessage(ChatColor.GREEN + "Successfully added " + player.getDisplayName() + ChatColor.GREEN + " to team " + Teams.teams.get(team.toUpperCase()).printTeamName() + ChatColor.GREEN + ".");
			}
		}
	
		return true;
	}
	
	

}
