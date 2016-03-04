package com.gp.suite;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import com.mysql.jdbc.Connection;
public class GP2PPLugin extends JavaPlugin {
	
    final String username= getConfig().getString("Username");
    final String password= getConfig().getString("Password");
    final String url = getConfig().getString("URL");
    static Connection connection;
    
    @Override
    public void onEnable() {
    	 //getConfig().addDefault("key", true);
    	createConfig();
    	
    	try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            return;
        }
        try {
            connection = (Connection) DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	this.getCommand("info").setExecutor(new InfoCommand());
    	getServer().getPluginManager().registerEvents(new LogListener(), this);
    	getServer().getPluginManager().registerEvents(new ActionListener(), this);
    }
    
    @Override
    public void onDisable() {
        try {
                if(connection!=null && !connection.isClosed()) connection.close();
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    
    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
