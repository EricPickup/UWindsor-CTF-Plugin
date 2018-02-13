package com.pickuperic;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandGetTeam implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player;
		
		if (args.length == 0) {
			player = (Player) sender;
		} else {
			player = Bukkit.getServer().getPlayerExact(args[0]);
		}

		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Invalid user!");
		} else if (Teams.getPlayerTeam(player) == null) {
			sender.sendMessage(ChatColor.GREEN + "User " + Teams.getPlayerColor(player) + player.getDisplayName() + ChatColor.GREEN + " is not part of a team.");
		} else {
			sender.sendMessage(ChatColor.GREEN + "Player " + Teams.getPlayerColor(player) + player.getDisplayName() + ChatColor.GREEN + " is part of team " + Teams.teams.get(Teams.getPlayerTeam(player).toUpperCase()).printTeamName() + ChatColor.GREEN + ".");
		}
	
		 return true;
	}
	
}
