package com.pickuperic;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.md_5.bungee.api.ChatColor;

public class Safezones {
	
	public static ArrayList<Team> list = new ArrayList<Team>();
	
	public static void addTeam(Team team) {
		
		list.add(team);
		
		for (String memberName : team.members) {	//Notify members
			Player member = Bukkit.getServer().getPlayerExact(memberName);
			if (member != null) {	//If member is offline, this would be null
				member.sendMessage(ChatColor.GREEN + "Your flag was captured! Your base is now under a 10-minute grace-period where you can recover from your losses"
						+ " and rebuild. Nobody can enter your area but your team.");
			}
		}
		
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(JavaPlugin.getPlugin(Main.class), new Runnable() {
			@Override
			public void run() {
				list.remove(team);
				for (String memberName : team.members) {	//Notify members
					Player member = Bukkit.getServer().getPlayerExact(memberName);
					if (member != null) {	//If member is offline, this would be null
						member.sendMessage(ChatColor.GREEN + "Your base is no longer under protection by the grace-period!");
					}
					SafezoneListener.violationsPerPlayer.clear();	//Clear violations
				}
				Bukkit.broadcastMessage(ChatColor.GREEN + "Team " + team.printTeamName() + ChatColor.GREEN + "'s base is no longer under protection by the grace-period!");
			}
		}, 12000L);
	}

}
