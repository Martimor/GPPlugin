package com.gp.suite;
import org.bukkit.plugin.java.JavaPlugin;

public class WebInterface extends JavaPlugin {
	
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	this.getCommand("test").setExecutor(new CommandTest());
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    }
}
