package me.colingrimes.skymines.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.geometry.Size;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.io.Logger;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;
import me.colingrimes.skymines.util.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration("mines.yml")
public interface Mines {

	Option<String> SIZE_PLACEHOLDER = option("size-placeholder", "&7[&a{length}&7x&a{height}&7x&a{width}&7]");
	Option<ItemStack> DEFAULT_TOKEN = item("default-token");
	Option<Map<String, Mine>> MINES = keys("mines", Mine::of);
	Option<Set<String>> MINES_ALL = option("mines", section -> section.getKeys(false));

	class Mine {
		private final String identifier;
		private final ItemStack token;
		private final Size defaultSize;
		private final Material borderType;
		private final Map<UpgradeType, String> upgrades;

		@Nullable
		private static Mine of(@Nonnull ConfigurationSection section, @Nonnull String identifier) {
			ItemStack token = Items.create().config(section.getConfigurationSection("token")).build();
			Size size = Size.of(section.getStringList("default-size"));
			Material borderType = Material.getMaterial(section.getString("border-type", ""));

			Map<UpgradeType, String> upgrades = new HashMap<>();
			ConfigurationSection upgradeSection = section.getConfigurationSection("upgrades");
			if (upgradeSection != null) {
				for (String type : upgradeSection.getKeys(false)) {
					UpgradeType upgradeType = UpgradeType.parse(type);
					if (upgradeType != null) {
						upgrades.put(upgradeType, section.getString("upgrades." + type));
					}
				}
			}

			// Don't load in mines without a valid default size.
			if (size == null) {
				Logger.warn("[SkyMines] Identifier '" + identifier + "' does not have a valid default size.");
				return null;
			}

			// Don't load in mines without a solid border type.
			if (!Utils.isValidBorderType(borderType)) {
				Logger.warn("[SkyMines] Identifier '" + identifier + "' does not have a solid (no gravity) border type.");
				return null;
			}

			return new Mine(identifier, token, size, borderType, upgrades);
		}

		private Mine(@Nonnull String identifier, @Nonnull ItemStack token, @Nonnull Size defaultSize, @Nonnull Material borderType, @Nonnull Map<UpgradeType, String> upgrades) {
			this.identifier = identifier;
			this.token = token;
			this.defaultSize = defaultSize;
			this.borderType = borderType;
			this.upgrades = upgrades;
		}

		@Nonnull
		public String getIdentifier() {
			return identifier;
		}

		@Nonnull
		public ItemStack getToken() {
			return token.getItemMeta() != null && token.getItemMeta().hasDisplayName() ? token.clone() : Mines.DEFAULT_TOKEN.get().clone();
		}

		@Nonnull
		public Size getDefaultSize() {
			return defaultSize;
		}

		@Nonnull
		public Material getBorderType() {
			return borderType;
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
