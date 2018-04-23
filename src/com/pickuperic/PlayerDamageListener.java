package com.pickuperic;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;

public class PlayerDamageListener implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		
		if (Main.pvpEnabled == false) {
			event.setCancelled(true);
			event.getDamager().sendMessage(ChatColor.RED + "PvP is currently disabled!");
		}
		
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {	//If the attacker and victim are players
			
			Player attacker = (Player) event.getDamager();
			Player victim = (Player) event.getEntity();
			
			if (Teams.getPlayerTeam(attacker).equals(Teams.getPlayerTeam(victim))) {
				attacker.sendMessage(ChatColor.RED + "You cannot attack your own team members!");
				event.setCancelled(true);
			}
			
		}
		
	}

}
