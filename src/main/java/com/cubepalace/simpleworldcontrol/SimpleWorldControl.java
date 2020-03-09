package com.cubepalace.simpleworldcontrol;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.cubepalace.simpleworldcontrol.listeners.WeatherListener;
import com.cubepalace.simpleworldcontrol.util.ConfigFile;
import com.cubepalace.simpleworldcontrol.util.WorldsFile;

import net.md_5.bungee.api.ChatColor;

public class SimpleWorldControl extends JavaPlugin {

	private WorldsFile worldsFile;
	private ConfigFile config;
	private Map<UUID, List<Integer>> worlds;
	private boolean listsChanged;
	private final String noPermission = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("no-permission"));
	private final String noConsoleCommand = ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("no-console-command"));
	
	@Override
	public void onEnable() {
		setWorldOptions();
		register();
		setup();
		saveTimer();
		getLogger().info("SimpleWeatherControl has been enabled");
	}
	
	@Override
	public void onDisable() {
		if (listsChanged) {
			getLogger().info("Saving world settings to file...");
			worldsFile.updateWorldList();
			getLogger().info("Save complete");
			listsChanged = false;
		}
		getLogger().info("SimpleWeatherControl has been disabled");
	}
	
	private void register() {
		getCommand("worldcontrol").setExecutor(new WorldControlCommand(this));
		getServer().getPluginManager().registerEvents(new WeatherListener(this), this);
	}
	
	private void setup() {
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
		saveResource("config.yml", false);
		config = new ConfigFile(this, "config.yml");
		worldsFile = new WorldsFile(this, "worlds.yml");
		worlds = worldsFile.loadToListWorlds();
	}
	
	private void saveTimer() {
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				if (listsChanged) {
					getLogger().info("Saving world settings to file...");
					worldsFile.updateWorldList();
					getLogger().info("Save complete");
					listsChanged = false;
				}
			}
		}, 6000L, 6000L);
	}
	
	private void setWorldOptions() {
		for (UUID world : worlds.keySet()) {
			World worldO = getServer().getWorld(world);
			List<Integer> worldSettings = worlds.get(world);
			if (worldSettings.get(0) != -1)
				worldO.setTime(worldSettings.get(0));
			if (worldSettings.get(1) != -1) {
				if (worldSettings.get(1) == 0) {
					worldO.setThundering(false);
					worldO.setStorm(false);
				} else if (worldSettings.get(1) == 1) {
					worldO.setThundering(false);
					worldO.setStorm(true);
				} else {
					worldO.setThundering(true);
					worldO.setStorm(true);
				}
			}
		}
	}
	
	public Map<UUID, List<Integer>> getWorlds() {
		return worlds;
	}
	
	public void setWorldSettings(World world, List<Integer> settings) {
		worlds.put(world.getUID(), settings);
		listsChanged = true;
	}
	
	public void removeWorldSettings(World world) {
		worlds.remove(world.getUID());
		listsChanged = true;
	}
	
	public WorldsFile getWorldsFile() {
		return worldsFile;
	}
	
	public ConfigFile getCustomConfig() {
		return config;
	}
	
	public FileConfiguration getConfig() {
		return config.getConfig();
	}
	
	public String getNoPerm() {
		return noPermission;
	}
	
	public String getNoConsoleCommand() {
		return noConsoleCommand;
	}
}
