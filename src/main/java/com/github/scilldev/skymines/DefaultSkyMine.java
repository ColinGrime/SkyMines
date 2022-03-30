package com.github.scilldev.skymines;

import com.github.scilldev.SkyMines;
import com.github.scilldev.skymines.structure.MineStructure;
import com.github.scilldev.skymines.upgrades.Upgrades;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class DefaultSkyMine implements SkyMine {

	private final SkyMines plugin;
	private final UUID uuid;
	private final UUID owner;
	private final MineStructure structure;
	private final Location home;
	private final Upgrades upgrades;

	public DefaultSkyMine(SkyMines plugin, UUID owner, MineStructure structure, Location home, Upgrades upgrades) {
		this(plugin, UUID.randomUUID(), owner, structure, home, upgrades);
	}

	public DefaultSkyMine(SkyMines plugin, UUID uuid, UUID owner, MineStructure structure, Location home, Upgrades upgrades) {
		this.plugin = plugin;
		this.uuid = uuid;
		this.owner = owner;
		this.structure = structure;
		this.home = home;
		this.upgrades = upgrades;

		reset();
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public int getId() {
		Player player = Bukkit.getPlayer(owner);
		if (player == null) {
			return -1;
		}

		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(player);
		for (int i=0; i<skyMines.size(); i++) {
			if (skyMines.get(i).equals(this)) {
				return i + 1;
			}
		}

		return -1;
	}

	@Override
	public MineStructure getStructure() {
		return null;
	}

	@Override
	public Location getHome() {
		return home;
	}

	@Override
	public Upgrades getUpgrades() {
		return upgrades;
	}

	@Override
	public void reset() {
		structure.buildInside();
	}
}
