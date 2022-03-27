package com.github.scilldev.skymines;

import com.github.scilldev.skymines.structure.MineStructure;
import com.github.scilldev.skymines.upgrades.Upgrades;
import org.bukkit.Location;

import java.util.UUID;

public class DefaultSkyMine implements SkyMine {

	private final UUID uuid;
	private final UUID owner;
	private final MineStructure structure;
	private final Location home;
	private final Upgrades upgrades;

	public DefaultSkyMine(UUID owner, MineStructure structure, Location home, Upgrades upgrades) {
		this(UUID.randomUUID(), owner, structure, home, upgrades);
	}

	public DefaultSkyMine(UUID uuid, UUID owner, MineStructure structure, Location home, Upgrades upgrades) {
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
