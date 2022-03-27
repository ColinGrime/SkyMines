package com.github.scilldev.skymines.token;

import org.bukkit.inventory.ItemStack;

public interface SkyMineToken {

	/**
	 * @return sky mine token in item form
	 */
	ItemStack getToken();

	/**
	 * @param item any item
	 * @return true if the item is a sky mine token
	 */
	boolean isToken(ItemStack item);
}
