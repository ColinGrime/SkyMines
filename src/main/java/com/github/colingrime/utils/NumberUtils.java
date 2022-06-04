package com.github.colingrime.utils;

import java.util.regex.Pattern;

public final class NumberUtils {

	private final static Pattern doubleRegex = Pattern.compile("\\d+(\\.\\d+)?");

	/**
	 * @param str any String
	 * @return true if the String is a digit
	 */
	public static boolean isDigit(String str) {
		if (str == null) return false;
		return str.chars().allMatch(Character::isDigit);
	}

	/**
	 * @param str any String
	 * @return true if the String is a double
	 */
	public static boolean isDouble(String str) {
		if (str == null) return false;
		return doubleRegex.matcher(str).matches();
	}

	public static int parseNumber(String input, int def) {
		return isDigit(input) ? Integer.parseInt(input) : def;
	}

	public static double parseNumber(String input, double def) {
		return isDouble(input) ? Double.parseDouble(input) : def;
	}

	private NumberUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
