package com.github.colingrime.listeners;

import com.github.colingrime.SkyMines;
import org.bukkit.event.Listener;

class AbstractListener implements Listener {

	final SkyMines plugin;

	AbstractListener(SkyMines plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
}
