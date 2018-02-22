package com.pickuperic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandDeleteTeam {
	
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//If sender is console and did not enter a team name
		if (!(sender instanceof Player) && args.length == 1) {
			sender.sendMessage(ChatColor.RED + "Console must enter a team name!");
		}
		
		Player player = (Player) sender;
		
		//If player did not enter a team name and does not belong to a team
		if (args.length == 1 && Teams.getPlayerTeam(player) == null) {
			
			player.sendMessage(ChatColor.RED + "You are not a member of a team. Please use /teams delete <teamName>");
			
		} else if (args.length == 1) {	//If player did not enter a team name but DOES belong to a team (delete that team)	
			
			Team deleteTeam = Teams.getPlayerTeam(player);
			Teams.availableColors.add(deleteTeam.getColorString());
			System.out.println("Added " + deleteTeam.getColorString());
			deleteTeam.purge();
			Teams.removeTeam(deleteTeam);
			player.sendMessage(ChatColor.GREEN + "Removed team " + deleteTeam.printTeamName());
			
		} else if (!Teams.containsTeam(args[1])) {	//If player DID enter a team but it does not exist
			
			sender.sendMessage(ChatColor.RED + "Invalid team name. Please choose from: " + String.join(", ", Teams.getTeamNames()));
			
		} else {	//If player enters a team that does exist
			
			Team deleteTeam = Teams.getTeam(args[1]);
			Teams.availableColors.add(deleteTeam.getColorString());
			deleteTeam.purge();
			player.sendMessage(ChatColor.GREEN + "Removed team " + deleteTeam.printTeamName());
			Teams.removeTeam(deleteTeam);
		}
		
		
		return true;
		
	}

}
