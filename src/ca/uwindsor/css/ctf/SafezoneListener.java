package ca.uwindsor.css.ctf;

import java.util.Hashtable;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.md_5.bungee.api.ChatColor;

public class SafezoneListener implements Listener {
	
	public static Hashtable<Player,Integer> violationsPerPlayer = new Hashtable<Player,Integer>();
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
	
		
		Player player = event.getPlayer();
		
		for (Team team : Safezones.list) {	//For every team currently on safezone cool down
			double distance = event.getTo().distance(team.getBannerSpawn());
			if (distance < 5 && !team.containsPlayer(player)) {
				event.setCancelled(true);
				if (violationsPerPlayer.containsKey(player)) {
					if (violationsPerPlayer.get(player) > 50) {	//If the player has violated more than 30 times (most likely stuck)
						if (Teams.getPlayerTeam(player) == null) {		//If they are not part of a team, teleport them to spanw
							player.teleport(player.getWorld().getSpawnLocation());
						} else {	//If they do belong to a team, teleport them to their own flag
							player.teleport(Teams.getPlayerTeam(player).getBannerSpawn());
						}
						player.sendMessage(ChatColor.GREEN + "Teleported you back to base, detected that you were stuck.");
						violationsPerPlayer.remove(player);
					} else {
						violationsPerPlayer.put(player, violationsPerPlayer.get(player) + 1);	//Increase violations by 1
					}
				} else {
					violationsPerPlayer.put(player, 1);
				}
				player.sendMessage(ChatColor.RED + "You cannot enter this area! Team " + team.printTeamName() + ChatColor.RED + " is currently on a grace-period for 10"
						+ " minutes after flag has been captured to recover!");
			}
			
			
		}

	}
	
}
