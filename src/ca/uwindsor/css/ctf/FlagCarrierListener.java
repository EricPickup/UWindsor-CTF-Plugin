package ca.uwindsor.css.ctf;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class FlagCarrierListener implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if (playerIsCarryingFlag(player)) {
			
			Location abovePlayer = new Location(player.getWorld(),player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ());
			player.getLocation().getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 8);
			abovePlayer.getWorld().playEffect(abovePlayer, Effect.ENDER_SIGNAL, 8);
			
			Team playerTeam = TeamManager.getPlayerTeam(player);
			if (playerTeam != null) {
				double distance = player.getLocation().distance(playerTeam.getBannerSpawn());
				if (distance < 5) {
					Team victimTeam = TeamManager.flagCarriers.get(player);
					victimTeam.restoreBanner();
					Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
					Bukkit.broadcastMessage(ChatColor.GREEN + "Player " + TeamManager.getPlayerColor(player) + player.getName() + ChatColor.GREEN + " captured " +
							victimTeam.printTeamName() + ChatColor.GREEN + "'s flag and scored a point! Returning flag to base.");
					Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
					Safezones.addTeam(victimTeam);
					player.getInventory().remove(victimTeam.getBannerMaterial());
					playerTeam.addPoint();
					TeamManager.flagCarriers.remove(player);
					victimTeam.getBannerSpawn().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, playerTeam.getBannerSpawn(), 150);
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (playerIsCarryingFlag(event.getEntity())) {	//If the dead player was carrying a flag
			Team victimTeam = TeamManager.flagCarriers.get(event.getEntity());	//Team of the stolen flag
			Material flagMaterial = victimTeam.getBannerMaterial();
			removeFlagFropDrops(event.getDrops(), flagMaterial);
			dropFlag(victimTeam, event.getEntity());
		}
	}
	
	public void dropFlag(Team victimTeam, Entity player) {
		Material flagMaterial = victimTeam.getBannerMaterial();
		Location tempFlagLocation = player.getLocation();
		placeTemporaryBanner(player.getLocation(), flagMaterial, victimTeam);
		Bukkit.broadcastMessage(ChatColor.GREEN + "Team " + victimTeam.printTeamName() + ChatColor.GREEN + "'s flag was dropped! Will return to base after 30 seconds if not picked up.");
		TeamManager.flagCarriers.remove(player);	//Remove user from list of carriers

		//Wait 30s, check if flag is still there, if so, restore it to base
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			@Override
			public void run() {
				World w = Bukkit.getServer().getWorlds().get(0);
				if (w.getBlockAt(tempFlagLocation).getType().equals(victimTeam.getBannerMaterial())) {
					victimTeam.restoreBanner();
					Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
					Bukkit.broadcastMessage(ChatColor.GREEN + "Team " + victimTeam.printTeamName() + ChatColor.GREEN + 
							"'s flag was restored to base!");
					Bukkit.broadcastMessage(ChatColor.AQUA + "=====================================================");
				}
			}
		}, 600L);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if (Tag.BANNERS.isTagged(event.getItemDrop().getItemStack().getType())) {
			Player player = event.getPlayer();
			Team victimTeam = TeamManager.flagCarriers.get(player);	//Team of the stolen flag
			player.getInventory().remove(player.getItemInHand());
			event.getItemDrop().remove();
			dropFlag(victimTeam, player);
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
		if(TeamManager.flagCarriers.containsKey(event.getPlayer())) { 	//If disconnected player had the flag
			event.getPlayer().setHealth(0);
			TeamManager.flagCarriers.remove(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onHeld(PlayerItemHeldEvent event) {
		if (TeamManager.flagCarriers.containsKey(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (TeamManager.flagCarriers.containsKey(event.getWhoClicked())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickup(EntityPickupItemEvent event) {
		if (Tag.BANNERS.isTagged(event.getItem().getItemStack().getType())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onHandSwap(PlayerSwapHandItemsEvent event) {
		if (Tag.BANNERS.isTagged(event.getMainHandItem().getType()) || Tag.BANNERS.isTagged(event.getOffHandItem().getType())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		for (ItemStack item : event.getPlayer().getInventory().getContents()) {
			if (Tag.BANNERS.isTagged(item.getType())) {
				event.getPlayer().getInventory().remove(item);
			}
		}
	}

	public boolean playerIsCarryingFlag(Player player) {
		return TeamManager.flagCarriers.containsKey(player);
	}

	public void removeFlagFropDrops(List<ItemStack> drops, Material flagMaterial) {
		ListIterator<ItemStack> litr = drops.listIterator();
		while( litr.hasNext() ) {
			ItemStack stack = litr.next();
			if(stack.getType().equals(flagMaterial))
			{
				litr.remove();
			}
		 }
	}

	public void placeTemporaryBanner(Location location, Material flagMaterial, Team victimTeam) {
		World w = Bukkit.getServer().getWorlds().get(0);
		Block block = w.getBlockAt(location);
		block.setType(flagMaterial);
		victimTeam.addStolenBanner(block);
	}
}
