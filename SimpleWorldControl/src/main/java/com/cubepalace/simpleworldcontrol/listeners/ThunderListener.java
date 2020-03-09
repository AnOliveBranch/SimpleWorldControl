package com.cubepalace.simpleworldcontrol.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;

import com.cubepalace.simpleworldcontrol.SimpleWorldControl;

public class ThunderListener implements Listener {

	private SimpleWorldControl instance;
	
	public ThunderListener(SimpleWorldControl instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onThunderChange(ThunderChangeEvent e) {
		if (instance.getWorlds().containsKey(e.getWorld().getUID())) {
			if (!(instance.getWorlds().get(e.getWorld().getUID()).get(1) == -1)) {
				if (instance.getWorlds().get(e.getWorld().getUID()).get(1) == 2) {
					if (!e.toThunderState())
						e.setCancelled(true);
				} else {
					if (e.toThunderState())
						e.setCancelled(true);
				}
			}
		}
	}
}
