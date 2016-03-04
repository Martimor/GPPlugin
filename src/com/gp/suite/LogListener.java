package com.gp.suite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class LogListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
    	event.setJoinMessage("Welcome, " + event.getPlayer().getName() + "!");    	
    	
    	Statement statement;
		try {
			statement = GP2PPLugin.connection.createStatement();

	   	 	Date date= new java.util.Date();
			ResultSet rs = statement.executeQuery("SELECT id FROM gp_mc_user WHERE `uuid` = '"+event.getPlayer().getUniqueId()+"'");
			if (rs.next()) 
		    statement.executeUpdate("UPDATE gp_mc_user SET `letzterlogin` = '"+(new Timestamp(date.getTime()))+"', `aktiv` = true WHERE `id` = " + rs.getInt("id"));
			else
	    	statement.executeUpdate("INSERT INTO gp_mc_user (`username`, `uuid`, `aktiv`) VALUES ('"+event.getPlayer().getName()+"','"+event.getPlayer().getUniqueId()+"', true)");
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {

    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	Statement statement;
		try {
			statement = GP2PPLugin.connection.createStatement();
			
	   	 	Date date= new java.util.Date();
		    statement.executeUpdate("UPDATE gp_mc_user SET `letzterlogout` = '"+(new Timestamp(date.getTime()))+"', `aktiv` = false WHERE `uuid` = '"+event.getPlayer().getUniqueId()+"'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
