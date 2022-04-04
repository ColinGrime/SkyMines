package com.github.colingrime.panel.setup.slot;

import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class PanelSlot {

	public ItemStack getItem(SkyMineUpgrades upgrades) {
		ItemStack item = new ItemStack(getType(upgrades));
		if (item.getType() == Material.AIR || item.getItemMeta() == null) {
			return item;
		}

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName(upgrades));
		meta.setLore(getLore(upgrades));

		item.setItemMeta(meta);
		return item;
	}

	public abstract Material getType(SkyMineUpgrades upgrades);
	public abstract String getName(SkyMineUpgrades upgrades);
	public abstract List<String> getLore(SkyMineUpgrades upgrades);
}
