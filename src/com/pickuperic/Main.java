package com.pickuperic;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	// Fired when first enabled
    @Override
    public void onEnable() {
    	this.getCommand("placeflag").setExecutor(new CommandPlaceFlag());
    	this.getCommand("getflag").setExecutor(new CommandGetFlag());
    	this.getCommand("setteam").setExecutor(new CommandSetTeam());
    	getServer().getPluginManager().registerEvents(new FlagBreakListener(), this);
    }
    // Fired when disabled
    @Override
    public void onDisable() {

    }    

}

