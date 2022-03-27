package com.github.scilldev.skymines.manager;

import com.github.scilldev.skymines.SkyMine;
import com.github.scilldev.skymines.factory.DefaultSkyMineFactory;
import com.github.scilldev.skymines.factory.SkyMineFactory;
import com.github.scilldev.skymines.token.DefaultSkyMineToken;
import com.github.scilldev.skymines.token.SkyMineToken;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class SkyMineManager {

	private final SkyMineFactory factory = new DefaultSkyMineFactory();
	private final SkyMineToken token = new DefaultSkyMineToken();

	private final Map<UUID, List<SkyMine>> skyMines = new HashMap<>();

	public SkyMineToken getToken() {
		return token;
	}

	public void createSkyMine(Player player, Location location) {
		addSkyMine(player, factory.createSkyMine(player, location));
	}

	public List<SkyMine> getSkyMines(Player player) {
		return getSkyMines(player.getUniqueId());
	}

	public List<SkyMine> getSkyMines(UUID uuid) {
		return skyMines.getOrDefault(uuid, new ArrayList<>());
	}

	public Optional<SkyMine> getSkyMine(Player player, String id) {
		List<SkyMine> skyMines = getSkyMines(player);
		for (int i=0; i<skyMines.size(); i++) {
			if (String.valueOf(i + 1).equals(id)) {
				return Optional.of(skyMines.get(i));
			}
		}

		return Optional.empty();
	}

	public Map<UUID, List<SkyMine>> getSkyMines() {
		return skyMines;
	}

	public void addSkyMine(Player player, SkyMine skyMine) {
		addSkyMine(player.getUniqueId(), skyMine);
	}

	public void addSkyMine(UUID uuid, SkyMine skyMine) {
		List<SkyMine> skyMines = getSkyMines(uuid);
		skyMines.add(skyMine);

		this.skyMines.put(uuid, skyMines);
	}
}
