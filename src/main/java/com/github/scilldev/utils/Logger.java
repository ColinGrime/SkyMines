package com.github.scilldev.utils;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public final class Logger {

	private Logger() {}

	public static void log(String message) {
		Bukkit.getLogger().log(Level.INFO, "[TieredChat] " + message);
	}

	public static void severe(String message) {
		Bukkit.getLogger().log(Level.SEVERE, "[TieredChat] " + message);
	}
}
