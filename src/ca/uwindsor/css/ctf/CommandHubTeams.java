package ca.uwindsor.css.ctf;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandHubTeams implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length == 0) {
			//HELP PAGE
			sender.sendMessage(helpMessage(sender));
		} else {
			if (args[0].equalsIgnoreCase("create")) {	//	/teams create <teamName> <teamColor>
				if (sender.isOp()) {
					CommandAddTeam.onCommand(sender, command, label, args);
				} else {
					denyPermissions(sender);
				}
			} else if (args[0].equalsIgnoreCase("list")) {	//	/teams list
				//list teams
				CommandGetTeams.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("add")) {	//	/teams add <player> <toTeam>
				//add player to team
				if (sender.isOp()) {
					CommandSetTeam.onCommand(sender, command, label, args);
				} else {
					denyPermissions(sender);
				}
			} else if (args[0].equalsIgnoreCase("remove")) {	//	/teams remove <player> <fromTeam>
				//remove player from team
				if (sender.isOp()) {
					CommandRemovePlayer.onCommand(sender, command, label, args);
				} else {
					denyPermissions(sender);
				}
			} else if (args[0].equalsIgnoreCase("get")) {	//	/teams get <player>
				//get user's team
				CommandGetTeam.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("info")) {	//	/teams info <team>
				//list team's users
			} else if (args[0].equalsIgnoreCase("getflag")) {	//	/teams getflag <team>
				//get flag location
				if (sender.isOp()) {
					CommandGetFlag.onCommand(sender, command, label, args);
				} else {
					denyPermissions(sender);
				}
			} else if (args[0].equalsIgnoreCase("setflag")) {	//	/teams setflag <team>
				//sets flag location for team
				CommandSetFlag.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(helpMessage(sender));
			} else if (args[0].equalsIgnoreCase("delete")) {
				if (sender.isOp()) {
					CommandDeleteTeam.onCommand(sender, command, label, args);
				} else {
					denyPermissions(sender);
				}
			} else if (args[0].equalsIgnoreCase("home")) {
				CommandHome.onCommand(sender, command, label, args);
			} else if (args[0].equalsIgnoreCase("setscore")) {	//	/teams getflag <team>
				if (sender.isOp()) {
					CommandSetScore.onCommand(sender, command, label, args);
				} else {
					denyPermissions(sender);
				}
			} else if (args[0].equalsIgnoreCase("pvp")) {
				if (sender.isOp()) {
					CommandPvP.onCommand(sender, command, label, args);
				} else {
					denyPermissions(sender);
				}
				
			}
		}
		
		return true;
		
	}
	
	
	public String helpMessage(CommandSender sender) {
		
		String message = ChatColor.GREEN + "=============WindsorCTF===============";
		
		message += "\nCommands:";
		//ADMIN COMMANDS
		if (sender.isOp()) {
			message += "\n" + ChatColor.AQUA + "/teams help" + ChatColor.GREEN + " - list help menu for plugin";
			message += "\n" + ChatColor.AQUA + "/teams home" + ChatColor.GREEN + " - teleport to your flag";
			message += "\n" + ChatColor.AQUA + "/teams create <teamName> <teamColor>" + ChatColor.GREEN + " - create new a team with the specified name/color";
			message += "\n" + ChatColor.AQUA + "/teams delete <teamName>" + ChatColor.GREEN + " - delete a team completely (removes members as well)";
			message += "\n" + ChatColor.AQUA + "/teams list" + ChatColor.GREEN + " - lists all of the current teams";
			message += "\n" + ChatColor.AQUA + "/teams add/remove <player> <team>" + ChatColor.GREEN + " - add/remove user to/from specified team";
			message += "\n" + ChatColor.AQUA + "/teams setFlag <teamName>" + ChatColor.GREEN + " - set the location of the specified team's flag";
			message += "\n" + ChatColor.AQUA + "/teams getFlag <teamName>" + ChatColor.GREEN + " - get the location of the specified team's flag";
			message += "\n" + ChatColor.AQUA + "/teams setScore <teamName> <score>" + ChatColor.GREEN + " - set specified team's score";
			message += "\n" + ChatColor.AQUA + "/teams get <player>" + ChatColor.GREEN + " - get the specified user's team name";
			message += "\n" + ChatColor.AQUA + "/teams info <teamName>" + ChatColor.GREEN + " - list info about the specified team";
			message += "\n" + ChatColor.AQUA + "/teams pvp true/false" + ChatColor.GREEN + " - enable or disable pvp manually";
		} else {
		//REGULAR COMMANDS
			message += "\n" + ChatColor.AQUA + "/teams help" + ChatColor.GREEN + " - list help menu for plugin";
			message += "\n" + ChatColor.AQUA + "/teams home" + ChatColor.GREEN + " - teleport to your flag";
			message += "\n" + ChatColor.AQUA + "/teams list" + ChatColor.GREEN + " - lists all of the current teams";
			message += "\n" + ChatColor.AQUA + "/teams setFlag" + ChatColor.GREEN + " - set the location of your team's flag";
			message += "\n" + ChatColor.AQUA + "/teams getFlag" + ChatColor.GREEN + " - get the location of your team's flag";
			message += "\n" + ChatColor.AQUA + "/teams get" + ChatColor.GREEN + " - get your team's name";
			message += "\n" + ChatColor.AQUA + "/teams info <teamName>" + ChatColor.GREEN + " - list info about your team";
		}
		message += "\n=====================================";
		return message;
	}
	
	public void denyPermissions(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You do not have permission to do that!");
	}
	
	public static boolean isConsole(CommandSender sender) {
		return (sender instanceof Player);
	}
	
	
}
