package ca.uwindsor.css.ctf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

//Contains list of players with their associating teams

public class Teams {
	
	private static HashMap<String, Team> teams = new HashMap<String, Team>();	//Key: TeamName, Value: TeamObject
	public static HashMap<Player, Team> carriers = new HashMap<Player, Team>();	//Key: flagCarrier (Player), Value: Victim Team (Team)
	static ArrayList<String> availableColors = new ArrayList<String>();
	
	
	public static boolean addTeam(String teamName, String teamColor) {
		teamColor = teamColor.toUpperCase();
		if (containsTeam(teamName)) {
			return false;
		} else if(!availableColors.contains(teamColor)) {
			return false;
		} else {
			teams.put(teamName.toUpperCase(), new Team(teamName, teamColor));
			availableColors.remove(teamColor);
			return true;
		}
	}
	
	public static boolean removeTeam(Team deleteTeam) {
		if (teams.containsValue(deleteTeam)) {
			deleteTeam.purge();
			teams.remove(deleteTeam.getName().toUpperCase());
			return true;
		}
		return false;
	}
	
	public static Team getTeam(String teamName) {
		teamName = teamName.toUpperCase();
		if (teams.containsKey(teamName)) {
			return teams.get(teamName);
		}
		return null;
	}
	
	public static Set<String> getTeamNames() {
		return teams.keySet();
	}
	
	public static Collection<Team> getTeamsValues() {
		return teams.values();
	}
	
	
	
	public static boolean containsTeam(String teamName) {
		return teams.containsKey(teamName.toUpperCase());
	}
	
	public static Team getPlayerTeam(Player player) {
		for (Team team : teams.values()) {
			if (team.containsPlayer(player)) {
				return team;
			}
		}
		return null;
	}
	
	
	public static ChatColor getPlayerColor(Player player) {
		return getPlayerTeam(player).getColor();
	}
	
	public static void purgePayer(Player player) {
		for (Team team : Teams.teams.values()) {
			if (team.containsPlayer(player)) {
				team.removePlayer(player);
			}
		}
	}
}
