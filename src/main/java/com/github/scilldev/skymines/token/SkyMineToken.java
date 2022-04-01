package com.github.scilldev.skymines.token;

import com.github.scilldev.skymines.structure.MineSize;
import com.github.scilldev.skymines.upgrades.SkyMineUpgrades;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface SkyMineToken {

	/**
	 * @return default skymine token
	 */
	ItemStack getToken();

	/**
	 * @param size size of the mine
	 * @return skymine token with custom size
	 */
	ItemStack getToken(MineSize size);

	/**
	 * @param size size of the mine
	 * @param upgrades upgrades of the mine
	 * @return skymine token with custom size and upgrades
	 */
	ItemStack getToken(MineSize size, SkyMineUpgrades upgrades);

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
}
