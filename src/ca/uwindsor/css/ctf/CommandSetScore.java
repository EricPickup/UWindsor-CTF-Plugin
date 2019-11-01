package ca.uwindsor.css.ctf;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;


public class CommandSetScore {
	
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length < 3) {
			sender.sendMessage(ChatColor.RED + "Invalid command usage: /teams setScore <team> <score>");
			return true;
		}
		
		String teamName = args[1];
		
		if (!Teams.containsTeam(teamName)) {
			sender.sendMessage(ChatColor.RED + "Invalid team name! Choose from: " + String.join(", ", Teams.getTeamNames()));
			return true;
		}
		
		//Checking if the new score entered is a numeric score
		try {
			Integer.parseInt(args[2]);
		} catch (NumberFormatException nfe) {
			sender.sendMessage(ChatColor.RED + "New score must be a numeric value.");
			return true;
		}
		
		Teams.getTeam(teamName).setScore(Integer.parseInt(args[2]));
		
		
		
		return true;
	}

}
