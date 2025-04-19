package me.colingrimes.skymines.data;

import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public interface MenuData {

	/**
	 * Constructs and returns the menu item that represents a specific upgrade in a skymine.
	 *
	 * @param skyMine the skymine to use in the item creation
	 * @return the menu item
	 */
	@Nonnull
	ItemStack getMenuItem(@Nonnull SkyMine skyMine);
}
