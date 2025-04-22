package me.colingrimes.skymines.manager;

import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.player.PlayerSettings;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

	private final Map<UUID, PlayerSettings> playerSettings = new HashMap<>();
	private final SkyMines plugin;

	public PlayerManager(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
	}

	/**
	 * Gets the {@link PlayerSettings} of the specified player.
	 *
	 * @param player the player
	 * @return the player's settings
	 */
	@Nonnull
	public PlayerSettings getSettings(@Nonnull Player player) {
		return getSettings(player.getUniqueId());
	}

	/**
	 * Gets the {@link PlayerSettings} of the specified UUID.
	 *
	 * @param uuid the uuid of the player
	 * @return the settings of the uuid
	 */
	@Nonnull
	public PlayerSettings getSettings(@Nonnull UUID uuid) {
		return playerSettings.computeIfAbsent(uuid, __ -> new PlayerSettings(plugin, uuid));
	}

	/**
	 * Adds the UUID-settings pair to the map.
	 *
	 * @param uuid the uuid of the player
	 * @param settings the player settings
	 */
	public void setSettings(@Nonnull UUID uuid, @Nonnull PlayerSettings settings) {
		playerSettings.put(uuid, settings);
	}

	/**
	 * Checks if the specified UUID has a loaded {@link PlayerSettings}.
	 *
	 * @param uuid the uuid to check
	 * @return true if the uuid has settings
	 */
	public boolean contains(@Nonnull UUID uuid) {
		return playerSettings.containsKey(uuid);
	}
}
