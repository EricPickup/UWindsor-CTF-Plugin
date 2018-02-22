package com.pickuperic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class Main extends JavaPlugin {
	
	public static Scoreboard board;
	public static HashMap<Score, Integer> scores;
	// Fired when first enabled
    @Override
    public void onEnable() {

    	this.getCommand("teams").setExecutor(new CommandHubTeams());
    	board = Bukkit.getScoreboardManager().getMainScoreboard();
    	Teams.availableColors.add("RED");
		Teams.availableColors.add("AQUA");
		Teams.availableColors.add("GOLD");
		Teams.availableColors.add("GRAY");
		Teams.availableColors.add("GREEN");
		Teams.availableColors.add("YELLOW");
		Teams.availableColors.add("BLUE");
		Teams.availableColors.add("LIGHT_PURPLE");
		Teams.availableColors.add("DARK_PURPLE");
		Teams.availableColors.add("WHITE");
		System.out.println(Teams.availableColors);
		
		for (org.bukkit.scoreboard.Team team : board.getTeams()) {
			team.unregister();
		}
		
		for (String entry : board.getEntries()) {
			Player player = Bukkit.getServer().getPlayerExact(entry);
			if (player != null) {
				player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
		}
		
		for (Objective objective : board.getObjectives()) {
			objective.unregister();
		}
		
		Objective objective = board.registerNewObjective("Team Scores", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("Team Scores");
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.setScoreboard(board);
		}	
		
		loadConfiguration();
		
    	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    	console.sendMessage("[BaseCTF] Loaded config");
		
    	getServer().getPluginManager().registerEvents(new FlagBreakListener(), this);
    	getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    	getServer().getPluginManager().registerEvents(new FlagIndirectBreakListener(), this);
    	getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
    	getServer().getPluginManager().registerEvents(new CarrierListeners(), this);
    	getServer().getPluginManager().registerEvents(new SafezoneListener(), this);
    	getServer().getPluginManager().registerEvents(new CommandHome(), this);
    	
    	
    	Iterator<Recipe> it = getServer().recipeIterator();
        Recipe recipe;
        while(it.hasNext())
        {
            recipe = it.next();
            if (recipe != null && recipe.getResult().getType() == Material.BANNER)
            {
                it.remove();
            }
        }
    	
    }
    // Fired when disabled
    @Override
    public void onDisable() {
    	saveConfiguration();
    	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    	console.sendMessage("[BaseCTF] Saved config");
    }    
    
    public void loadConfiguration() {
    	
    	//See "Creating you're defaults"
        getConfig().options().copyDefaults(true); // NOTE: You do not have to use "plugin." if the class extends the java plugin
        //Save the config whenever you manipulate it
        saveConfig();
        
        List<String> currentTeamMembers;
        
        for (String team : getConfig().getConfigurationSection("Teams").getKeys(false)) {
        	String path = "Teams." + team;
        	Teams.addTeam(getConfig().getString(path + ".name"), getConfig().getString(path + ".color"));	//Add the team
        	currentTeamMembers = getConfig().getStringList(path + ".members");
        	for (String member : currentTeamMembers) {	//Adding members to team
        		Teams.getTeam(team).addPlayerByName(member);
        	}
        	Teams.getTeam(team).setScore(getConfig().getInt(path + ".score"));
        	if (!getConfig().getString(path + ".bannerLocation").equals("null")) {	//If they have a banner placed
        		String[] coords = getConfig().getString(path + ".bannerLocation").split("\\s+");
        		Teams.getTeam(team).addBannerByCoords(coords);
        	} else {
        		System.out.println("Banner location null");
        	}
        }
       
    	saveConfig();
    }
    
    public void saveConfiguration() {
    	getConfig().set("Teams", "");
    	for (Team team : Teams.getTeamsValues()) {
    		String tableName = team.getName().toUpperCase();
    		String configPath = "Teams." + tableName;
    		getConfig().set(configPath + ".name", team.getName());
    		getConfig().set(configPath + ".color", team.getColorString());
    		if (team.getBannerBlock() == null) {
    			getConfig().set(configPath + ".bannerLocation", "null");
    		} else {
    			getConfig().set(configPath + ".bannerLocation", team.getBannerCoordinatesConfig());
    		}
    		getConfig().set(configPath + ".members", Teams.getTeam(tableName).members);
    		getConfig().set(configPath + ".score", team.getScore());
    		System.out.println("Saved info for team " + tableName);
    	}
    	saveConfig();
    }
    
}

