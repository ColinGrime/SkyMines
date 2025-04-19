package me.colingrimes.skymines.data.menu;

import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.skymines.data.MenuData;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.data.upgrade.UpgradeDataComposition;
import me.colingrimes.skymines.skymine.upgrade.type.CompositionUpgrade;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class MenuDataComposition implements MenuData {

	private final Material material;
	private final String nameDefault;
	private final String nameMax;
	private final Map<UpgradeDataComposition.MaterialDiff, String> loreDiff = new HashMap<>();

	public MenuDataComposition(@Nonnull ConfigurationSection section) {
		this.material = Material.getMaterial(section.getString("type", "AMETHYST_CLUSTER").toUpperCase());
		this.nameDefault = section.getString("name.default");
		this.nameMax = section.getString("name.max");
		for (var diff : UpgradeDataComposition.MaterialDiff.values()) {
			loreDiff.put(diff, section.getString("lore." + diff.name().toLowerCase(), diff.getDefaultLore()));
		}
	}

	@Nonnull
	@Override
	public ItemStack getMenuItem(@Nonnull SkyMine skyMine) {
		CompositionUpgrade upgrade = skyMine.getUpgrades().getComposition();
		return Items.of(material)
				.name(upgrade.canBeUpgraded() ? nameDefault : nameMax)
				.lore(upgrade.getUpgradeData().getLore(upgrade.getLevel()))
				.placeholder("{level}", upgrade.getLevel())
				.placeholder("{next-level}", upgrade.getLevel() + 1)
				.build();
	}

	@Nonnull
	public String getLore(@Nonnull UpgradeDataComposition.MaterialDiff diff) {
		return loreDiff.get(diff);
	}
}
