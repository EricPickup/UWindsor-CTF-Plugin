package ca.uwindsor.css.ctf;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandGetTeam {
	
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player;
		
		if (args.length == 1) {
			player = (Player) sender;
		} else {
			player = Bukkit.getServer().getPlayerExact(args[1]);
		}

		if (player == null) {
			sender.sendMessage(ChatColor.RED + "Invalid user!");
		} else if (Teams.getPlayerTeam(player) == null) {
			sender.sendMessage(ChatColor.GREEN + "User " + player.getDisplayName() + ChatColor.GREEN + " is not part of a team.");
		} else {
			sender.sendMessage(ChatColor.GREEN + "Player "+ player.getDisplayName() + ChatColor.GREEN + " is part of team " + Teams.getPlayerTeam(player).printTeamName() + ChatColor.GREEN + ".");
		}
	
		 return true;
	}
	
}
