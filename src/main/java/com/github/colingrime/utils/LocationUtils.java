package com.github.colingrime.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

public final class LocationUtils {

	/**
	 * @param location any location
	 * @return String form of location
	 */
	public static String serializeLocation(@Nonnull Location location) {
		Objects.requireNonNull(location, "location");
		Objects.requireNonNull(location.getWorld(), "world");

		String world = location.getWorld().getName();
		String x = String.valueOf(location.getX());
		String y = String.valueOf(location.getY());
		String z = String.valueOf(location.getZ());
		String yaw = String.valueOf(location.getYaw());
		String pitch = String.valueOf(location.getPitch());

		return world + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
	}

	/**
	 * @param str String to check for Location
	 * @return Optional containing the deserialized Location if available
	 */
	public static Optional<Location> deserializeLocation(String str) {
		String[] args = str.split(":");
		if (args.length < 4) {
			return Optional.empty();
		}

		World world = Bukkit.getWorld(args[0]);
		if (world == null) {
			return Optional.empty();
		}

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
			return Optional.empty();
		}

		return Optional.of(new Location(world, x, y, z, yaw, pitch));
	}

	private LocationUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
