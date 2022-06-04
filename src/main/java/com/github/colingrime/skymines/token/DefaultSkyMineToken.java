package com.github.colingrime.skymines.token;

import com.github.colingrime.SkyMines;
import com.github.colingrime.config.custom.implementation.TokenConfig;
import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.locale.Replacer;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

public class DefaultSkyMineToken implements SkyMineToken {

	private final SkyMines plugin;

	public DefaultSkyMineToken(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public ItemStack getToken(TokenConfig.TokenItem token) {
		NBTItem nbtItem = new NBTItem(token.item());
		nbtItem.setBoolean("skymine", true);
		nbtItem.setObject("skymine-size", token.size());
		nbtItem.setObject("skymine-upgrades", token.upgrades());
		nbtItem.setObject("skymine-bordertype", token.borderType());
		return nbtItem.getItem();
	}

	@Override
	public boolean isToken(ItemStack item) {
		if (isInvalidItem(item)) {
			return false;
		}

		NBTItem nbtItem = new NBTItem(item);
		return nbtItem.getBoolean("skymine");
	}

	@Override
	public Optional<MineSize> getMineSize(ItemStack item) {
		if (isInvalidItem(item)) {
			return Optional.empty();
		}

		NBTItem nbtItem = new NBTItem(item);
		return Optional.ofNullable(nbtItem.getObject("skymine-size", MineSize.class));
	}

	@Override
	public Optional<SkyMineUpgrades> getUpgrades(ItemStack item) {
		if (isInvalidItem(item)) {
			return Optional.empty();
		}

		NBTItem nbtItem = new NBTItem(item);
		return Optional.of(nbtItem.getObject("skymine-upgrades", SkyMineUpgrades.class));
	}

	@Override
	public Optional<Material> getBorderType(ItemStack item) {
		if (isInvalidItem(item)) {
			return Optional.empty();
		}

		NBTItem nbtItem = new NBTItem(item);
		return Optional.ofNullable(nbtItem.getObject("skymine-bordertype", Material.class));
	}

	private boolean isInvalidItem(ItemStack item) {
		return item == null || item.getType() == Material.AIR;
	}
}
