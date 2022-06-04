package com.github.colingrime.utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class StringUtils {

	private static final Pattern specialChars = Pattern.compile("\\s+|:+|-+|_+");
	private static final Pattern spaceBeforeCapitals = Pattern.compile("(.)([A-Z])");

	/**
	 * @param str String to be colored
	 * @return color-coded message
	 */
	public static String color(String str) {
		if (str == null) return "";
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	/**
	 * @param strList list of Strings
	 * @return color-coded list of messages
	 */
	public static List<String> color(List<String> strList) {
		if (strList.isEmpty()) return new ArrayList<>();
		return strList.stream().map(StringUtils::color).collect(Collectors.toList());
	}

	/**
	 * @param str String to be stripped
	 * @return new String with spaces, colons, dashes, and underscores removed
	 */
	public static String strip(String str) {
		return replaceSpecialCharacters(str, "");
	}

	/**
	 * Formats a String object.
	 *
	 * In doing so, the following actions are performed:
	 * - Replaces all colons, dashes, and underscores with spaces.
	 * - Places a space before all capital letters.
	 * - Capitalizes the first letter of each word.
	 *
	 * @param str String to be formatted
	 * @return new String that is formatted
	 */
	public static String format(String str) {
		str = replaceSpecialCharacters(str, " ");
		str = spaceBeforeCapitals.matcher(str).replaceAll("$1 $2");
		return WordUtils.capitalizeFully(str);
	}

	/**
	 * Unformats a String object.
	 * In doing so, all capital letters become lowercased with a colon behind them.
	 *
	 * @param str String to be unformatted
	 * @return new String that is unformatted
	 */
	public static String unformat(String str) {
		StringBuilder builder = new StringBuilder(str.substring(0, 1).toLowerCase());
		for (char c : str.substring(1).toCharArray()) {
			if (Character.isUpperCase(c)) {
				builder.append("-").append(Character.toLowerCase(c));
			} else {
				builder.append(c);
			}
		}

		return builder.toString();
	}

	/**
	 * Replaces special characters (spaces, colons, dashes, underscores) with a new character.
	 *
	 * @param str String to be replaced
	 * @param replacement String to replace special chars to
	 * @return replaced String
	 */
	private static String replaceSpecialCharacters(String str, String replacement) {
		if (str == null) return "";
		return specialChars.matcher(str).replaceAll(replacement);
	}

	private StringUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
