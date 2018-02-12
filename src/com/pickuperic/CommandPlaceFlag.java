package com.pickuperic;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.block.Banner;
import net.md_5.bungee.api.ChatColor;

public class CommandPlaceFlag implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		String teamName = args[0].toUpperCase();
		
		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			
			if (args.length == 0) {
				
				player.sendMessage(ChatColor.RED + "Invalid command usage! /placeflag <color>");
			
			} else if (!Teams.containsTeam(teamName)) {
				player.sendMessage(ChatColor.RED + "Invalid team name! Choose from: " + String.join(", ", Teams.teams.keySet()));
				
			} else {

				if (Teams.teams.get(teamName).hasBanner()) {
					player.sendMessage(ChatColor.GREEN + "Removed previously saved flag at location " + Teams.teams.get(teamName).getBannerCoordinates());
					Teams.teams.get(teamName).removeBanner();
				}
				
				World w = Bukkit.getServer().getWorlds().get(0);
				Block block = w.getBlockAt(player.getLocation());
				block.setType(Material.STANDING_BANNER);
				Banner banner = (Banner) block.getState();
				
				banner.setBaseColor(Teams.teams.get(teamName).getBannerColor());
				banner.update();
				
				player.sendMessage(ChatColor.GREEN + "You've successfully set the flag location for the " + Teams.teams.get(teamName).printTeamName() + ChatColor.GREEN + " team at X: " + block.getX() + " Y: " + block.getY() +
					" Z: " + block.getZ());
				Teams.teams.get(teamName).addBanner(block);
			}
		}
		return false;
	}
}
