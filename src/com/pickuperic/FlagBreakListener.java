package com.pickuperic;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import net.md_5.bungee.api.ChatColor;

public class FlagBreakListener implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		
		Block eventBlock = event.getBlock();
		
		if (eventBlock.getType() == Material.STANDING_BANNER) {	//If banner is destroyed
			
			for (String team : Teams.teams.keySet()) {	
				
				if (eventBlock.equals(Teams.teams.get(team).getBannerBlock())) {
					
					Player player = event.getPlayer();
					
					if (Teams.teams.get(team).containsPlayer(player)) {
						
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED + "You cannot break your own flag!");
						
					} else {
						ChatColor playerColor;
						if (Teams.getPlayerTeam(player) == null) {	//Player is not part of team, set chat color to white
							playerColor = ChatColor.WHITE;
						} else {
							playerColor = Teams.getPlayerColor(player);
						}
						
						Team victimTeam = Teams.teams.get(team);
						
						Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
						Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + victimTeam.printTeamName() + ChatColor.GREEN + 
								"'S FLAG HAS BEEN STOLEN BY " + playerColor + player.getDisplayName() + ChatColor.GREEN + "!");
						Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + victimTeam.printTeamName() + ChatColor.RESET + ChatColor.GREEN + 
								"'S FLAG HAS BEEN STOLEN BY " + playerColor + player.getDisplayName() + ChatColor.GREEN + "!");
						Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + victimTeam.printTeamName() + ChatColor.RESET + ChatColor.GREEN + 
								"'S FLAG HAS BEEN STOLEN BY " + playerColor + player.getDisplayName() + ChatColor.GREEN + "!");
						Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
						
						Teams.teams.get(team).removeBanner();
						
						ItemStack i = new ItemStack(Material.BANNER, 1);
						BannerMeta m = (BannerMeta)i.getItemMeta();
						m.setBaseColor(victimTeam.getBannerColor());
						m.setDisplayName(victimTeam.getColor() + team + "'S FLAG");
						i.setItemMeta(m);
						
						player.getInventory().addItem(i);
						Teams.carriers.put(player, Teams.teams.get(team));
				
					}
				} else if (eventBlock.equals(Teams.teams.get(team).getStolenBanner())) {		//If broken flag is a stolen (temporarily placed flag)
					
					Player player = event.getPlayer();
					
					if (Teams.teams.get(team).containsPlayer(player)) {
						
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED + "You cannot break your own flag!");
					} else {
						
						ChatColor playerColor;
						if (Teams.getPlayerTeam(player) == null) {	//Player is not part of team, set chat color to white
							playerColor = ChatColor.WHITE;
						} else {
							playerColor = Teams.getPlayerColor(player);
						}
						
						Team victimTeam = Teams.teams.get(team);

						Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
						Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + victimTeam.printTeamName() + ChatColor.GREEN + 
								"'S WAS PICKED UP BY " + playerColor + player.getDisplayName() + ChatColor.GREEN + "!");
						Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
						
						victimTeam.removeStolenBanner();
						
						ItemStack i = new ItemStack(Material.BANNER, 1);
						BannerMeta m = (BannerMeta)i.getItemMeta();
						m.setBaseColor(victimTeam.getBannerColor());
						m.setDisplayName(victimTeam.getColor() + team + "'S FLAG");
						i.setItemMeta(m);
						
						player.getInventory().addItem(i);
						Teams.carriers.put(player, Teams.teams.get(team));
					}
					
				}
			}
		}
		
	}

	
}
