package me.colingrimes.skymines.skymine.factory;

import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.structure.MineSize;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SkyMineFactory {

	/**
	 * Creates a skymine at the specified location.
	 *
	 * @param owner player who placed the skymine down
	 * @param location location of the skymine
	 * @param size size of the skymine
	 * @param borderType border type of the skymine
	 * @param upgrades upgrades of the skymine
	 * @return created skymine if one can be placed
	 */
	@Nonnull
	Optional<SkyMine> createSkyMine(@Nonnull Player owner, @Nonnull Location location, @Nonnull MineSize size, @Nonnull Material borderType, @Nonnull SkyMineUpgrades upgrades);
}
