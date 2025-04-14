package me.colingrimes.skymines.config;

import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;
import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.config.util.ConfigurableInventory;
import me.colingrimes.skymines.skymine.upgrade.data.CompositionData;
import me.colingrimes.skymines.skymine.upgrade.type.CompositionUpgrade;
import me.colingrimes.skymines.skymine.upgrade.type.ResetCooldownUpgrade;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration("menus.yml")
public interface Menus {

	Option<ConfigurableInventory> MAIN_MENU = inventory("main-menu");
	Option<ConfigurableInventory> UPGRADE_MENU = inventory("upgrade-menu");
	Option<Map<Integer, UpgradeType>> UPGRADE_MENU_SLOTS = slots("upgrade-menu.slots", sec -> UpgradeType.parse(sec.getString("upgrade")));
	Option<Composition> UPGRADE_MENU_COMPOSITION = option("upgrades.composition", Composition::new);
	Option<ResetCooldown> UPGRADE_MENU_RESET_COOLDOWN = option("upgrades.reset-cooldown", ResetCooldown::new);

	class Composition {
		private final Material material;
		private final String nameDefault;
		private final String nameMax;
		private final Map<CompositionData.MaterialDiff, String> loreDiff = new HashMap<>();

		public Composition(@Nonnull ConfigurationSection section) {
			this.material = Material.getMaterial(section.getString("type", "AMETHYST_CLUSTER").toUpperCase());
			this.nameDefault = section.getString("name.default");
			this.nameMax = section.getString("name.max");
			for (var diff : CompositionData.MaterialDiff.values()) {
				loreDiff.put(diff, section.getString("lore." + diff.name().toLowerCase(), diff.getDefaultLore()));
			}
		}

		@Nonnull
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
		public String getLore(@Nonnull CompositionData.MaterialDiff diff) {
			return loreDiff.get(diff);
		}
	}

	class ResetCooldown {
		private final Material material;
		private final String nameDefault;
		private final String nameMax;
		private final String loreDefault;
		private final String loreNext;

		public ResetCooldown(@Nonnull ConfigurationSection section) {
			this.material = Material.getMaterial(section.getString("type", "CLOCK").toUpperCase());
			this.nameDefault = section.getString("name.default");
			this.nameMax = section.getString("name.max");
			this.loreDefault = section.getString("lore.default");
			this.loreNext = section.getString("lore.next");
		}

		@Nonnull
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
}
