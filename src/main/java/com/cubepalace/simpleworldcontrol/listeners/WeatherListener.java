package com.cubepalace.simpleworldcontrol.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.cubepalace.simpleworldcontrol.SimpleWorldControl;

public class WeatherListener implements Listener {

	private SimpleWorldControl instance;
	
	public WeatherListener(SimpleWorldControl instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		if (instance.getWorlds().containsKey(e.getWorld().getUID())) {
			if (!(instance.getWorlds().get(e.getWorld().getUID()).get(1) == -1)) {
				if (instance.getWorlds().get(e.getWorld().getUID()).get(1) == 0) {
					if (e.toWeatherState())
						e.setCancelled(true);
				} else {
					if (!e.toWeatherState())
						e.setCancelled(true);
				}
			}
		}
	}
}
