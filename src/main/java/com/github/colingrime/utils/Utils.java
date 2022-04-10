package com.github.colingrime.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Utils {

	private Utils() {}

	/**
	 * @param message any message
	 * @return color-coded message
	 */
	public static String color(String message) {
		if (message == null) {
			return "";
		}

		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * @param messages list of messages
	 * @return color-coded list of messages
	 */
	public static List<String> color(List<String> messages) {
		if (messages.isEmpty()) {
			return new ArrayList<>();
		}

		return messages.stream().map(Utils::color).collect(Collectors.toList());
	}

	/**
	 * @param message any message
	 * @return new message with spaces, dashes, and colons removed
	 */
	public static String strip(String message) {
		return message.replaceAll("\\s+", "")
				.replaceAll("-", "")
				.replaceAll(":", "");
	}

	/**
	 * @param message any message
	 * @return new message that is formatted
	 */
	public static String format(String message) {
		message = message.replaceAll("_", " ").replaceAll("-", " ").replaceAll("(.)([A-Z])", "$1 $2");
		return WordUtils.capitalizeFully(message);
	}

	/**
	 * @param message any message
	 * @param command any command
	 * @return TextComponent of a command message
	 */
	public static TextComponent command(String message, String command) {
		TextComponent commandMessage = new TextComponent(TextComponent.fromLegacyText(color(message)));
		commandMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		return commandMessage;
	}

	/**
	 * Removes a single item from the player's hand.
	 * @param player any player
	 */
	public static void removeOneItemFromHand(Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if (item.getType() == Material.AIR) {
			return;
		}

		if (item.getAmount() > 1) {
			item.setAmount(item.getAmount() - 1);
		} else {
			player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
		}
	}

	/**
	 * @param location any location
	 * @return string form of location
	 */
	public static String parseLocation(Location location) {
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
	public static Location parseLocation(String text) {
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
