package com.gp.suite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;


public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
    	event.setJoinMessage("Welcome, " + event.getPlayer().getName() + "!");
    }
    
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
    	Statement statement;
		try {
			statement = WebInterface.connection.createStatement();

	   	 	Date date= new java.util.Date();
			ResultSet rs = statement.executeQuery("SELECT id FROM gp_mc_user WHERE `uuid` = '"+event.getPlayer().getUniqueId()+"'");
			if (rs.next()) 
		    statement.executeUpdate("UPDATE gp_mc_user SET `letzterlogin` = '"+(new Timestamp(date.getTime()))+"', `aktiv` = true WHERE `id` = " + rs.getInt("id"));
			else
	    	statement.executeUpdate("INSERT INTO gp_mc_user (`username`, `uuid`, `aktiv`) VALUES ('"+event.getPlayer().getName()+"','"+event.getPlayer().getUniqueId()+"', 'true')");
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	Statement statement;
		try {
			statement = WebInterface.connection.createStatement();
			
	   	 	Date date= new java.util.Date();
		    statement.executeUpdate("UPDATE gp_mc_user SET `letzterlogout` = '"+(new Timestamp(date.getTime()))+"', `aktiv` = false WHERE `uuid` = '"+event.getPlayer().getUniqueId()+"'");

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
  
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event){
        Player p = event.getPlayer();
        if(event.getClickedBlock().getType() == Material.SIGN_POST)
        {    
            Sign s = (Sign) event.getClickedBlock().getState();
            
            if(s.getLine(0).equalsIgnoreCase("Test")){
            

            p.sendMessage(s.getLine(0));
            	
            /*
            ItemStack bricks = new ItemStack(Material.BRICK);
            bricks.setAmount(20);
            p.getInventory().addItem(bricks);*/
            }
        }
        
        /*
        if(p.getItemInHand() == Material.BLAZE_POWDER){

        }
        else if(p.getItemInHand() == Material.BLAZE_ROD){
            //Do whatever
        }*/
    }
}
