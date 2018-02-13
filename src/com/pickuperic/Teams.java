package com.pickuperic;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

//Contains list of players with their associating teams

public class Teams {
	
	public static HashMap<String, Team> teams = new HashMap<String, Team>();
	static ArrayList<String> availableColors = new ArrayList<String>();
	
	
	public static boolean addTeam(String teamName, String teamColor) {
		teamColor = teamColor.toUpperCase();
		if (containsTeam(teamName)) {
			return false;
		} else if(!availableColors.contains(teamColor)) {
			System.out.println("INVALID COLOR, does not contain " + teamColor);
			System.out.println(availableColors);
			return false;
		} else {
			teams.put(teamName.toUpperCase(), new Team(teamName, teamColor));
			return true;
		}
	}
	
	public static boolean containsTeam(String teamName) {
		return teams.containsKey(teamName.toUpperCase());
	}
	
	public static boolean removeTeam(String teamName) {
		teamName = teamName.toUpperCase();
		if (containsTeam(teamName)) {
			teams.remove(teamName);
			return true;
		}
		return false;
	}
	
	public static String getPlayerTeam(Player player) {
		for (Team team : teams.values()) {
			if (team.containsPlayer(player)) {
				return team.getName().toUpperCase();
			}
		}
		return null;
	}
	
	public static ChatColor getPlayerColor(Player player) {
		return Teams.teams.get(getPlayerTeam(player).toUpperCase()).getColor();
	}
}
