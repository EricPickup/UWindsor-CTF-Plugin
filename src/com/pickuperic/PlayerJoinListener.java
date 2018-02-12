package com.pickuperic;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		if (Teams.getPlayerTeam(event.getPlayer()) == null) {
			Teams.teams.get("BASE").addPlayer(event.getPlayer());
		}
		
	}

}
