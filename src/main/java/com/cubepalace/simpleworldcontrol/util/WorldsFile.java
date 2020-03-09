package com.cubepalace.simpleworldcontrol.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cubepalace.simpleworldcontrol.SimpleWorldControl;

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
		Map<UUID, List<Integer>> worlds = instance.getWorlds();
		Map<String, List<Integer>> worldStrings = new HashMap<String, List<Integer>>();
		for (UUID worldUUID : worlds.keySet()) {
			List<Integer> ints = worlds.get(worldUUID);
			worldStrings.put(worldUUID.toString(), ints);
		}
		config.set("worldSettings", worldStrings);
		save();
	}

	public Map<UUID, List<Integer>> loadToListWorlds() {
		Map<UUID, List<Integer>> worlds = new HashMap<UUID, List<Integer>>();
		try {
			for (String worldUUID : config.getConfigurationSection("worldSettings").getKeys(false)) {
				List<Integer> values = new ArrayList<Integer>();
				for (String value : config.getStringList("worldSettings." + worldUUID)) {
					values.add(Integer.valueOf(value));
				}
				worlds.put(UUID.fromString(worldUUID), values);
			}
		} catch (NullPointerException e) {

		}
		return worlds;
	}
}
