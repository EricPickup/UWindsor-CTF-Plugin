package com.pickuperic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class CommandHubTeams implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length == 0) {
			//HELP PAGE
			sender.sendMessage(helpMessage());
		} else {
			if (args[0].equalsIgnoreCase("create")) {	//	/teams create <teamName> <teamColor>
				CommandAddTeam.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("list")) {	//	/teams list
				//list teams
				CommandGetTeams.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("add")) {	//	/teams add <player> <toTeam>
				//add player to team
				CommandSetTeam.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("remove")) {	//	/teams remove <player> <fromTeam>
				//remove player from team
				CommandRemovePlayer.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("get")) {	//	/teams get <player>
				//get user's team
				CommandGetTeam.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("info")) {	//	/teams info <team>
				//list team's users
			} else if (args[0].equalsIgnoreCase("getflag")) {	//	/teams getflag <team>
				//get flag location
				CommandGetFlag.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("setflag")) {	//	/teams setflag <team>
				//sets flag location for team
				CommandSetFlag.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(helpMessage());
			} else if (args[0].equalsIgnoreCase("delete")) {
				CommandDeleteTeam.onCommand(sender, command, label, args);
			}
		}
		
		return true;
		
	}
	
	
	public String helpMessage() {
		
		String message = ChatColor.GREEN + "=============WindsorCTF===============";
		message += "\nCommands:";
		message += "\n" + ChatColor.AQUA + "/teams help" + ChatColor.GREEN + " - list help menu for plugin";
		message += "\n" + ChatColor.AQUA + "/teams create <teamName> <teamColor>" + ChatColor.GREEN + " - create new a team with the specified name/color";
		message += "\n" + ChatColor.AQUA + "/teams delete <teamName>" + ChatColor.GREEN + " - delete a team completely (removes members as well)";
		message += "\n" + ChatColor.AQUA + "/teams list" + ChatColor.GREEN + " - lists all of the current teams";
		message += "\n" + ChatColor.AQUA + "/teams add/remove <player> <team>" + ChatColor.GREEN + " - add/remove user to/from specified team";
		message += "\n" + ChatColor.AQUA + "/teams setFlag <teamName>" + ChatColor.GREEN + " - set the location of the specified team's flag";
		message += "\n" + ChatColor.AQUA + "/teams getFlag <teamName>" + ChatColor.GREEN + " - get the location of the specified team's flag";
		message += "\n" + ChatColor.AQUA + "/teams get <player>" + ChatColor.GREEN + " - get the specified user's team name";
		message += "\n" + ChatColor.AQUA + "/teams info <teamName>" + ChatColor.GREEN + " - list info about the specified team";
		message += "\n=====================================";
		return message;
	}
}
