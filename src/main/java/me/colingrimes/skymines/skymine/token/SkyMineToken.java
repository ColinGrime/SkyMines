package me.colingrimes.skymines.skymine.token;

import me.colingrimes.midnight.geometry.Size;
import me.colingrimes.skymines.config.Mines;
import me.colingrimes.skymines.skymine.upgrade.SkyMineUpgrades;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Responsible for the creation, validation, and parsing of the physical skymine token item.
 */
public interface SkyMineToken {

	/**
	 * Checks if the provided item is a skymine token.
	 *
	 * @param item the item to check
	 * @return true if the item is a skymine token
	 */
	boolean isToken(@Nonnull ItemStack item);

	/**
	 * Checks if the provided item is a valid skymine token.
	 * <p>
	 * To be valid, {@link SkyMineToken#isToken(ItemStack)} must first be true.
	 * If the token has an attached skymine identifier, it must also be present in the mines.yml file.
	 * If the token does not have any identifier, it will return true assuming a default mine is configured.
	 *
	 * @param item the item to check
	 * @return true if the item is a valid skymine token
	 */
	boolean isValidToken(@Nonnull ItemStack item);

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
	ItemStack getToken(@Nonnull String identifier, @Nonnull Size mineSize);

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
	ItemStack getToken(@Nonnull String identifier, @Nonnull Size mineSize, @Nonnull SkyMineUpgrades upgrades);

	/**
	 * Gets the {@link Mines.Mine} configuration data based on the token.
	 * If the token has an attached skymine identifier, it must also be present in the mines.yml file.
	 * If the token does not have any identifier, it will return the default mine data assuming a default mine is configured.
	 *
	 * @param token the token
	 * @return configuration data if available
	 */
	@Nullable
	Mines.Mine getMine(@Nonnull ItemStack token);

	/**
	 * Gets the mine identifier based on the token.
	 * If the token does not have any identifier, it will return "default".
	 *
	 * @param token the token
	 * @return mine identifier
	 */
	@Nonnull
	String getMineIdentifier(@Nonnull ItemStack token);

	/**
	 * Gets the {@link Size} based on the token.
	 *
	 * @param token the token
	 * @return size of the skymine
	 */
	@Nonnull
	Size getMineSize(@Nonnull ItemStack token);

	/**
	 * Gets the {@link SkyMineUpgrades} based on the token.
	 *
	 * @param token the token
	 * @return upgrades of the skymine
	 */
	@Nonnull
	SkyMineUpgrades getUpgrades(@Nonnull ItemStack token);
}
