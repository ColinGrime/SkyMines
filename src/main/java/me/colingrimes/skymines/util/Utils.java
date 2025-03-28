package me.colingrimes.skymines.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

// TODO refactor this (need to setup some sort of Gson system for Storage to better handle this)
public final class Utils {

	private Utils() {}

	/**
	 * @param location any location
	 * @return string form of location
	 */
	public static String parseLocation(@Nonnull Location location) {
		String world = location.getWorld().getName();
		String x = String.valueOf(location.getX());
		String y = String.valueOf(location.getY());
		String z = String.valueOf(location.getZ());
		String yaw = String.valueOf(location.getYaw());
		String pitch = String.valueOf(location.getPitch());
		return putBreakers(world, x, y, z, yaw, pitch);
	}

	/**
	 * @param text any text
	 * @return location form of string
	 */
	public static Location parseLocation(@Nonnull String text) {
		String[] args = text.split(":");
		if (args.length < 4) {
			return null;
		}

		World world = Bukkit.getWorld(args[0]);
		double x, y, z;
		float yaw = 0, pitch = 0;

		try {
			x = Double.parseDouble(args[1]);
			y = Double.parseDouble(args[2]);
			z = Double.parseDouble(args[3]);

			if (args.length == 6) {
				yaw = Float.parseFloat(args[4]);
				pitch = Float.parseFloat(args[5]);
			}
		} catch (NumberFormatException ex) {
			return null;
		}

		return new Location(world, x, y, z, yaw, pitch);
	}

	/**
	 * @param text any amount of text
	 * @return the text wrapped in a single string with colons between them
	 */
	private static String putBreakers(String...text) {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<text.length; i++) {
			builder.append(text[i]);

			// if it's not the last string
			if (i != text.length - 1) {
				builder.append(":");
			}
		}

		return builder.toString();
	}
}
