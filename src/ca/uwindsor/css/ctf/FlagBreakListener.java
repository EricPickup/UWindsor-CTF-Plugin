package ca.uwindsor.css.ctf;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class FlagBreakListener implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		
		Block eventBlock = event.getBlock();
		
		if (Tag.BANNERS.isTagged(eventBlock.getType())) {	//If banner is destroyed
			
			for (String team : TeamManager.getTeamNames()) {		//For each team
				
				Player player = event.getPlayer();
				
				if (TeamManager.getPlayerTeam(player) != null) {
					Team attackerTeam = TeamManager.getPlayerTeam(player);
					if (Safezones.list.contains(attackerTeam)) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED + "You cannot capture an enemy flag while your team is in the grace-period!");
						return;
					}
				}
				
				Team victimTeam = TeamManager.getTeam(team);
				
				if (eventBlock.equals(victimTeam.getBannerBlock())) {			//IF FLAG BROKEN IS THE TEAM'S MAIN FLAG (FROM THEIR BASE)
					
					
					if (victimTeam.containsPlayer(player)) {		//If breaker is breaking their own flag
					
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED + "You cannot break your own flag!");
						
					} else if (player.getInventory().firstEmpty() == -1) {		//If breaker has no space in their inventory
						
						player.sendMessage(ChatColor.RED + "YOU HAVE NO SPACE IN YOUR INVENTORY TO HOLD THE FLAG!!!");	
						event.setCancelled(true);
						
					} else {
						
						//Cannot alter drops from BlockBreakEvent, so to prevent the flag entity from dropping, cancel the block break and set block to air instead
						event.setCancelled(true);	
						eventBlock.setType(Material.AIR);
						
						ChatColor playerColor;
						if (TeamManager.getPlayerTeam(player) == null) {	//Player is not part of team, set their chat color to white
							playerColor = ChatColor.WHITE;
						} else {
							playerColor = TeamManager.getPlayerColor(player);
						}
						
						
						Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
						Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + victimTeam.printTeamName() + ChatColor.GREEN + 
								"'S FLAG HAS BEEN STOLEN BY " + playerColor + player.getDisplayName() + ChatColor.GREEN + "!");
						Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + victimTeam.printTeamName() + ChatColor.RESET + ChatColor.GREEN + 
								"'S FLAG HAS BEEN STOLEN BY " + playerColor + player.getDisplayName() + ChatColor.GREEN + "!");
						Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + victimTeam.printTeamName() + ChatColor.RESET + ChatColor.GREEN + 
								"'S FLAG HAS BEEN STOLEN BY " + playerColor + player.getDisplayName() + ChatColor.GREEN + "!");
						Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
						
						victimTeam.setStolenStatus(true);
						
						ItemStack i = new ItemStack(victimTeam.getBannerMaterial(), 1);
						BannerMeta m = (BannerMeta)i.getItemMeta();
						m.setDisplayName(victimTeam.getColor() + team + "'S FLAG");
						i.setItemMeta(m);
						
						//Replacing held item slot with flag
						player.getInventory().addItem(player.getItemInHand());
						player.setItemInHand(i);
						TeamManager.flagCarriers.put(player, victimTeam);
						playAlarmForVictimTeam(victimTeam);
					}
				} else if (eventBlock.equals(victimTeam.getStolenBanner())) {		//IF FLAG BROKEN IS THE TEAM'S TEMPORARY FLAG (already stolen, carrier was killed so flag is dropped on their body for 30s)
					
					
					if (victimTeam.containsPlayer(player)) {
						
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED + "You cannot break your own flag!");
						
					} else if (player.getInventory().firstEmpty() == -1) {
						player.sendMessage(ChatColor.RED + "YOU HAVE NO SPACE IN YOUR INVENTORY TO HOLD THE FLAG!!!");
						event.setCancelled(true);
					} else {
						
						//Cannot alter drops from BlockBreakEvent, so to prevent the flag entity from dropping, cancel the block break and set block to air instead
						event.setCancelled(true);	
						eventBlock.setType(Material.AIR);
						
						ChatColor playerColor;
						if (TeamManager.getPlayerTeam(player) == null) {	//Player is not part of team, set chat color to white
							playerColor = ChatColor.WHITE;
						} else {
							playerColor = TeamManager.getPlayerColor(player);
						}

						Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
						Bukkit.broadcastMessage(ChatColor.GREEN + "TEAM " + victimTeam.printTeamName() + ChatColor.GREEN + 
								"'S WAS PICKED UP BY " + playerColor + player.getDisplayName() + ChatColor.GREEN + "!");
						Bukkit.broadcastMessage(ChatColor.DARK_RED + "=====================================================");
						
						victimTeam.removeStolenBanner();
						
						ItemStack i = new ItemStack(victimTeam.getBannerMaterial(), 1);
						BannerMeta m = (BannerMeta)i.getItemMeta();
						m.setDisplayName(victimTeam.getColor() + team + "'S FLAG");
						i.setItemMeta(m);
						
						//Replacing held item slot with flag
						player.getInventory().addItem(player.getItemInHand());
						player.setItemInHand(i);
						TeamManager.flagCarriers.put(player, victimTeam);
					}
					
				}
			}
		}
	}

	public void playAlarmForVictimTeam(Team victimTeam) {
		new BukkitRunnable() {
			int count = 0;
			public void run() {
				if (count < 10) {
					for (Player p : victimTeam.getPlayers()) {
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 10, 4);
					}
				} else {
					this.cancel();
				}
				count++;
			}
		}.runTaskTimer(Main.getPlugin(Main.class), 0L, 3L);
	}
}
