package com.github.colingrime.skymines.factory;

import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface SkyMineFactory {

	/**
	 * Creates a sky mine at the given location.
	 *
	 * @param owner player who placed the skymine down
	 * @param location location of the skymine
	 * @param size size of the skymine
	 * @param upgrades upgrades of the skymine
	 * @return created SkyMine if one can be placed
	 */
	Optional<SkyMine> createSkyMine(Player owner, Location location, MineSize size, SkyMineUpgrades upgrades);
}
