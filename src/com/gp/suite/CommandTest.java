package com.gp.suite;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandTest implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            ItemStack diamond = new ItemStack(Material.DIAMOND);
            ItemStack bricks = new ItemStack(Material.BRICK);
            bricks.setAmount(20);
            player.getInventory().addItem(bricks, diamond);
        }
        return true;
    }
}