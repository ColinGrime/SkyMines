package me.colingrimes.skymines.skymine.token;

import me.colingrimes.skymines.skymine.structure.MineSize;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SkyMineToken {

	/**
	 * @return default skymine token
	 */
	@Nonnull
	ItemStack getToken();

	/**
	 * @param size size of the mine
	 * @param borderType border type of the mine
	 * @return skymine token with custom size and upgrades
	 */
	@Nonnull
	ItemStack getToken(@Nonnull MineSize size, @Nonnull Material borderType);

	/**
	 * @param size size of the mine
	 * @param upgrades upgrades of the mine
	 * @param borderType border type of the mine
	 * @return skymine token with custom size, upgrades, and border type
	 */
	@Nonnull
	ItemStack getToken(@Nonnull MineSize size, @Nonnull SkyMineUpgrades upgrades, @Nonnull Material borderType);

	/**
	 * @param item any item
	 * @return true if the item is a sky mine token
	 */
	boolean isToken(@Nonnull ItemStack item);

	/**
	 * @param item any item
	 * @return size of the mine if available
	 */
	@Nonnull
	Optional<MineSize> getMineSize(@Nonnull ItemStack item);

	/**
	 * @param item any item
	 * @return upgrades of the mine if available
	 */
	@Nonnull
	SkyMineUpgrades getUpgrades(@Nonnull ItemStack item);

	/**
	 * @param item any item
	 * @return border type of the mine if available
	 */
	@Nonnull
	Optional<Material> getBorderType(@Nonnull ItemStack item);
}
