package com.cubepalace.simpleworldcontrol;

import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleWorldControl extends JavaPlugin {

	private WorldsFile worldsFile;
	private Map<World, List<Integer>> worlds;
	private boolean listsChanged;
	
	@Override
	public void onEnable() {
		register();
		setup();
		saveTimer();
		getLogger().info("SimpleWeatherControl has been enabled");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("SimpleWeatherControl has been disabled");
	}
	
	private void register() {
		getCommand("weathercontrol").setExecutor(new WorldControlCommand(this));
		getServer().getPluginManager().registerEvents(new WeatherListener(this), this);
	}
	
	private void setup() {
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
		worldsFile = new WorldsFile(this, "worlds.yml");
		worlds = worldsFile.loadToListWorlds();
	}
	
	private void saveTimer() {
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				if (listsChanged) {
					getLogger().info("Saving player options to file...");
					worldsFile.save();
					getLogger().info("Save complete");
					listsChanged = false;
				}
			}
		}, 6000L, 6000L);
	}
	
	public Map<World, List<Integer>> getWorlds() {
		return worlds;
	}
}
