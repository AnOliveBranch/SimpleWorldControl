package com.cubepalace.simpleworldcontrol;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class WorldsFile {

	private SimpleWorldControl instance;
	private File file;
	private FileConfiguration config;
	
	public WorldsFile(SimpleWorldControl instance, String fileName) {
		this.instance = instance;
		file = new File(instance.getDataFolder(), fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
		save();
	}
	
	public void updateWorldList() {
		Map<World, List<Integer>> worlds = instance.getWorlds();
		Map<String, List<Integer>> worldStrings = new HashMap<String, List<Integer>>();
		for (World world : worlds.keySet()) {
			List<Integer> ints = worlds.get(world);
			worldStrings.put(world.getName(), ints);
		}
		config.set("worldSettings", worldStrings);
		save();
	}
	
	public Map<World, List<Integer>> loadToListWorlds() {
		Map<World, List<Integer>> worlds = new HashMap<World, List<Integer>>();
		
		for (String worldName : config.getStringList("worldSettings")) {
			
		}
		
		return worlds;
	}
}
