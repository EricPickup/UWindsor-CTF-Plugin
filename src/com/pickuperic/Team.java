package com.pickuperic;

import java.util.ArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Team {
	
	private String teamName;
	private String teamColor;
	private ArrayList<String> members = new ArrayList<String>();
	private Block bannerBlock;
	private DyeColor bannerColor;
	
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
	}
	
	public void addPlayer(Player player) {
		members.add(player.getDisplayName());
		player.sendMessage(ChatColor.GREEN + "You've been added to team " + ChatColor.valueOf(teamColor) + ChatColor.BOLD + teamName + 
				ChatColor.RESET + ChatColor.GREEN + ".");
	}
	
	public void removePlayer(Player player) {
		members.remove(player.getDisplayName());
		player.sendMessage(ChatColor.GREEN + "You've been removed from team " + ChatColor.valueOf(teamColor) + ChatColor.BOLD + teamName + 
				ChatColor.RESET + ChatColor.GREEN + ".");
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
		return members.contains(player.getDisplayName());
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
		return ("" + getColor() + ChatColor.BOLD + teamName.toUpperCase() + ChatColor.RESET);
	}
	
	public Block getBannerBlock() {
		return bannerBlock;
	}
	
}
