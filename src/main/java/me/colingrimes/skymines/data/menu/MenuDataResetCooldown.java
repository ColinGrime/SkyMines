package me.colingrimes.skymines.data.menu;

import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.skymines.data.MenuData;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.upgrade.type.ResetCooldownUpgrade;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class MenuDataResetCooldown implements MenuData {

	private final Material material;
	private final String nameDefault;
	private final String nameMax;
	private final String loreDefault;
	private final String loreNext;

	public MenuDataResetCooldown(@Nonnull ConfigurationSection section) {
		this.material = Material.getMaterial(section.getString("type", "CLOCK").toUpperCase());
		this.nameDefault = section.getString("name.default");
		this.nameMax = section.getString("name.max");
		this.loreDefault = section.getString("lore.default");
		this.loreNext = section.getString("lore.next");
	}

	@Nonnull
	@Override
	public ItemStack getMenuItem(@Nonnull SkyMine skyMine) {
		ResetCooldownUpgrade upgrade = skyMine.getUpgrades().getResetCooldown();
		return Items.of(material)
				.name(upgrade.canBeUpgraded() ? nameDefault : nameMax)
				.lore(upgrade.getUpgradeData().getLore(upgrade.getLevel()))
				.placeholder("{level}", upgrade.getLevel())
				.placeholder("{next-level}", upgrade.getLevel() + 1)
				.build();
	}

	@Nonnull
	public String getLoreDefault() {
		return loreDefault;
	}

	@Nonnull
	public String getLoreNext() {
		return loreNext;
	}
}
