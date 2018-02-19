package com.pickuperic;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import net.md_5.bungee.api.ChatColor;

public class Team {
	
	private String teamName;
	private String teamColor;
	public ArrayList<String> members = new ArrayList<String>();
	private Block bannerBlock;
	private Block stolenBannerBlock;
	private DyeColor bannerColor;
	private Location tempBannerLocation;	//Needed to restore banner
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
		System.out.println(ChatColor.GREEN + "Added team " + teamName);

	}
	
	public void addPlayer(Player player) {
		
		//Removing player from all other teams
		for (String team : Teams.teams.keySet()) {
			if (Teams.teams.get(team).containsPlayer(player) && !team.equals(this.teamName.toUpperCase())) {
				System.out.println(team + " != " + this.teamName.toUpperCase());
				Teams.teams.get(team).removePlayer(player);
			}
		}
		
		members.add(player.getName());
		player.sendMessage(ChatColor.GREEN + "You've been added to team " + printTeamName() + ChatColor.GREEN + ".");
		scoreboardTeam.addEntry(player.getName());
		player.setDisplayName(getColor() + player.getName() + ChatColor.RESET);
		player.setPlayerListName(getColor() + player.getName() + ChatColor.RESET);
	}
	
	public void addPlayerByName(String playerName) {
		members.add(playerName);
		Player player = Bukkit.getServer().getPlayerExact(playerName);
		if (player != null) {
			scoreboardTeam.addEntry(player.getName());
			player.setDisplayName(getColor() + player.getName() + ChatColor.RESET);
			player.setPlayerListName(getColor() + player.getName() + ChatColor.RESET);
		}
	}
	
	public void removePlayer(Player player) {
		if (members.remove(player.getName())) {	//If the player was successfully removed
			player.sendMessage(ChatColor.GREEN + "You've been removed from team " + ChatColor.valueOf(teamColor) + ChatColor.BOLD + teamName + 
			ChatColor.RESET + ChatColor.GREEN + ".");
			scoreboardTeam.removeEntry(player.getName());
			player.setDisplayName(player.getName());
			player.setPlayerListName(player.getName());
		}
	}
	
	public void addBanner(Block bannerBlock) {
		if (this.bannerBlock != null) {
			removeBanner();
		} else {
			this.bannerBlock = bannerBlock;
		}
		this.tempBannerLocation = bannerBlock.getLocation();
	}
	
	public void addStolenBanner(Block bannerBlock) {	//Temp banner is when banner is captured and the capturer is killed
		this.stolenBannerBlock = bannerBlock;
	}
	
	public Block getStolenBanner() {
		return this.stolenBannerBlock;
	}
	
	public void removeStolenBanner() {
		stolenBannerBlock.setType(Material.AIR);
		this.stolenBannerBlock = null;
		
	}
	
	public void restoreBanner() {
		removeStolenBanner();
		World w = Bukkit.getServer().getWorlds().get(0);
		Block block = w.getBlockAt(this.tempBannerLocation);
		block.setType(Material.STANDING_BANNER);
		Banner banner = (Banner) block.getState();
		banner.setBaseColor(getBannerColor());
		banner.update();
		addBanner(block);
	}
	
	public void addBannerByCoords(String[] coords) {
		if (this.bannerBlock != null) {
			removeBanner();
		} else {
			World w = Bukkit.getServer().getWorlds().get(0);
			Block block = w.getBlockAt(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
			block.setType(Material.STANDING_BANNER);
			Banner banner = (Banner) block.getState();
			banner.setBaseColor(getBannerColor());
			banner.update();
			this.bannerBlock = block;
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
	
	public String getColorString() {
		return this.teamColor;
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
	
	public String getBannerCoordinatesConfig() {
		if (bannerBlock == null) {
			return null;
		} else {
			return (bannerBlock.getX() + " " + bannerBlock.getY() + " " + bannerBlock.getZ());
		}
	}
	
	public String printTeamName() {
		return ("" + getColor() + ChatColor.BOLD + this.teamName + ChatColor.RESET);
	}
	
	public Block getBannerBlock() {
		return bannerBlock;
	}
	
	public void purge() {
		Player player;
		for (String playerName : members) {
			player = Bukkit.getServer().getPlayerExact(playerName);
			if (player != null) {
				player.sendMessage(ChatColor.GREEN + "You've been removed from team " + printTeamName() + ChatColor.GREEN + ".");
				scoreboardTeam.removeEntry(player.getName());
				player.setDisplayName(player.getName());
				player.setPlayerListName(player.getName());
			}
		}
	}
	
}
