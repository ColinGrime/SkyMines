package com.github.colingrime.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

// TODO refactor into a more descriptive utility class
public final class Utils {

	/**
	 * @param time any time
	 * @return formatted String representing the time
	 */
	public static String formatTime(double time) {
		int minutes = (int) time / 60;
		int seconds = (int) time - (minutes * 60);
		return String.format("%d:%02d", minutes, seconds);
	}

	/**
	 * @param message any message
	 * @param command any command
	 * @return TextComponent of a command message
	 */
	public static TextComponent command(String message, String command) {
		TextComponent commandMessage = new TextComponent(TextComponent.fromLegacyText(StringUtils.color(message)));
		commandMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		return commandMessage;
	}

	private Utils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
