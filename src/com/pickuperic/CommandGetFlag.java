package com.pickuperic;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandGetFlag implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		
		if (sender instanceof Player) {
			if (args.length <= 0) {
				sender.sendMessage(ChatColor.RED + "Invalid arguments: /getflag <team>");
			} else if (!Teams.teams.get(args[0].toUpperCase()).hasBanner()) {
				sender.sendMessage(ChatColor.RED + "Team does not have a banner placed.");
			} else {
				String team = args[0].toUpperCase();
				args[0] = team;
				Player player = (Player) sender;
				player.sendMessage(Teams.teams.get(team).printTeamName() + ChatColor.GREEN + 
						" team's flag is located at: " + Teams.teams.get(team).getBannerCoordinates());
			}
		}
		return true;
	}

}
