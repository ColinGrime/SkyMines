package com.github.scilldev.skymines.token;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DefaultSkyMineToken implements SkyMineToken {

	private final Material testType = Material.TRIPWIRE_HOOK;

	@Override
	public ItemStack getToken() {
		NBTItem nbtItem = new NBTItem(new ItemStack(testType));
		nbtItem.setBoolean("skymine", true);
		return nbtItem.getItem();
	}

	@Override
	public boolean isToken(ItemStack item) {
		if (item == null || item.getType() == Material.AIR) {
			return false;
		}

		NBTItem nbtItem = new NBTItem(item);
		return nbtItem.getBoolean("skymine");
	}
}
