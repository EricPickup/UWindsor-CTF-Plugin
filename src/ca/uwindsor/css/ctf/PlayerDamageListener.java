package ca.uwindsor.css.ctf;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class PlayerDamageListener implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		
		//Check for team-hitting
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {	//If the attacker and victim are players
			
			Player attacker = (Player) event.getDamager();
			Player victim = (Player) event.getEntity();
			
			if (TeamManager.getPlayerTeam(attacker).equals(TeamManager.getPlayerTeam(victim))) {
				attacker.sendMessage(ChatColor.RED + "You cannot attack your own team members!");
				event.setCancelled(true);
				return;
			}

		}
		
		//Check if PVP is enabled
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player && Main.pvpEnabled == false) {
			event.setCancelled(true);
			event.getDamager().sendMessage(ChatColor.RED + "PvP is currently disabled!");
			return;
		}
		
		//Check if PvP is inside spawn
		if (event.getEntity() instanceof Player && event.getEntity().getLocation().distance(event.getEntity().getWorld().getSpawnLocation()) < 30) {
			event.getDamager().sendMessage(ChatColor.RED + "PvP is disabled in spawn!");
			event.setCancelled(true);
			return;
		}
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		CommandHome.cooldown.add(player);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			@Override
			public void run() {
				CommandHome.cooldown.remove(player);
			}
		}, 2400);
	}
}
