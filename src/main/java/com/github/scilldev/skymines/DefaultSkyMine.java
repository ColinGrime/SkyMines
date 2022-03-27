package com.github.scilldev.skymines;

import com.github.scilldev.skymines.upgrades.Upgrades;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;
import java.util.UUID;

public class DefaultSkyMine implements SkyMine {

	private final UUID uuid;
	private final UUID owner;
	private final Location home;
	private final List<Block> blocks;
	private final Upgrades upgrades;

	public DefaultSkyMine(UUID owner, Location home, List<Block> blocks, Upgrades upgrades) {
		this(UUID.randomUUID(), owner, home, blocks, upgrades);
	}

	public DefaultSkyMine(UUID uuid, UUID owner, Location home, List<Block> blocks, Upgrades upgrades) {
		this.uuid = uuid;
		this.owner = owner;
		this.home = home;
		this.blocks = blocks;
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
	public Location getHome() {
		return home;
	}

	@Override
	public List<Block> getBlocks() {
		return blocks;
	}

	@Override
	public Upgrades getUpgrades() {
		return upgrades;
	}

	@Override
	public void reset() {
		for (Block block : blocks) {
			block.setType(Material.STONE);
		}
	}
}
