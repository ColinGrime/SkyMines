package me.colingrimes.skymines.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.geometry.Size;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration("mines.yml")
public interface Mines {

	Option<String> SIZE_PLACEHOLDER = option("size-placeholder", "&7[&a{length}&7x&a{height}&7x&a{width}&7]");
	Option<ItemStack> DEFAULT_TOKEN = item("default-token");
	Option<Map<String, Mine>> MINES = keys("mines", Mine::new);

	class Mine {
		private final String id;
		private final ItemStack token;
		private final Size defaultSize;
		private final Material borderType;
		private final Map<UpgradeType, String> upgrades = new HashMap<>();

		public Mine(@Nonnull ConfigurationSection sec, @Nonnull String id) {
			this.id = id;
			this.token = Items.create().config(sec.getConfigurationSection("token")).build();
			this.defaultSize = Size.of(sec.getStringList("default-size"));
			this.borderType = Material.getMaterial(sec.getString("border-type", "BEDROCK"));
			for (String type : Objects.requireNonNull(sec.getConfigurationSection("upgrades")).getKeys(false)) {
				UpgradeType upgradeType = UpgradeType.parse(type);
				if (upgradeType != null) {
					upgrades.put(upgradeType, sec.getString("upgrades." + type));
				}
			}
		}

		@Nonnull
		public String getId() {
			return id;
		}

		@Nonnull
		public ItemStack getToken() {
			return token.getItemMeta() != null && token.getItemMeta().hasDisplayName() ? token.clone() : Mines.DEFAULT_TOKEN.get().clone();
		}

		@Nonnull
		public Size getDefaultSize() {
			return defaultSize != null ? defaultSize : Size.of(10);
		}

		@Nonnull
		public Material getBorderType() {
			return borderType != null ? borderType : Material.BEDROCK;
		}

		@Nonnull
		public Map<UpgradeType, String> getUpgrades() {
			return upgrades;
		}

		@Nonnull
		public String getUpgradeId(@Nonnull UpgradeType type) {
			return upgrades.get(type);
		}
	}
}
