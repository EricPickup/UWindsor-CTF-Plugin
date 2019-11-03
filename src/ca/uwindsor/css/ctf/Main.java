package ca.uwindsor.css.ctf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class Main extends JavaPlugin {
	
	public static Scoreboard board;
	public static Scoreboard timerBoard;
	public static HashMap<Score, Integer> scores;
	public static boolean pvpEnabled = false;
	// Fired when first enabled
    @Override
    public void onEnable() {
    	
    	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
    	//COMMAND AND LISTENERS
    	this.getCommand("teams").setExecutor(new TeamsCommandManager());
    	getServer().getPluginManager().registerEvents(new FlagBreakListener(), this);
    	getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    	getServer().getPluginManager().registerEvents(new FlagIndirectBreakListener(), this);
    	getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
    	getServer().getPluginManager().registerEvents(new FlagCarrierListener(), this);
    	getServer().getPluginManager().registerEvents(new SafezoneListener(), this);
    	getServer().getPluginManager().registerEvents(new CommandHome(), this);
    	
    	Main.board = Bukkit.getScoreboardManager().getMainScoreboard();
    	
    	loadColors();
		
		wipeScoreboard();	//Scoreboards save through bukkit, need to wipe and re-add them on reload/start so they're in sync with plugin
		constructScoreboard();
		
		console.sendMessage("[BaseCTF] Loading config..");
		loadConfiguration();
    	console.sendMessage("[BaseCTF] Successfully loaded config");
    	
    	disableBannerCrafting();
    	initializeCompasses();
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
        
        if (getConfig().getConfigurationSection("Teams") != null) {
        	for (String team : getConfig().getConfigurationSection("Teams").getKeys(false)) {
            	String path = "Teams." + team;
            	TeamManager.addTeam(getConfig().getString(path + ".name"), getConfig().getString(path + ".color"));	//Add the team
            	currentTeamMembers = getConfig().getStringList(path + ".members");
            	for (String member : currentTeamMembers) {	//Adding members to team
            		TeamManager.getTeam(team).addPlayerByName(member);
            	}
            	TeamManager.getTeam(team).setScore(getConfig().getInt(path + ".score"));	//Adding team to scoreboard
            	if (!getConfig().getString(path + ".bannerLocation").equals("null")) {	//If they have a banner placed
            		String[] coords = getConfig().getString(path + ".bannerLocation").split("\\s+");
            		TeamManager.getTeam(team).addBannerByCoords(coords);
            	}
            }
        } else {
        	Bukkit.getServer().getConsoleSender().sendMessage("[BaseCTF] No teams in configuration");
        }
       
    	saveConfig();
    }
    
    public void saveConfiguration() {
    	getConfig().set("Teams", "");
    	for (Team team : TeamManager.getTeams()) {
    		String tableName = team.getName().toUpperCase();
    		String configPath = "Teams." + tableName;
    		getConfig().set(configPath + ".name", team.getName());
    		getConfig().set(configPath + ".color", team.getColorString());
    		if (team.getBannerBlock() == null) {
    			getConfig().set(configPath + ".bannerLocation", "null");
    		} else {
    			getConfig().set(configPath + ".bannerLocation", team.getBannerCoordinatesConfig());
    		}
    		getConfig().set(configPath + ".members", TeamManager.getTeam(tableName).members);
    		getConfig().set(configPath + ".score", team.getScore());
    		System.out.println("Saved info for team " + tableName);
    	}
    	saveConfig();
    }
    
    public void wipeScoreboard() {
    	
    	//Remove teams from scoreboard
    	for (org.bukkit.scoreboard.Team team : board.getTeams()) {
			team.unregister();
		}
		
		for (String entry : board.getEntries()) {
			Player player = Bukkit.getServer().getPlayerExact(entry);
			if (player != null) {
				player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
		}
		
		//Remove all objectives (objectives are simply each team and their score)
		for (Objective objective : board.getObjectives()) {
			objective.unregister();
		}
    }
    
    public void constructScoreboard() {
		//Constructing side scoreboard
		Objective objective = board.registerNewObjective("Scoreboard", "Scoreboard");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("Team Scores");

		//Assign scoreboard to every player in server
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.setScoreboard(board);
		}
    }

    public void disableBannerCrafting() {
    	Iterator<Recipe> it = getServer().recipeIterator();
        Recipe recipe;
        while(it.hasNext())
        {
            recipe = it.next();
            if (recipe != null && Tag.BANNERS.isTagged(recipe.getResult().getType()))
            {
                it.remove();
            }
        }
    }
    
    public void loadColors() {
    	TeamManager.availableColors.add("RED");
		TeamManager.availableColors.add("AQUA");
		TeamManager.availableColors.add("GOLD");
		TeamManager.availableColors.add("GRAY");
		TeamManager.availableColors.add("GREEN");
		TeamManager.availableColors.add("YELLOW");
		TeamManager.availableColors.add("BLUE");
		TeamManager.availableColors.add("LIGHT_PURPLE");
		TeamManager.availableColors.add("DARK_PURPLE");
		TeamManager.availableColors.add("WHITE");
    }
    
    public void initializeCompasses() {
    	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					double minDistance = 999999;
					Location closestFlag = null;
					for (Team team : TeamManager.getTeams()) {
						if (TeamManager.getPlayerTeam(player) != null && !(TeamManager.getPlayerTeam(player).equals(team))) {
							double distanceFromEnemyTeam = player.getLocation().distance(team.getBannerSpawn());
							if (distanceFromEnemyTeam < minDistance) {
								minDistance = distanceFromEnemyTeam;
								closestFlag = team.getBannerSpawn();
							}
						}
					}
					if (closestFlag != null) {
						player.setCompassTarget(closestFlag);
					}
				}
			
			}
		}, 0L, 250L);
    }
}
