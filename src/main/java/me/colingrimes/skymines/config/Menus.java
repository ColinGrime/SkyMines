package me.colingrimes.skymines.config;

import me.colingrimes.skymines.skymine.upgrade.UpgradeType;
import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.config.util.ConfigurableInventory;
import me.colingrimes.midnight.util.misc.Types;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.Map;

import static me.colingrimes.midnight.config.option.OptionFactory.inventory;
import static me.colingrimes.midnight.config.option.OptionFactory.option;

@Configuration("menus.yml")
public interface Menus {

	Option<ConfigurableInventory> MAIN_MENU = inventory("main-menu");
	Option<ConfigurableInventory> UPGRADE_MENU = inventory("upgrade-menu");
	Option<Map<Integer, UpgradeType>> UPGRADE_MENU_SLOTS = option("upgrade-menu.slots", sec -> Types.mapSlotKeys(sec, (slot -> UpgradeType.parse(sec.getString(slot + ".upgrade")))));
	Option<Composition> UPGRADE_MENU_COMPOSITION = option("upgrades.composition", Composition::new);
	Option<ResetCooldown> UPGRADE_MENU_RESET_COOLDOWN = option("upgrades.reset-cooldown", ResetCooldown::new);

	class Composition {
		private final Material material;
		private final String nameDefault;
		private final String nameMax;
		private final String loreChange;
		private final String loreNew;
		private final String loreMax;

		public Composition(@Nonnull ConfigurationSection sec) {
			this.material = Material.getMaterial(sec.getString("type", "AMETHYST_CLUSTER").toUpperCase());
			this.nameDefault = sec.getString("name.default");
			this.nameMax = sec.getString("name.max");
			this.loreChange = sec.getString("lore.change");
			this.loreNew = sec.getString("lore.new");
			this.loreMax = sec.getString("lore.max");
		}

		@Nonnull
		public Material getMaterial() {
			return material;
		}

		@Nonnull
		public String getNameDefault() {
			return nameDefault;
		}

		@Nonnull
		public String getNameMax() {
			return nameMax;
		}

		@Nonnull
		public String getLoreChange() {
			return loreChange;
		}

		@Nonnull
		public String getLoreNew() {
			return loreNew;
		}

		@Nonnull
		public String getLoreMax() {
			return loreMax;
		}
	}

	class ResetCooldown {
		private final Material material;
		private final String nameDefault;
		private final String nameMax;
		private final String loreDefault;
		private final String loreNext;

		public ResetCooldown(@Nonnull ConfigurationSection sec) {
			this.material = Material.getMaterial(sec.getString("type", "CLOCK").toUpperCase());
			this.nameDefault = sec.getString("name.default");
			this.nameMax = sec.getString("name.max");
			this.loreDefault = sec.getString("lore.default");
			this.loreNext = sec.getString("lore.next");
		}

		@Nonnull
		public Material getMaterial() {
			return material;
		}

		@Nonnull
		public String getNameDefault() {
			return nameDefault;
		}

		@Nonnull
		public String getNameMax() {
			return nameMax;
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
