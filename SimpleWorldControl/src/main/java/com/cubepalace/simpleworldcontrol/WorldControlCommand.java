package com.cubepalace.simpleworldcontrol;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WorldControlCommand implements CommandExecutor {

	
	private SimpleWorldControl instance;
	
	public WorldControlCommand(SimpleWorldControl instance) {
		this.instance = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("worldcontrol")) {
			if (!sender.hasPermission("worldcontrol.cmd")) {
				sender.sendMessage(ChatColor.RED + "No Permission");
				return true;
			}
			
			if (args.length == 0) {
				showHelp(sender);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("view")) {
				
			} else if (args[0].equalsIgnoreCase("set")) {
				
			} else if (args[0].equalsIgnoreCase("remove")) {
				
			} else if (args[0].equalsIgnoreCase("reload")) {
				
			} else {
				showHelp(sender);
			}
		}
		return false;
	}
	
	private void showHelp(CommandSender sender) {
		
	}
}
