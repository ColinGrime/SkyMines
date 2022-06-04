package com.github.colingrime.skymines.token;

import com.github.colingrime.config.custom.implementation.TokenConfig;
import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface SkyMineToken {

	/**
	 * @param token the TokenItem object you want to be physical
	 * @return skymine token with custom size, upgrades, and border type
	 */
	ItemStack getToken(TokenConfig.TokenItem token);

	/**
	 * @param item any item
	 * @return true if the item is a sky mine token
	 */
	boolean isToken(ItemStack item);

	/**
	 * @param item any item
	 * @return size of the mine if available
	 */
	Optional<MineSize> getMineSize(ItemStack item);

	/**
	 * @param item any item
	 * @return upgrades of the mine if available
	 */
	Optional<SkyMineUpgrades> getUpgrades(ItemStack item);

	/**
	 * @param item any item
	 * @return border type of the mine if available
	 */
	Optional<Material> getBorderType(ItemStack item);
}
