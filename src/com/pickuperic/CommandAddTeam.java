package com.pickuperic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class CommandAddTeam implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Invalid arguments: /addteam <teamName> <teamColor>");
		} else {
			try {
				ChatColor.valueOf(args[1].toUpperCase());
				String teamColor = args[1].toUpperCase();
				String teamName = args[0];
				if (Teams.addTeam(teamName, teamColor) == true) {
					sender.sendMessage(ChatColor.GREEN + "Successfully added team " + ChatColor.valueOf(teamColor) + ChatColor.BOLD + teamName + 
					ChatColor.RESET + ChatColor.GREEN + " to list of teams.");
				} else {
					sender.sendMessage(ChatColor.RED + "Error: Team name already in use.");
				}
				
			} catch (IllegalArgumentException e) {
				sender.sendMessage(ChatColor.RED + "Invalid color. Please choose from: " + String.join(", ", Teams.availableColors));
			}
			
			return true; 
		}
		return true;
		
	}

}
