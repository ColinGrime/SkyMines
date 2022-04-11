package com.github.colingrime.skymines;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.structure.MineStructure;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class DefaultSkyMine implements SkyMine {

	private final UUID uuid;
	private final UUID owner;
	private final MineStructure structure;
	private final Location home;
	private final SkyMineUpgrades upgrades;

	public DefaultSkyMine(UUID owner, MineStructure structure, Location home, SkyMineUpgrades upgrades) {
		this(UUID.randomUUID(), owner, structure, home, upgrades);
	}

	public DefaultSkyMine(UUID uuid, UUID owner, MineStructure structure, Location home, SkyMineUpgrades upgrades) {
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

		List<SkyMine> skyMines = SkyMines.getInstance().getSkyMineManager().getSkyMines(player);
		for (int i=0; i<skyMines.size(); i++) {
			if (skyMines.get(i).equals(this)) {
				return i + 1;
			}
		}

		return -1;
	}

	@Override
	public MineStructure getStructure() {
		return structure;
	}

	@Override
	public Location getHome() {
		return home;
	}

	@Override
	public SkyMineUpgrades getUpgrades() {
		return upgrades;
	}

	@Override
	public void reset() {
		structure.buildInside(upgrades.getBlockVarietyUpgrade().getBlockVariety());
	}
}
