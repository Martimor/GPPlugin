package com.gp.suite;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class WebInterface extends JavaPlugin {
	
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	this.getCommand("test").setExecutor(new CommandTest());
    	getServer().getPluginManager().registerEvents(new MyListener(), this);
    }
    
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
    }
}
