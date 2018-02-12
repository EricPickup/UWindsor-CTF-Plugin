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
			
			for (String team : Teams.teams.keySet()) {	
				
				if (eventBlock.equals(Teams.teams.get(team).getBannerBlock())) {
					
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
					Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + Teams.teams.get(team).printTeamName() + ChatColor.GREEN + 
							"'S FLAG HAS BEEN BROKEN BY " + Teams.getPlayerColor(event.getPlayer()) + event.getPlayer().getDisplayName() + ChatColor.GREEN + "!");
					Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + Teams.teams.get(team).printTeamName() + ChatColor.RESET + ChatColor.GREEN + 
							"'S FLAG HAS BEEN BROKEN BY " + Teams.getPlayerColor(event.getPlayer()) + event.getPlayer().getDisplayName() + ChatColor.GREEN + "!");
					Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + Teams.teams.get(team).printTeamName() + ChatColor.RESET + ChatColor.GREEN + 
							"'S FLAG HAS BEEN BROKEN BY " + Teams.getPlayerColor(event.getPlayer()) + event.getPlayer().getDisplayName() + ChatColor.GREEN + "!");
					Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
					
					Teams.teams.get(team).removeBanner();
				}
			}
		}
		
	}

	
}
