package com.pickuperic;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

//Contains list of players with their associating teams

public class Teams {
	
	private static Hashtable<Player, String> players = new Hashtable<Player, String>();
	private static HashMap<String, Block> banners = new HashMap<String, Block>();
	
	public static boolean addPlayer(Player player, String team) {
		players.put(player, team);
		return true;
	}
	
	public static boolean removePlayer(Player player, String team) {
		if (players.contains(player)) {
			players.remove(player);
			return true;
		}
		return false;
	}
	
	public static Set<String> getBannerKeys() {
		return banners.keySet();
	}
	
	public static String getTeam(Player player) {
		return players.get(player);
	}
	
	public static boolean addBanner(String team, Block bannerBlock) {
		if (banners.containsKey(team)) {
			removeBanner(team);
		}
		banners.put(team, bannerBlock);
		return false;
	}
	
	public static boolean containsFlag(String team) {
		return banners.containsKey(team);
	}
	
	public static boolean removeBanner(String team) {
		if (banners.containsKey(team)) {
			banners.get(team).setType(Material.AIR);
		}
		return true;
	}
	
	public static String getBannerCoordinates(String team) {
		if (banners.containsKey(team)) {
			return ("X: " + banners.get(team).getX() + " Y: " + banners.get(team).getY() + " Z: " + banners.get(team).getZ());
		} else {
			return "Flag does not exist!";
		}
	}
	
	public static Location getBannerLocation(String team) {
		if (banners.containsKey(team)) {
			return banners.get(team).getLocation();
		} else {
			return null;
		}
	}
	
	public static Block getBannerBlock(String team) {
		return banners.get(team);
	}
}
