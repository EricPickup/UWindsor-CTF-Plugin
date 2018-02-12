package com.pickuperic;

import java.util.ArrayList;
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
	
	static ArrayList<String> bannerColors = new ArrayList<String>(); {
		bannerColors.add("RED");
		bannerColors.add("BLUE");
		bannerColors.add("YELLOW");
		bannerColors.add("ORANGE");
		bannerColors.add("GREEN");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		String flagColor = args[0].toUpperCase();
		
		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			
			if (args.length == 0) {
				
				player.sendMessage(ChatColor.RED + "Invalid command usage! /placeflag <color>");
			
			} else if (!bannerColors.contains(flagColor)) {
				
				player.sendMessage(ChatColor.RED + "Invalid color name! Choose from: " + String.join(", ", bannerColors));
			
			} else {
				
				if (Teams.containsFlag(flagColor)) {
					player.sendMessage(ChatColor.GREEN + "Removed previously saved flag at location " + Teams.getBannerCoordinates(flagColor));
					Teams.removeBanner(flagColor);
				}
				
				World w = Bukkit.getServer().getWorlds().get(0);
				Block block = w.getBlockAt(player.getLocation());
				block.setType(Material.STANDING_BANNER);
				Banner banner = (Banner) block.getState();
				banner.setBaseColor(DyeColor.valueOf(flagColor));
				banner.update();
				
				player.sendMessage(ChatColor.GREEN + "You've successfully set the flag location for the " + ChatColor.valueOf((flagColor.equals("ORANGE") ? "GOLD" : flagColor))
					+ ChatColor.BOLD + flagColor + ChatColor.RESET + ChatColor.GREEN + " team at X: " + block.getX() + " Y: " + block.getY() +
					" Z: " + block.getZ());
				Teams.addBanner(flagColor, block);
			}
		}
		
		return true;
	}
}
