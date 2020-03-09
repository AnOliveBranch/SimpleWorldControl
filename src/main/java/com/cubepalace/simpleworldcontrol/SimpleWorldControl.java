package com.cubepalace.simpleworldcontrol;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.cubepalace.simpleworldcontrol.listeners.WeatherListener;

public class SimpleWorldControl extends JavaPlugin {

	private WorldsFile worldsFile;
	private Map<UUID, List<Integer>> worlds;
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
}
