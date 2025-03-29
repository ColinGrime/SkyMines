package me.colingrimes.skymines.skymine.token;

import me.colingrimes.skymines.skymine.structure.MineSize;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Responsible for the creation and parsing of the physical skymine token item.
 */
public interface SkyMineToken {

	/**
	 * Gets the skymine token with the specified size and border material.
	 *
	 * @param size size of the mine
	 * @param borderType border type of the mine
	 * @return skymine item
	 */
	@Nonnull
	ItemStack getToken(@Nonnull MineSize size, @Nonnull Material borderType);

	/**
	 * Gets the skymine token with the specified size, border material, and upgrades.
	 * This is used when you pick up a skymine to ensure the upgrades are reflected.
	 *
	 * @param size size of the mine
	 * @param borderType border type of the mine
	 * @param upgrades upgrades of the mine
	 * @return skymine item
	 */
	@Nonnull
	ItemStack getToken(@Nonnull MineSize size, @Nonnull Material borderType, @Nonnull SkyMineUpgrades upgrades);

	/**
	 * Checks if the provided item is a SkyMine token.
	 *
	 * @param item the item to check
	 * @return true if the item is a skymine token
	 */
	boolean isToken(@Nonnull ItemStack item);

	/**
	 * Gets the {@link MineSize} based on the item.
	 *
	 * @param item the item
	 * @return size of the skymine if available
	 */
	@Nonnull
	Optional<MineSize> getMineSize(@Nonnull ItemStack item);

	/**
	 * Gets the border material based on the item.
	 *
	 * @param item the item
	 * @return border type of the skymine if available
	 */
	@Nonnull
	Optional<Material> getBorderType(@Nonnull ItemStack item);

	/**
	 * Gets the {@link SkyMineUpgrades} based on the item.
	 *
	 * @param item the item
	 * @return upgrades of the skymine if available
	 */
	@Nonnull
	SkyMineUpgrades getUpgrades(@Nonnull ItemStack item);
}
