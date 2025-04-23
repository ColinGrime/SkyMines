package me.colingrimes.skymines.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Utils {

	/**
	 * Deserializes the old {@link Location} format.
	 *
	 * @param locationStr the string that represents a location
	 * @return the deserialized location
	 */
	@Deprecated
	@Nullable
	public static Location deserializeLocation(@Nonnull String locationStr) {
		String[] args = locationStr.split(":");
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

	private Utils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
