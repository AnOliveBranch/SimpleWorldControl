package com.cubepalace.simpleworldcontrol;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener implements Listener {

	private SimpleWorldControl instance;
	
	public WeatherListener(SimpleWorldControl instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		
	}
}
