package com.github.colingrime.locale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Replacer {

	private final Map<String, String> replacements = new HashMap<>();

	public Replacer() {}

	public Replacer(String placeholder, int replacement) {
		this(placeholder, String.valueOf(replacement));
	}

	public Replacer(String placeholder, String replacement) {
		replacements.put(placeholder, replacement);
	}

	public Replacer add(String placeholder, int replacement) {
		return add(placeholder, String.valueOf(replacement));
	}

	public Replacer add(String placeholder, String replacement) {
		replacements.put(placeholder, replacement);
		return this;
	}

	public String replace(String message) {
		// replace the placeholders in the message
		for (Map.Entry<String, String> replacement : replacements.entrySet()) {
			message = message.replaceAll(replacement.getKey(), replacement.getValue());
		}

		return message;
	}

	public List<String> replace(List<String> messages) {
		// replace each message
		List<String> replacedList = new ArrayList<>();
		for (String message : messages) {
			replacedList.add(replace(message));
		}

		return replacedList;
	}
}
