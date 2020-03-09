package com.cubepalace.simpleworldcontrol.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cubepalace.simpleworldcontrol.SimpleWorldControl;

public class ConfigFile {

	private File file;
	private FileConfiguration config;

	public ConfigFile(SimpleWorldControl instance, String fileName) {
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

	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
}
