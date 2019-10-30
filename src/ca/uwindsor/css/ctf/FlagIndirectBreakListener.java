package ca.uwindsor.css.ctf;

import java.util.Iterator;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FlagIndirectBreakListener implements Listener {
	
	@EventHandler
	public void onPhysics(BlockPhysicsEvent event) {
		
		Block eventBlock = event.getBlock();
		
		if (Tag.BANNERS.isTagged(eventBlock.getType())) {	//If banner is destroyed
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void entityChange(EntityChangeBlockEvent event) {
		
		Block eventBlock = event.getBlock();
		
		if (Tag.BANNERS.isTagged(eventBlock.getType())) {	//If banner is destroyed
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void blockBurn(BlockBurnEvent event) {
		Block eventBlock = event.getBlock();
		
		if (Tag.BANNERS.isTagged(eventBlock.getType())) {	//If banner is destroyed
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void blockExplode(BlockExplodeEvent event) {
		Iterator<Block> it = event.blockList().iterator();
		while (it.hasNext()) {
		    Block b= it.next();
		    if (Tag.BANNERS.isTagged(b.getType())) {
		    	it.remove();
		    }
		}
	}
	
	@EventHandler
	public void entityExplode(EntityExplodeEvent event) {
		Iterator<Block> it = event.blockList().iterator();
		while (it.hasNext()) {
		    Block b= it.next();
		    if (Tag.BANNERS.isTagged(b.getType())) {
		    	it.remove();
		    }
		}
	}
	
}
