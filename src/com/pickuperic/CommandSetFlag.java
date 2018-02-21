package com.pickuperic;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.block.Banner;
import net.md_5.bungee.api.ChatColor;

public class CommandSetFlag {
	
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You cannot set the flag from console!");
			return true;
		}
		
		Player player = (Player) sender;
		
		Team team;
		
		if (args.length == 1) {	//If command entered is in format: /teams setflag
			if (Teams.getPlayerTeam(player) == null) {
				sender.sendMessage(ChatColor.RED + "You are not part of a team!");
				return true;
			}
			team = Teams.teams.get(Teams.getPlayerTeam(player));
		} else if (player.isOp()) {	//If command entered is in format: /teams setflag <teamName> - check if player is op
			
			String teamName = args[1].toUpperCase();
			if (!Teams.containsTeam(teamName)) {
				sender.sendMessage(ChatColor.RED + "Invalid team name! Choose from: " + String.join(", ", Teams.teams.keySet()));
				return true;
			} else if (Teams.teams.get(teamName).getStolenStatus() == true) {
				sender.sendMessage(ChatColor.RED + "You cannot place your flag while it is stolen!");
				return true;
			} else {
				team = Teams.teams.get(teamName);
			}
			
		} else {	//Otherwise, player is not op, reject permissions
			player.sendMessage(ChatColor.RED + "You do not have permissions to set the flag of another team!");
			return true;
		}
		
		if (team.hasBanner()) {
			if (enemiesNearBanner(team)) {
				player.sendMessage(ChatColor.RED + "You cannot move the flag when enemies are near it!");
				return true;
			}
			player.sendMessage(ChatColor.GREEN + "Removed previously saved flag at location " + team.getBannerCoordinates());
			team.removeBanner();
		}
		
		World w = Bukkit.getServer().getWorlds().get(0);
		Block block = w.getBlockAt(player.getLocation());
		block.setType(Material.STANDING_BANNER);
		Banner banner = (Banner) block.getState();
		
		banner.setBaseColor(team.getBannerColor());
		banner.update();
		
		sender.sendMessage(ChatColor.GREEN + "You've successfully set the flag location for the " + team.printTeamName() + ChatColor.GREEN + " team at X: " + block.getX() + " Y: " + block.getY() +
			" Z: " + block.getZ());
		team.addBanner(block);
		return true;
	}
	
	public static boolean enemiesNearBanner(Team team) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			double distance = player.getLocation().distance(team.getBannerSpawn());
			System.out.println("Player's team: " +  Teams.getPlayerTeam(player));
			System.out.print("Current team's name: " + team.getName());
			
			if (distance < 150 && (Teams.getPlayerTeam(player) == null || !Teams.getPlayerTeam(player).equalsIgnoreCase(team.getName()))) {
				return true;
			}
		}
		return false;
	}
}
