package ca.uwindsor.css.ctf;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class CarrierListeners implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		
		if (Teams.carriers.containsKey(player)) {
			
			player.getLocation().getWorld().playEffect(player.getLocation(), Effect.SMOKE, 10);
			Location abovePlayer = new Location(player.getWorld(),player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ());
			abovePlayer.getWorld().playEffect(abovePlayer, Effect.SMOKE, 10);
			
			if (Teams.getPlayerTeam(player) != null) {
				double distance = player.getLocation().distance(Teams.getPlayerTeam(player).getBannerSpawn());
				if (distance < 5) {
					Team victimTeam = Teams.carriers.get(player);
					Teams.carriers.get(player).restoreBanner();
					Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
					Bukkit.broadcastMessage(ChatColor.GREEN + "Player " + Teams.getPlayerColor(player) + player.getName() + ChatColor.GREEN + " captured " +
							victimTeam.printTeamName() + ChatColor.GREEN + "'s flag and scored a point! Returning flag to base.");
					Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
					Safezones.addTeam(victimTeam);
					player.getInventory().remove(victimTeam.getBannerMaterial());
					Teams.getPlayerTeam(player).addPoint();
					Teams.carriers.remove(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (Teams.carriers.containsKey(event.getEntity())) {	//If the dead player was carrying a flag
			Team victimTeam = Teams.carriers.get(event.getEntity());	//Team of the stolen flag
			//Remove flag from their drops
			List<ItemStack> drops = event.getDrops();
			ListIterator<ItemStack> litr = drops.listIterator();
			while( litr.hasNext() ) {
				ItemStack stack = litr.next();  
				if(stack.getType().equals(victimTeam.getBannerMaterial()))
				{
					litr.remove();
				}
			 }
			
			World w = Bukkit.getServer().getWorlds().get(0);		//Place temp flag
			Location tempLocation = event.getEntity().getLocation();
			Block block = w.getBlockAt(tempLocation);
			block.setType(victimTeam.getBannerMaterial());
			Banner banner = (Banner) block.getState();
			
			banner.setBaseColor(victimTeam.getBannerColor());
			banner.update();
			victimTeam.addStolenBanner(block);
			
			Bukkit.broadcastMessage(ChatColor.GREEN + "Team " + victimTeam.printTeamName() + ChatColor.GREEN + "'s flag was dropped! Will return to base after 30 seconds if not picked up.");
			
			Teams.carriers.remove(event.getEntity());	//Remove user from list of carriers
			
			//Wait 30s, check if flag is still there, if so, restore it to base
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					if (w.getBlockAt(tempLocation).getType().equals(victimTeam.getBannerMaterial())) {
						victimTeam.restoreBanner();
						Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
						Bukkit.broadcastMessage(ChatColor.GREEN + "Team " + victimTeam.printTeamName() + ChatColor.GREEN + 
								"'s flag was restored to base!");
						Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
					}
				}
			}, 600L);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if (Tag.BANNERS.isTagged(event.getItemDrop().getItemStack().getType())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You cannot drop the flag!");
		}
	}
	
	@EventHandler
	public void place(BlockPlaceEvent event) {
		if (Tag.BANNERS.isTagged(event.getBlock().getType())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "You cannot place banners manually!");
		}
	}
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		if(Teams.carriers.containsKey(event.getPlayer())) { 	//If disconnected player had the flag
			event.getPlayer().setHealth(0);
			Teams.carriers.remove(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onHeld(PlayerItemHeldEvent event) {
		if (Teams.carriers.containsKey(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (Teams.carriers.containsKey(event.getWhoClicked())) {
			event.setCancelled(true);
		}
	}
	
}
