package com.github.scilldev.skymines.token;

import com.github.scilldev.SkyMines;
import com.github.scilldev.locale.Placeholders;
import com.github.scilldev.skymines.structure.MineSize;
import com.github.scilldev.skymines.upgrades.SkyMineUpgrades;
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
	public ItemStack getToken() {
		return getToken(new MineSize(10, 10, 10));
	}

	@Override
	public ItemStack getToken(MineSize size) {
		return getToken(size, new SkyMineUpgrades());
	}

	@Override
	public ItemStack getToken(MineSize size, SkyMineUpgrades upgrades) {
		ItemStack item = new ItemStack(plugin.getSettings().getTokenType());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Placeholders.replaceAll(plugin.getSettings().getTokenName(), size));
		meta.setLore(Placeholders.replaceAll(plugin.getSettings().getTokenLore(), size));
		item.setItemMeta(meta);

		NBTItem nbtItem = new NBTItem(item);
		nbtItem.setBoolean("skymine", true);
		nbtItem.setObject("skymine-size", size);
		nbtItem.setObject("skymine-upgrades", upgrades);
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
		return Optional.ofNullable(nbtItem.getObject("skymine-upgrades", SkyMineUpgrades.class));
	}

	private boolean isInvalidItem(ItemStack item) {
		return item == null || item.getType() == Material.AIR;
	}
}
