package com.pickuperic;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import net.md_5.bungee.api.ChatColor;

public class Team {
	
	private String teamName;
	private String teamColor;
	private ArrayList<String> members = new ArrayList<String>();
	private Block bannerBlock;
	private DyeColor bannerColor;
	public static Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
	private org.bukkit.scoreboard.Team scoreboardTeam;
	
	public Team (String teamName, String teamColor) {
		
		this.teamName = teamName;
		this.teamColor = teamColor.toUpperCase();
		bannerBlock = null;
		teamColor = teamColor.toUpperCase();
		if (teamColor.equals("GOLD"))
			this.bannerColor = DyeColor.ORANGE;
		else if (teamColor.equals("DARK_PURPLE"))
			this.bannerColor = DyeColor.PURPLE;
		else if (teamColor.equals("AQUA"))
			this.bannerColor = DyeColor.CYAN;
		else if (teamColor.equals("LIGHT_PURPLE"))
			this.bannerColor = DyeColor.PINK;
		else
			this.bannerColor = DyeColor.valueOf(teamColor);
		this.scoreboardTeam = sb.registerNewTeam(teamName.toUpperCase());
		this.scoreboardTeam.setPrefix(getColor().toString());
	}
	
	public void addPlayer(Player player) {
		
		members.add(player.getName());
		player.sendMessage(ChatColor.GREEN + "You've been added to team " + printTeamName() + ChatColor.GREEN + ".");
		scoreboardTeam.addEntry(player.getName());
		player.setDisplayName(getColor() + player.getName() + ChatColor.RESET);
		
		for (String team : Teams.teams.keySet()) {
			if (Teams.teams.get(team).containsPlayer(player) && !team.equals(this.teamName.toUpperCase())) {
				System.out.println(team + " != " + this.teamName.toUpperCase());
				Teams.teams.get(team).removePlayer(player);
			}
		}
	}
	
	public void removePlayer(Player player) {
		members.remove(player.getName());
		player.sendMessage(ChatColor.GREEN + "You've been removed from team " + ChatColor.valueOf(teamColor) + ChatColor.BOLD + teamName + 
				ChatColor.RESET + ChatColor.GREEN + ".");
		scoreboardTeam.removeEntry(player.getName());
	}
	
	public void addBanner(Block bannerBlock) {
		if (this.bannerBlock != null) {
			removeBanner();
		} else {
			this.bannerBlock = bannerBlock;
		}
	}
	
	public void removeBanner() {
		this.bannerBlock.setType(Material.AIR);
		this.bannerBlock = null;
	}
	
	public boolean hasBanner() {
		if (bannerBlock == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public DyeColor getBannerColor() {
		return this.bannerColor;
	}
	
	public boolean containsPlayer(Player player) {
		return members.contains(player.getName());
	}
	
	public ChatColor getColor() {
		return ChatColor.valueOf(teamColor);
	}
	
	public String getName() {
		return teamName;
	}
	
	public String getBannerCoordinates() {
		if (hasBanner()) {
			return ("X: " + bannerBlock.getX() + " Y: " + bannerBlock.getY() + " Z: " + bannerBlock.getZ());
		} else {
			return null;
		}
	}
	
	public String printTeamName() {
		return ("" + getColor() + ChatColor.BOLD + this.teamName + ChatColor.RESET);
	}
	
	public Block getBannerBlock() {
		return bannerBlock;
	}
	
}
