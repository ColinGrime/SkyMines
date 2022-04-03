package com.github.scilldev.panel.setup.slot;

import com.github.scilldev.skymines.SkyMine;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class PanelSlot {

	public ItemStack getItem(SkyMine skyMine) {
		ItemStack item = new ItemStack(getType(skyMine));
		if (item.getType() == Material.AIR || item.getItemMeta() == null) {
			return item;
		}

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName(skyMine));
		meta.setLore(getLore(skyMine));

		item.setItemMeta(meta);
		return item;
	}

	public abstract Material getType(SkyMine skyMine);
	public abstract String getName(SkyMine skyMine);
	public abstract List<String> getLore(SkyMine skyMine);
}
