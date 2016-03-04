package com.gp.suite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;


public class ActionListener implements Listener { 
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event){
        Player p = event.getPlayer();
        
        if(event.getClickedBlock().getType() == Material.SIGN_POST)
        {    
            Sign s = (Sign) event.getClickedBlock().getState();
            
            if(s.getLine(0).equalsIgnoreCase("GP Real Estate")){
            	buyEstate(event.getClickedBlock().getLocation(), event.getPlayer());
            }
        } else
        if(event.getClickedBlock().getType() == Material.DIRT && p.getItemInHand().getType() == Material.STICK)
        {    
        	if(!checkIsEstate(event.getClickedBlock().getLocation())) 
        	if(setNewEstate(event.getClickedBlock())) 
                p.sendMessage("Grundstück angelegt");
        }
    }

	private boolean buyEstate(Location location, Player player) {
    	Statement statement;
		try {
			statement = GP2PPLugin.connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT id FROM gp_mc_user WHERE `uuid` = '"+player.getUniqueId()+"'");
			if (rs.next())
			{
			int playerid = rs.getInt("id");
			int en = statement.executeUpdate("UPDATE gp_mc_estate SET `playerid` = '"+ playerid +"' WHERE `x1` <= '"+location.getX()+"' AND `x2` >= '"+location.getX()+"' AND `z1` <= '"+location.getZ()+"' AND `z2` >= '"+location.getZ()+"'");
			if (en > 0) return true; else return false;
			} else return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean setNewEstate(Block block) {
    	Statement statement;
		try {
			statement = GP2PPLugin.connection.createStatement();
			
			World world = block.getWorld();
			
			int x1 = 0;
			int x2 = 0;
			int z1 = 0;
			int z2 = 0;
			
			while(new Location(world, block.getX() - x1, block.getY(), block.getZ()).getBlock().getType() == Material.DIRT)
			{
				x1++;
				if(x1 > 500) { x1 = -1; break; }
			}
			
			while(new Location(world, block.getX() + x2, block.getY(), block.getZ()).getBlock().getType() == Material.DIRT)
			{
				x2++;
				if(x2 > 500) { x2 = -1; break; }
			}
			
			while(new Location(world, block.getX() - z1, block.getY(), block.getZ()).getBlock().getType() == Material.DIRT)
			{
				z1++;
				if(z1 > 500) { z1 = -1; break; }
			}
			
			while(new Location(world, block.getX() + z1, block.getY(), block.getZ()).getBlock().getType() == Material.DIRT)
			{
				z2++;
				if(z2 > 500) { z2 = -1; break; }
			}
			if(x1 != -1 && x2 != -1 && z1 != -1 && z2 != -1)
			{
			new Location(world, x1, block.getY(), z1).getBlock().setType(Material.SIGN_POST);
			Sign s = (Sign) new Location(world, x1, block.getY(), z1).getBlock().getState();
			s.setLine(0, "GP Real Estate");
			s.setLine(1, x1 + "x" + x2 + "|" + z1 + "x" + z2);
			s.update();
			
	    	statement.executeUpdate("INSERT INTO gp_mc_estate (`x1`, `x2`, `z1`, `z2`) VALUES ('"+x1+"','"+x2+"','"+z1+"','"+z2+"')");
	    	
	    	BlockVector min = new BlockVector(x1, 0, z1);
	    	BlockVector max = new BlockVector(x2, 255, z2);
	    	ProtectedRegion region = new ProtectedCuboidRegion(x1 + "x" + x2 + "|" + z1 + "x" + z2, min, max);
	    	Collection<ProtectedRegion> regions = new HashSet<ProtectedRegion>();
	    	regions.add(region);
	    	WGBukkit.getRegionManager(world).setRegions(regions);
	    	
            return true;
			}
			else
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			
			return false;
		}
	}

	private boolean checkIsEstate(Location location) {
    	Statement statement;
		try {
			statement = GP2PPLugin.connection.createStatement();

			ResultSet rs = statement.executeQuery("SELECT id FROM gp_mc_estate WHERE `x1` <= '"+location.getX()+"' AND `x2` >= '"+location.getX()+"' AND `z1` <= '"+location.getZ()+"' AND `z2` >= '"+location.getZ()+"'");
			if (rs.next()) return true; else return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
