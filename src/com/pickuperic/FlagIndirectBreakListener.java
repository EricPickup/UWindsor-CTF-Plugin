package com.pickuperic;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class FlagIndirectBreakListener implements Listener {
	
	@EventHandler
	public void onPhysics(BlockPhysicsEvent event) {
		
		Block eventBlock = event.getBlock();
		
		if (eventBlock.getType() == Material.STANDING_BANNER) {	//If banner is destroyed
			event.setCancelled(true);
		}
	}
	
}
