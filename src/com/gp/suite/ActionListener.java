package com.gp.suite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;


public class ActionListener implements Listener { 
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event){
        Player p = event.getPlayer();
        
        if(event.getClickedBlock() != null)
        {
	        if(event.getClickedBlock().getType() == Material.SIGN_POST)
	        {    
	            Sign s = (Sign) event.getClickedBlock().getState();
	            
	            if(s.getLine(0).equalsIgnoreCase("GP Real Estate")){
	            	buyEstate(event.getClickedBlock().getLocation(), event.getPlayer());
	            }
	        } else
	        if(event.getClickedBlock().getType() == Material.GRASS && p.getItemInHand().getType() == Material.STICK && p.isOp() == true)
	        {    
	        	if(!checkIsEstate(event.getClickedBlock().getLocation())) 
	        	if(setNewEstate(event.getClickedBlock())) 
	                p.sendMessage("Grundstück angelegt");
	        }
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
			
			int x1 = 1;
			int x2 = 1;
			int z1 = 1;
			int z2 = 1;
			
			while(new Location(world, block.getX() - x1, block.getY(), block.getZ()).getBlock().getType() == Material.GRASS)
			{
				x1++;
				if(x1 > 500) { x1 = -1; break; }
			}
			
			while(new Location(world, block.getX() + x2, block.getY(), block.getZ()).getBlock().getType() == Material.GRASS)
			{
				x2++;
				if(x2 > 500) { x2 = -1; break; }
			}
			
			while(new Location(world, block.getX(), block.getY(), block.getZ() - z1).getBlock().getType() == Material.GRASS)
			{
				z1++;
				if(z1 > 500) { z1 = -1; break; }
			}
			
			while(new Location(world, block.getX(), block.getY(), block.getZ() + z2).getBlock().getType() == Material.GRASS)
			{
				z2++;
				if(z2 > 500) { z2 = -1; break; }
			}
			
			if(x1 != -1 && x2 != -1 && z1 != -1 && z2 != -1)
			{		    	
			x1 = block.getX() - x1 + 1;
			x2 = block.getX() + x2 - 1;

			z1 = block.getZ() - z1 + 1;
			z2 = block.getZ() + z2 - 1;
			
	    	WorldGuardPlugin wg = WGBukkit.getPlugin();
	    	
	    	RegionContainer container = wg.getRegionContainer();
	    	RegionManager regions = container.get(world);
	    	int numregions = regions.getRegions().size();

			new Location(world, x1, block.getY(), z1 - 1).getBlock().setType(Material.SIGN_POST);
			Sign s = (Sign) new Location(world, x1, block.getY(), z1 - 1).getBlock().getState();
			((org.bukkit.material.Sign)s.getData()).setFacingDirection(BlockFace.NORTH);
			s.setLine(0, "GP Real Estate");
			s.setLine(1, "GS" + numregions);
			s.update();

			
	    	statement.executeUpdate("INSERT INTO gp_mc_estate (`name`, `x1`, `x2`, `z1`, `z2`) VALUES ('" + "GS" + numregions + "', '"+x1+"','"+x2+"','"+z1+"','"+z2+"')");
	    	
	    	BlockVector min = new BlockVector(x1, 0, z1);
	    	BlockVector max = new BlockVector(x2, 255, z2);
	    	ProtectedRegion region = new ProtectedCuboidRegion("GS" + numregions, min, max);    
	    	

	    	regions.addRegion(region);
			
	    	try {
				regions.saveChanges();
				regions.save();
			} catch (StorageException e) {
				e.printStackTrace();
				return false;
			}
	    	
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
