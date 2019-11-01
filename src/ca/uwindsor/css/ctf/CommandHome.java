package ca.uwindsor.css.ctf;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;


public class CommandHome implements Listener {
	
	public static HashMap<Player, Integer> waiting = new HashMap<Player, Integer>();

	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			
			if (TeamManager.carriers.containsKey(player)) {
				player.sendMessage(ChatColor.RED + "You cannot teleport while carrying the flag!");
				return true;
			}
			
			waiting.remove(player);	//Remove any previous tp requests to prevent exploiting
			player.sendMessage(ChatColor.BLUE + "Teleport request accepted. Do not move for " + ChatColor.RED + "10 " + ChatColor.BLUE + "seconds...");
			
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			int taskID = scheduler.scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
				@Override
				public void run() {
					if (waiting.containsKey(player)) {
						player.sendMessage(ChatColor.BLUE + "Teleporting you to base...");
						waiting.remove(player);
						if (TeamManager.getPlayerTeam(player) != null) {
							player.teleport(TeamManager.getPlayerTeam(player).getBannerSpawn());
						} else {
							player.sendMessage(ChatColor.RED + "You are not a part of a team therefore you cannot be teleported to base!");
						}
					}
				}
			}, 200);
			waiting.put(player, taskID);
			
			
		}
		
		return true;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		
		if (waiting.containsKey(player)) {
			System.out.println("Test");
			player.sendMessage(ChatColor.RED + "Teleportation cancelled. You cannot move while waiting!");
			Bukkit.getScheduler().cancelTask(waiting.get(player));
			waiting.remove(player);
		}
		
	}
	
}
