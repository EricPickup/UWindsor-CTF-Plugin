package com.pickuperic;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.md_5.bungee.api.ChatColor;

public class FlagBreakListener implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		
		Block eventBlock = event.getBlock();
		
		if (eventBlock.getType() == Material.STANDING_BANNER) {	//If banner is destroyed
			
			for (String team : Teams.getBannerKeys()) {	
				
				if (eventBlock.equals(Teams.getBannerBlock(team))) {
					String chatColor = Teams.getTeam(event.getPlayer());
					if (chatColor.equals("ORANGE"))
						chatColor = "GOLD";
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
					Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + ChatColor.valueOf(team.equals("ORANGE") ? "GOLD" : team) + ChatColor.BOLD + team + ChatColor.RESET + ChatColor.GREEN + 
							"'S FLAG HAS BEEN BROKEN BY " + ChatColor.valueOf(chatColor) + event.getPlayer().getDisplayName() + ChatColor.GREEN + "!");
					Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + ChatColor.valueOf(team.equals("ORANGE") ? "GOLD" : team) + ChatColor.BOLD + team + ChatColor.RESET + ChatColor.GREEN + 
							"'S FLAG HAS BEEN BROKEN BY " + ChatColor.valueOf(chatColor) + event.getPlayer().getDisplayName() + ChatColor.GREEN + "!");
					Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + ChatColor.valueOf(team.equals("ORANGE") ? "GOLD" : team) + ChatColor.BOLD + team + ChatColor.RESET + ChatColor.GREEN + 
							"'S FLAG HAS BEEN BROKEN BY " + ChatColor.valueOf(chatColor) + event.getPlayer().getDisplayName() + ChatColor.GREEN + "!");
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
					
				}
			}
		}
		
	}

	
}
