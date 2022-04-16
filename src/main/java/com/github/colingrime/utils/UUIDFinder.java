package com.github.colingrime.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class UUIDFinder {

	private static final Map<String, UUID> uuidCache = new HashMap<>();

	private UUIDFinder() {}

	public static UUID fromName(String name) {
		if (uuidCache.containsKey(name)) {
			return uuidCache.get(name);
		}

		Player player = Bukkit.getPlayer(name);
		if (player != null) {
			return storeAndReturnUuid(name, player.getUniqueId());
		}

		// attempts to retrieve the uuid from the Mojang API
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()))) {
			JsonElement idElement = ((JsonObject) JsonParser.parseReader(in)).get("id");
			if (idElement != null) {
				return storeAndReturnUuid(name, getUuidFromString(idElement.toString()));
			}
		} catch (Exception ignored) {}

		return null;
	}

	private static UUID storeAndReturnUuid(String name, UUID uuid) {
		uuidCache.put(name, uuid);
		return uuid;
	}

	private static UUID getUuidFromString(String uuidString) {
		return UUID.fromString(uuidString.replaceAll("\"", "").replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
	}
}
