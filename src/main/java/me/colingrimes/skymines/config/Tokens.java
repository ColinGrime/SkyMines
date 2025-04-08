package me.colingrimes.skymines.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.skymines.skymine.structure.MineSize;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

import static me.colingrimes.midnight.config.option.OptionFactory.item;
import static me.colingrimes.midnight.config.option.OptionFactory.option;

@Configuration("tokens.yml")
public interface Tokens {

	Option<String> SIZE_PLACEHOLDER = option("size-placeholder", "&7[&a{length}&7x&a{height}&7x&a{width}&7]");
	Option<ItemStack> DEFAULT_ITEM = item("default-item");
	Option<Map<String, Token>> TOKENS = option("tokens", sec -> {
		Map<String, Token> tokens = new HashMap<>();
		for (String name : sec.getKeys(false)) {
			ConfigurationSection s = sec.getConfigurationSection(name);
			if (s != null) {
				tokens.put(name, new Token(name, s));
			}
		}
		return tokens;
	});

	class Token {
		private final String id;
		private final ItemStack item;
		private final MineSize mineSize;
		private final Material borderType;
		private final Map<UpgradeType, String> upgrades = new HashMap<>();

		public Token(@Nonnull String id, @Nonnull ConfigurationSection sec) {
			this.id = id;
			this.item = Items.create().config(sec.getConfigurationSection("item")).build();
			this.mineSize = MineSize.deserialize(String.join(":", sec.getStringList("mine-size")));
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
		public ItemStack getItem() {
			return item.getItemMeta() != null && item.getItemMeta().hasDisplayName() ? item : Tokens.DEFAULT_ITEM.get();
		}

		@Nonnull
		public Map<UpgradeType, String> getUpgrades() {
			return upgrades;
		}

		@Nonnull
		public MineSize getMineSize() {
			return mineSize != null ? mineSize : new MineSize(10, 10, 10);
		}

		@Nonnull
		public Material getBorderType() {
			return borderType != null ? borderType : Material.BEDROCK;
		}
	}
}
