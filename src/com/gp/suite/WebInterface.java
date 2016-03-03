package com.gp.suite;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
public class WebInterface extends JavaPlugin {
	
	//DataBase vars.
    final String username= getConfig().getString("Username"); //Enter in your db username
    final String password= getConfig().getString("Password"); //Enter your password for the db
    final String url = getConfig().getString("URL"); //Enter URL w/db name

    //Connection vars
    static Connection connection; //This is the variable we will use to connect to database
    
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	 //getConfig().addDefault("youAreAwesome", true);
    	
    	
    	try { //We use a try catch to avoid errors, hopefully we don't get any.
            Class.forName("com.mysql.jdbc.Driver"); //this accesses Driver in jdbc.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            return;
        }
        try { //Another try catch to get any SQL errors (for example connections errors)
            connection = DriverManager.getConnection(url,username,password);
            //with the method getConnection() from DriverManager, we're trying to set
            //the connection's url, username, password to the variables we made earlier and
            //trying to get a connection at the same time. JDBC allows us to do this.
        } catch (SQLException e) { //catching errors)
            e.printStackTrace(); //prints out SQLException errors to the console (if any)
        }
    	
    	this.getCommand("test").setExecutor(new CommandTest());
    	getServer().getPluginManager().registerEvents(new MyListener(), this);
    }
    
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
        // envoke on disable.
        try { //using a try catch to catch connection errors (like wrong sql password...)
                if(connection!=null && !connection.isClosed()){ //checking if connection isn't null to
                //avoid recieving a nullpointer
                        connection.close(); //closing the connection field variable.
                }
        }catch(Exception e){
                        e.printStackTrace();
            
                }
    }
}
