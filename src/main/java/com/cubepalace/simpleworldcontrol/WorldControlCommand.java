package com.cubepalace.simpleworldcontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldControlCommand implements CommandExecutor {

	private SimpleWorldControl instance;

	public WorldControlCommand(SimpleWorldControl instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("worldcontrol")) {
			if (!sender.hasPermission("worldcontrol.cmd")) {
				sender.sendMessage(instance.getNoPerm());
				return true;
			}

			if (args.length == 0) {
				showHelp(sender);
				return true;
			}

			if (args[0].equalsIgnoreCase("reload")) {
				if (!sender.hasPermission("worldcontrol.relaod")) {
					sender.sendMessage(instance.getNoPerm());
					return true;
				}

				instance.getWorldsFile().reload();
				instance.getCustomConfig().reload();
				sender.sendMessage(ChatColor.GREEN + "SimpleWorldControl has been reloaded");
				return true;
			}

			if (args.length == 1) {
				showHelp(sender);
				return true;
			}

			if (args[0].equalsIgnoreCase("view")) {
				if (args.length > 1 && args[1].equalsIgnoreCase("here")) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(instance.getNoConsoleCommand());
						return true;
					}

					if (!sender.hasPermission("worldcontrol.view.here")) {
						sender.sendMessage(instance.getNoPerm());
						return true;
					}

					Player p = (Player) sender;

					sendSettings(sender, p.getWorld().getUID());

				} else if (args.length > 1 && args[1].equalsIgnoreCase("controlled")) {
					if (!sender.hasPermission("worldcontrol.view.controlled")) {
						sender.sendMessage(instance.getNoPerm());
						return true;
					}

					sender.sendMessage(ChatColor.GOLD + "--[--- World Control Settings ---]--");
					for (UUID world : instance.getWorlds().keySet())
						sendSettings(sender, world);

				} else if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
					if (!sender.hasPermission("worldcontrol.view.all")) {
						sender.sendMessage(instance.getNoPerm());
						return true;
					}

					sender.sendMessage(ChatColor.GOLD + "--[--- World Control Settings ---]--");
					for (World world : instance.getServer().getWorlds())
						sendSettings(sender, world.getUID());

				} else {
					showHelp(sender);
					return true;
				}

			} else if (args[0].equalsIgnoreCase("set")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(instance.getNoConsoleCommand());
					return true;
				}

				Player p = (Player) sender;
				List<Integer> worldSettings;
				
				if (args[1].equalsIgnoreCase("time")) {
					if (!sender.hasPermission("worldcontrol.set.time")) {
						sender.sendMessage(instance.getNoPerm());
						return true;
					}

					if (args.length < 3) {
						sender.sendMessage(ChatColor.RED + "Usage: /worldcontrol set time <time>");
						return true;
					}

					int time;

					try {
						time = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "Time must be a number, got \"" + args[2] + "\"");
						return true;
					}

					if (time < 0 || time > 24000) {
						sender.sendMessage(ChatColor.RED + "Time must be between 0 and 24000, got " + time);
						return true;
					}

					if (instance.getWorlds().keySet().contains(p.getWorld().getUID()))
						worldSettings = instance.getWorlds().get(p.getWorld().getUID());
					else {
						worldSettings = new ArrayList<Integer>();
						worldSettings.add(-1);
						worldSettings.add(-1);
					}

					worldSettings.set(0, time);
					p.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
					p.getWorld().setTime(time);
					instance.setWorldSettings(p.getWorld(), worldSettings);
					sender.sendMessage(
							ChatColor.GREEN + "Set time in world \"" + p.getWorld().getName() + "\" to " + time);

				} else if (args[1].equalsIgnoreCase("weather")) {
					if (!sender.hasPermission("worldcontrol.set.weather")) {
						sender.sendMessage(instance.getNoPerm());
						return true;
					}

					if (args.length < 3 || (!args[2].equalsIgnoreCase("sun") && !args[2].equalsIgnoreCase("rain")
							&& !args[2].equalsIgnoreCase("storm"))) {
						sender.sendMessage(
								ChatColor.RED + "Usage: /worldcontrol set weather <\"sun\"|\"rain\"|\"storm\">");
						return true;
					}

					if (instance.getWorlds().containsKey(p.getWorld().getUID()))
						worldSettings = instance.getWorlds().get(p.getWorld().getUID());
					else {
						worldSettings = new ArrayList<Integer>();
						worldSettings.add(-1);
						worldSettings.add(-1);
					}

					if (args[2].equalsIgnoreCase("sun")) {
						worldSettings.set(1, 0);
						p.getWorld().setThundering(false);
						p.getWorld().setStorm(false);
					} else if (args[2].equalsIgnoreCase("rain")) {
						worldSettings.set(1, 1);
						p.getWorld().setStorm(true);
						p.getWorld().setThundering(false);
					} else {
						worldSettings.set(1, 2);
						p.getWorld().setStorm(true);
						p.getWorld().setThundering(true);
					}

					instance.setWorldSettings(p.getWorld(), worldSettings);
					sender.sendMessage(
							ChatColor.GREEN + "Set the weather in \"" + p.getWorld().getName() + "\" to " + args[2]);
				} else {
					showHelp(sender);
					return true;
				}

			} else if (args[0].equalsIgnoreCase("remove")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(instance.getNoConsoleCommand());
					return true;
				}

				Player p = (Player) sender;

				if (args[1].equalsIgnoreCase("all")) {
					if (!sender.hasPermission("worldcontrol.remove.time")
							|| !sender.hasPermission("worldcontrol.remove.weather")) {
						sender.sendMessage(instance.getNoPerm());
						return true;
					}

					p.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
					instance.removeWorldSettings(p.getWorld());
					sender.sendMessage(ChatColor.GREEN + "Removed all controls for " + p.getWorld().getName());
					return true;
				} else if (args[1].equalsIgnoreCase("time")) {
					if (!sender.hasPermission("worldcontrol.remove.time")) {
						sender.sendMessage(instance.getNoPerm());
						return true;
					}

					
					
					if (!instance.getWorlds().containsKey(p.getWorld().getUID())
							|| instance.getWorlds().get(p.getWorld().getUID()).get(0) == -1) {
						sender.sendMessage(ChatColor.RED + p.getWorld().getName() + " is not being time controlled");
						return true;
					}

					if (instance.getWorlds().get(p.getWorld().getUID()).get(1) == -1) {
						instance.removeWorldSettings(p.getWorld());
					} else {
						List<Integer> worldSettings = instance.getWorlds().get(p.getWorld().getUID());
						worldSettings.set(-1, worldSettings.get(1));
						instance.setWorldSettings(p.getWorld(), worldSettings);
					}

					p.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
					sender.sendMessage(ChatColor.GREEN + "Removed time controls for \"" + p.getWorld().getName() + "\"");
				} else if (args[1].equalsIgnoreCase("weather")) {
					if (!sender.hasPermission("worldcontrol.remove.weather")) {
						sender.sendMessage(instance.getNoPerm());
						return true;
					}

					if (!instance.getWorlds().containsKey(p.getWorld().getUID())
							|| instance.getWorlds().get(p.getWorld().getUID()).get(1) == -1) {
						sender.sendMessage(ChatColor.RED + p.getWorld().getName() + " is not being weather controlled");
						return true;
					}

					if (instance.getWorlds().get(p.getWorld().getUID()).get(0) == -1) {
						instance.removeWorldSettings(p.getWorld());
					} else {
						List<Integer> worldSettings = instance.getWorlds().get(p.getWorld().getUID());
						worldSettings.set(worldSettings.get(0), -1);
						instance.setWorldSettings(p.getWorld(), worldSettings);
					}
				} else {
					showHelp(sender);
				}
			} else {
				showHelp(sender);
			}
			return true;
		}
		return false;
	}

	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "--[--- SimpleWorldControl ---]--");
		if (sender.hasPermission("worldcontrol.view.here"))
			sender.sendMessage(ChatColor.GOLD + "/worldcontrol view here: " + ChatColor.AQUA
					+ "Lists the world control options for your current world");
		if (sender.hasPermission("worldcontrol.view.controlled"))
			sender.sendMessage(ChatColor.GOLD + "/worldcontrol view controlled: " + ChatColor.AQUA
					+ "Lists the world control options for all controlled worlds");
		if (sender.hasPermission("worldcontrol.view.all"))
			sender.sendMessage(ChatColor.GOLD + "/worldcontrol view all: " + ChatColor.AQUA
					+ "Lists the world control options for all server worlds");
		if (sender.hasPermission("worldcontrol.set.time"))
			sender.sendMessage(ChatColor.GOLD + "/worldcontrol set time <time>: " + ChatColor.AQUA
					+ "Permanently sets the time in the current world");
		if (sender.hasPermission("worldcontrol.set.weather"))
			sender.sendMessage(ChatColor.GOLD + "/worldcontrol set weather <\"sun\"|\"rain\"|\"storm\">: "
					+ ChatColor.AQUA + "Permanently sets the weather in the current world");
		if (sender.hasPermission("worldcontrol.remove.time") && sender.hasPermission("worldcontrol.remove.weather"))
			sender.sendMessage(ChatColor.GOLD + "/worldcontrol remove all: " + ChatColor.AQUA
					+ "Removes all control in the current world");
		if (sender.hasPermission("worldcontrol.remove.time"))
			sender.sendMessage(ChatColor.GOLD + "/worldcontrol remove time: " + ChatColor.AQUA
					+ "Removes time control in the current world");
		if (sender.hasPermission("worldcontrol.remove.weather"))
			sender.sendMessage(ChatColor.GOLD + "/worldcontrol remove weather: " + ChatColor.AQUA
					+ "Removes weather control in the current world");
		if (sender.hasPermission("worldcontrol.reload"))
			sender.sendMessage(ChatColor.GOLD + "/worldcontrol reload: " + ChatColor.AQUA + "Reloads the plugin");
	}

	private void sendSettings(CommandSender sender, UUID world) {
		sender.sendMessage(ChatColor.GOLD + "Settings for " + instance.getServer().getWorld(world).getName());
		if (!instance.getWorlds().containsKey(world)) {
			sender.sendMessage(ChatColor.GOLD + "\tTime control: " + ChatColor.RED + "No");
			sender.sendMessage(ChatColor.GOLD + "\tWeather control: " + ChatColor.RED + "No");
		} else {
			List<Integer> worldSettings = instance.getWorlds().get(world);
			sender.sendMessage(ChatColor.GOLD + "  Time control: " + (worldSettings.get(0) == -1 ? ChatColor.RED + "No"
					: ChatColor.GREEN + "" + worldSettings.get(0)));
			sender.sendMessage(ChatColor.GOLD + "  Weather control: " + (worldSettings.get(1) == -1
					? ChatColor.RED + "No"
					: worldSettings.get(1) == 0 ? ChatColor.GREEN + "Sunny"
							: worldSettings.get(1) == 1 ? ChatColor.GREEN + "Rainy" : ChatColor.GREEN + "Stormy"));
		}
	}
}