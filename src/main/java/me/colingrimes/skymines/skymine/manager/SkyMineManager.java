package me.colingrimes.skymines.skymine.manager;

import me.colingrimes.midnight.util.io.Logger;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.factory.SkyMineFactory;
import me.colingrimes.skymines.skymine.structure.MineSize;
import me.colingrimes.skymines.skymine.token.SkyMineToken;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

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

	@Nonnull
	public SkyMineToken getToken() {
		return token;
	}

	/**
	 * Creates a skymine if there's space available.
	 *
	 * @param player any player
	 * @param location location of the skymine
	 * @param size size of the skymine
	 * @param upgrades upgrades of the skymine
	 * @return true if the skymine was successfully created
	 */
	public boolean createSkyMine(@Nonnull Player player, @Nonnull Location location, @Nonnull MineSize size, @Nonnull SkyMineUpgrades upgrades, @Nonnull Material borderType) {
		Optional<SkyMine> skyMine = factory.createSkyMine(player, location, size, upgrades, borderType);
		if (skyMine.isEmpty()) {
			return false;
		}

		addSkyMine(player, skyMine.get());
		skyMine.get().save();
		return true;
	}

	/**
	 * @return all skymines
	 */
	@Nonnull
	public List<SkyMine> getSkyMines() {
		List<SkyMine> skyMines = new ArrayList<>();
		for (List<SkyMine> skyMine : this.skyMines.values()) {
			skyMines.addAll(skyMine);
		}

		return skyMines;
	}

	/**
	 * @param player any player
	 * @return list of skymines the player owns
	 */
	@Nonnull
	public List<SkyMine> getSkyMines(@Nonnull Player player) {
		return getSkyMines(player.getUniqueId());
	}

	/**
	 * @param uuid uuid of player
	 * @return list of skymines the uuid owns
	 */
	@Nonnull
	public List<SkyMine> getSkyMines(@Nonnull UUID uuid) {
		return skyMines.getOrDefault(uuid, new ArrayList<>());
	}

	/**
	 * @param player any player
	 * @param id id number of the skymine
	 * @return Optional skymine if found, or an empty Optional
	 */
	@Nonnull
	public Optional<SkyMine> getSkyMine(@Nonnull Player player, @Nonnull String id) {
		return getSkyMine(player.getUniqueId(), id);
	}

	/**
	 * @param uuid any uuid
	 * @param id id number of the skymine
	 * @return Optional skymine if found, or an empty Optional
	 */
	@Nonnull
	public Optional<SkyMine> getSkyMine(@Nonnull UUID uuid, String id) {
		if (id.matches("\\d+")) {
			return getSkyMine(uuid, Integer.parseInt(id));
		}
		return Optional.empty();
	}

	/**
	 * @param uuid any uuid
	 * @param id id number of the skymine
	 * @return Optional skymine if found, or an empty Optional
	 */
	@Nonnull
	protected Optional<SkyMine> getSkyMine(@Nonnull UUID uuid, int id) {
		List<SkyMine> skyMines = getSkyMines(uuid);
		for (int i=0; i<skyMines.size(); i++) {
			if (id == i + 1) {
				return Optional.of(skyMines.get(i));
			}
		}

		return Optional.empty();
	}

	/**
	 * @param player any player
	 * @param skyMine created skymine
	 */
	public void addSkyMine(@Nonnull Player player, @Nonnull SkyMine skyMine) {
		addSkyMine(player.getUniqueId(), skyMine);
	}

	/**
	 * @param uuid any uuid of player
	 * @param skyMine created skymine
	 */
	public void addSkyMine(@Nonnull UUID uuid, @Nonnull SkyMine skyMine) {
		List<SkyMine> skyMines = getSkyMines(uuid);
		skyMines.add(skyMine);
		this.skyMines.put(uuid, skyMines);
	}

	/**
	 * Removes the SkyMine and deletes it from storage.
	 *
	 * @param player any player
	 * @param skyMine removed skymine
	 */
	public void removeSkyMine(@Nonnull Player player, @Nonnull SkyMine skyMine) {
		removeSkyMine(player.getUniqueId(), skyMine);
	}

	/**
	 * @param uuid any uuid of player
	 * @param skyMine removed skymine
	 */
	public void removeSkyMine(@Nonnull UUID uuid, @Nonnull SkyMine skyMine) {
		List<SkyMine> skyMines = getSkyMines(uuid);
		skyMines.remove(skyMine);
		this.skyMines.put(uuid, skyMines);

		try {
			plugin.getCooldownManager().getSkyMineCooldown().cancel(skyMine);
			plugin.getStorage().delete(skyMine);
		} catch (Exception e) {
			Logger.severe("SkyMine has failed to delete. Please report this to the developer:", e);
		}
	}
}
