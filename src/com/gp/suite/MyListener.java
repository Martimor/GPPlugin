package com.gp.suite;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MyListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
    	//Bukkit.broadcastMessage("Welcome to the server!");
    	event.setJoinMessage("Welcome, " + event.getPlayer().getName() + "!");
    }
}
