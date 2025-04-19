package me.colingrimes.skymines.skymine.factory;

import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SkyMineFactory {

	/**
	 * Creates a skymine at the specified location.
	 *
	 * @param owner player who placed the skymine down
	 * @param token the token item stack
	 * @return created skymine if one can be placed
	 */
	@Nonnull
	Optional<SkyMine> createSkyMine(@Nonnull Player owner, @Nonnull ItemStack token);
}
