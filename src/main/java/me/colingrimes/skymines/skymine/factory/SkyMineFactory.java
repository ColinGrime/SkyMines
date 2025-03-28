package me.colingrimes.skymines.skymine.factory;

import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.structure.MineSize;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import org.bukkit.Location;
import org.bukkit.Material;
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
	 * @param borderType border type of the skymine
	 * @return created SkyMine if one can be placed
	 */
	Optional<SkyMine> createSkyMine(Player owner, Location location, MineSize size, SkyMineUpgrades upgrades, Material borderType);
}
