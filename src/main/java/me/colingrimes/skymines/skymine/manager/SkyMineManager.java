package me.colingrimes.skymines.skymine.manager;

import me.colingrimes.midnight.util.io.Logger;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.factory.SkyMineFactory;
import me.colingrimes.skymines.skymine.structure.MineSize;
import me.colingrimes.skymines.skymine.token.SkyMineToken;
import me.colingrimes.skymines.skymine.upgrade.SkyMineUpgrades;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Manages the instances of {@link SkyMine}'s that players own.
 * Allows you to create skymines, get all skymines, and get/add/remove skymines of a specific player or UUID.
 */
public class SkyMineManager {

	private final Map<UUID, List<SkyMine>> skyMines = new HashMap<>();
	private final SkyMines plugin;
	private final SkyMineFactory factory;
	private final SkyMineToken token;

	public SkyMineManager(@Nonnull SkyMines plugin, @Nonnull SkyMineFactory factory, @Nonnull SkyMineToken token) {
		this.plugin = plugin;
		this.factory = factory;
		this.token = token;
	}

	/**
	 * Gets the skymine token provider.
	 * This is used to provide the token form of skymines.
	 *
	 * @return the skymine token provider
	 */
	@Nonnull
	public SkyMineToken getToken() {
		return token;
	}

	/**
	 * Creates a skymine if there's space available.
	 *
	 * @param player the player
	 * @param location location of the skymine
	 * @param size size of the skymine
	 * @param borderType the material used to create the border of the skymine
	 * @param upgrades upgrades of the skymine
	 * @return true if the skymine was successfully created
	 */
	public boolean createSkyMine(@Nonnull Player player, @Nonnull Location location, @Nonnull MineSize size, @Nonnull Material borderType, @Nonnull SkyMineUpgrades upgrades) {
		Optional<SkyMine> skyMine = factory.createSkyMine(player, location, size, borderType, upgrades);
		if (skyMine.isEmpty()) {
			return false;
		}

		addSkyMine(player, skyMine.get());
		skyMine.get().save();
		return true;
	}

	/**
	 * Gets a list of all loaded skymines.
	 *
	 * @return all loaded skymines
	 */
	@Nonnull
	public List<SkyMine> getSkyMines() {
		return skyMines.values().stream().flatMap(List::stream).toList();
	}

	/**
	 * Gets a list of all skymines owned by the specified player.
	 *
	 * @param player the player
	 * @return list of skymines the player owns
	 */
	@Nonnull
	public List<SkyMine> getSkyMines(@Nonnull Player player) {
		return getSkyMines(player.getUniqueId());
	}

	/**
	 * Gets a list of all skymines owned by the specified UUID.
	 *
	 * @param uuid uuid of player
	 * @return list of skymines the uuid owns
	 */
	@Nonnull
	public List<SkyMine> getSkyMines(@Nonnull UUID uuid) {
		return skyMines.getOrDefault(uuid, new ArrayList<>());
	}

	/**
	 * Attempts to get a specific skymine of a player by ID.
	 *
	 * @param player the player
	 * @param id the ID number of the skymine (>= 1)
	 * @return skymine if one exists
	 */
	@Nonnull
	public Optional<SkyMine> getSkyMine(@Nonnull Player player, @Nonnull String id) {
		return getSkyMine(player.getUniqueId(), id);
	}

	/**
	 * Attempts to get a specific skymine of a UUID by ID.
	 *
	 * @param uuid uuid of player
	 * @param id the ID number of the skymine (>= 1)
	 * @return skymine if one exists
	 */
	@Nonnull
	public Optional<SkyMine> getSkyMine(@Nonnull UUID uuid, @Nonnull String id) {
		if (!Types.isInteger(id)) {
			return Optional.empty();
		}

		List<SkyMine> skyMines = getSkyMines(uuid);
		int integerId = Integer.parseInt(id);
		for (int i=0; i<skyMines.size(); i++) {
			if (integerId == i + 1) {
				return Optional.of(skyMines.get(i));
			}
		}

		return Optional.empty();
	}

	/**
	 * Adds the specified skymine to the player's skymine list.
	 *
	 * @param player the player
	 * @param skyMine the skymine to add
	 */
	public void addSkyMine(@Nonnull Player player, @Nonnull SkyMine skyMine) {
		addSkyMine(player.getUniqueId(), skyMine);
	}

	/**
	 * Adds the specified skymine to the uuid's skymine list.
	 *
	 * @param uuid the uuid of player
	 * @param skyMine the skymine to add
	 */
	public void addSkyMine(@Nonnull UUID uuid, @Nonnull SkyMine skyMine) {
		skyMines.computeIfAbsent(uuid, __ -> new ArrayList<>()).add(skyMine);
	}

	/**
	 * Removes the specified skymine from the player and deletes it from storage.
	 *
	 * @param player the player
	 * @param skyMine the skymine to remove
	 */
	public void removeSkyMine(@Nonnull Player player, @Nonnull SkyMine skyMine) {
		removeSkyMine(player.getUniqueId(), skyMine);
	}

	/**
	 * Removes the specified skymine from the UUID and deletes it from storage.
	 *
	 * @param uuid the uuid of player
	 * @param skyMine the skymine to remove
	 */
	public void removeSkyMine(@Nonnull UUID uuid, @Nonnull SkyMine skyMine) {
		try {
			getSkyMines(uuid).remove(skyMine);
			plugin.getCooldownManager().getSkyMineCooldown().cancel(skyMine);
			plugin.getStorage().delete(skyMine);
		} catch (Exception e) {
			Logger.severe("SkyMine has failed to delete. Please report this to the developer:", e);
		}
	}
}
