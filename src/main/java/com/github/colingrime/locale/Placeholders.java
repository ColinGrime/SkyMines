package com.github.colingrime.locale;

import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.structure.MineSize;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Function;

public enum Placeholders {

	LENGTH("%length%", o -> {
		if (o instanceof MineSize) {
			return String.valueOf(((MineSize) o).getLength());
		}
		return "%length%";
	}),

	HEIGHT("%height%", o -> {
		if (o instanceof MineSize) {
			return String.valueOf(((MineSize) o).getHeight());
		}
		return "%height%";
	}),

	WIDTH("%width%", o -> {
		if (o instanceof MineSize) {
			return String.valueOf(((MineSize) o).getWidth());
		}
		return "%width%";
	}),

	ID("%id%", o -> {
		if (o instanceof SkyMine) {
			return String.valueOf(((SkyMine) o).getId());
		}
		return "%id%";
	}),

	AMOUNT("%amount%", o -> {
		if (o instanceof ItemStack) {
			return String.valueOf(((ItemStack) o).getAmount());
		}
		return "%amount%";
	}),

	TOKEN("%token%", o -> {
		if (o instanceof ItemStack) {
			return ((ItemStack) o).getItemMeta().getDisplayName();
		}
		return "%token%";
	}),

	PLAYER("%player%", o -> {
		if (o instanceof Player) {
			return ((Player) o).getName();
		}
		return "%player%";
	});

	private final String placeholder;
	private final Function<Object, String> extractValue;

	Placeholders(String placeholder, Function<Object, String> extractValue) {
		this.placeholder = placeholder;
		this.extractValue = extractValue;
	}

	/**
	 * @param message message you want replaced
	 * @param objects objects you want to check for replacements
	 * @return new message with placeholders changed
	 */
	public static String replaceAll(String message, Object...objects) {
		for (Placeholders placeholder : Placeholders.values()) {
			for (Object object : objects) {
				message = message.replaceAll(placeholder.placeholder, placeholder.extractValue.apply(object));
			}
		}
		return message;
	}

	public static String replaceAll(Messages message, Object...objects) {
		return replaceAll(message.toString(), objects);
	}

	/**
	 * @param messages list of messages you want replaced
	 * @param objects objects you want to check for replacements
	 * @return new list of messages with placeholders changed
	 */
	public static List<String> replaceAll(List<String> messages, Object...objects) {
		for (int i=0; i<messages.size(); i++) {
			messages.set(i, replaceAll(messages.get(i), objects));
		}
		return messages;
	}
}
