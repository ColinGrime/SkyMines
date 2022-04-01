package com.github.scilldev.skymines.manager;

import com.github.scilldev.SkyMines;
import com.github.scilldev.skymines.SkyMine;
import com.github.scilldev.skymines.factory.DefaultSkyMineFactory;
import com.github.scilldev.skymines.factory.SkyMineFactory;
import com.github.scilldev.skymines.structure.MineSize;
import com.github.scilldev.skymines.token.DefaultSkyMineToken;
import com.github.scilldev.skymines.token.SkyMineToken;
import com.github.scilldev.skymines.upgrades.SkyMineUpgrades;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class SkyMineManager {

	private final SkyMineFactory factory;
	private final SkyMineToken token;

	private final Map<UUID, List<SkyMine>> skyMines = new HashMap<>();

	public SkyMineManager(SkyMines plugin) {
		this.factory = new DefaultSkyMineFactory(plugin);
		this.token = new DefaultSkyMineToken(plugin);
	}

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
	public boolean createSkyMine(Player player, Location location, MineSize size, SkyMineUpgrades upgrades) {
		Optional<SkyMine> skyMine = factory.createSkyMine(player, location, size, upgrades);
		if (!skyMine.isPresent()) {
			return false;
		}

		addSkyMine(player, skyMine.get());
		return true;
	}

	/**
	 * @param player any player
	 * @return list of skymines the player owns
	 */
	public List<SkyMine> getSkyMines(Player player) {
		return getSkyMines(player.getUniqueId());
	}

	/**
	 * @param uuid uuid of player
	 * @return list of skymines the uuid owns
	 */
	public List<SkyMine> getSkyMines(UUID uuid) {
		return skyMines.getOrDefault(uuid, new ArrayList<>());
	}

	/**
	 * @param player any player
	 * @param id id number of the skymine
	 * @return Optional skymine if found, or an empty Optional
	 */
	public Optional<SkyMine> getSkyMine(Player player, String id) {
		if (id.matches("\\d+")) {
			return getSkyMine(player, Integer.parseInt(id));
		}
		return Optional.empty();
	}

	/**
	 * @param player any player
	 * @param id id number of the skymine
	 * @return Optional skymine if found, or an empty Optional
	 */
	public Optional<SkyMine> getSkyMine(Player player, int id) {
		List<SkyMine> skyMines = getSkyMines(player);
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
	public void addSkyMine(Player player, SkyMine skyMine) {
		addSkyMine(player.getUniqueId(), skyMine);
	}

	/**
	 * @param uuid any uuid of player
	 * @param skyMine created skymine
	 */
	public void addSkyMine(UUID uuid, SkyMine skyMine) {
		List<SkyMine> skyMines = getSkyMines(uuid);
		skyMines.add(skyMine);

		this.skyMines.put(uuid, skyMines);
	}
}
