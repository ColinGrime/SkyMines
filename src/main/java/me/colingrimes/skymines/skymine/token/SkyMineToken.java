package me.colingrimes.skymines.skymine.token;

import me.colingrimes.midnight.geometry.Size;
import me.colingrimes.skymines.config.Mines;
import me.colingrimes.skymines.skymine.upgrade.SkyMineUpgrades;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Responsible for the creation and parsing of the physical skymine token item.
 */
public interface SkyMineToken {

	/**
	 * Gets the skymine token with the specified identifier and mine size.
	 * <p>
	 * There must be a matching mine identifier in the mines.yml file.
	 *
	 * @param identifier the mine identifier
	 * @param mineSize the mine size
	 * @return skymine item if available
	 */
	@Nonnull
	Optional<ItemStack> getToken(@Nonnull String identifier, @Nonnull Size mineSize);

	/**
	 * Gets the skymine token with the specified identifier, mine size, and upgrades.
	 * This is used when you pick up a skymine to ensure the upgrades are reflected.
	 *
	 * @param identifier the mine identifier
	 * @param mineSize the mine size
	 * @param upgrades the upgrades of the mine
	 * @return skymine item if available
	 */
	@Nonnull
	Optional<ItemStack> getToken(@Nonnull String identifier, @Nonnull Size mineSize, @Nonnull SkyMineUpgrades upgrades);

	/**
	 * Checks if the provided item is a skymine token.
	 *
	 * @param item the item to check
	 * @return true if the item is a skymine token
	 */
	boolean isToken(@Nonnull ItemStack item);

	/**
	 * Gets the {@link Mines.Mine} configuration data based on the item.
	 *
	 * @param item the item
	 * @return configuration data if available
	 */
	@Nonnull
	Optional<Mines.Mine> getMine(@Nonnull ItemStack item);

	/**
	 * Gets the {@link Size} based on the item.
	 *
	 * @param item the item
	 * @return size of the skymine if available
	 */
	@Nonnull
	Optional<Size> getMineSize(@Nonnull ItemStack item);

	/**
	 * Gets the {@link SkyMineUpgrades} based on the item.
	 *
	 * @param item the item
	 * @return upgrades of the skymine if available
	 */
	@Nonnull
	SkyMineUpgrades getUpgrades(@Nonnull ItemStack item);
}
