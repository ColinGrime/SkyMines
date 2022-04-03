package com.github.scilldev.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
		message = message.replaceAll("_", " ").replaceAll("-", " ");
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
}
