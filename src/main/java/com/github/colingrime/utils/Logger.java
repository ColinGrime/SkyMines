package com.github.colingrime.utils;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.logging.Level;

public final class Logger {

	public static void log(@Nonnull String message) {
		Objects.requireNonNull(message);
		Bukkit.getLogger().log(Level.INFO, "[SkyMines] " + message);
	}

	public static void severe(@Nonnull String message) {
		Objects.requireNonNull(message);
		Bukkit.getLogger().log(Level.SEVERE, "[SkyMines] " + message);
	}

	public static void warn(@Nonnull String message) {
		Objects.requireNonNull(message);
		Bukkit.getLogger().log(Level.WARNING, "[SkyMines] " + message);
	}

	private Logger() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
