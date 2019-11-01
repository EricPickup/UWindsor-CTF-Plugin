package ca.uwindsor.css.ctf;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class CommandList {
	
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		String message = ChatColor.GREEN + "Current teams: ";
		
		for (Team team : TeamManager.getTeams()) {
			message += team.printTeamName();
			message += ChatColor.GREEN + ", ";
		}
		
		sender.sendMessage(message);
		
		return true;
	}
	
}
