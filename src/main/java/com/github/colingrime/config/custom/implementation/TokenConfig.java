package com.github.colingrime.config.custom.implementation;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.skymines.upgrades.paths.UpgradePath;
import com.github.colingrime.utils.ItemBuilder;
import com.github.colingrime.utils.MaterialParser;
import com.github.colingrime.utils.NumberUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TokenConfig extends AbstractConfiguration {

	private Map<String, TokenItem> tokens;

	public TokenConfig(SkyMines plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "tokens.yml";
	}

	@Override
	void updateValues() {
		tokens = _getTokens();
	}

	private Map<String, TokenItem> _getTokens() {
		return createMap("", (sec, key) -> {
			MineSize size = getTokenSize(sec, key);
			ItemStack item = new ItemBuilder(Material.TRIPWIRE_HOOK)
					.material(sec.getString(key + ".item.type"))
					.name(sec.getString(key + ".item.name"))
					.lore(sec.getStringList(key + ".item.lore"))
					.replace("%length%", size.length())
					.replace("%height%", size.height())
					.replace("%width%", size.width())
					.build();
			Map<UpgradeType, String> upgrades = getTokenUpgrades(key);
			Material borderType = MaterialParser.parseMaterial(sec.getString(key + ".border-type"), Material.BEDROCK);

			return new TokenItem(item, upgrades, size, borderType);
		});
	}

	private MineSize getTokenSize(ConfigurationSection tokenSec, String tokenName) {
		List<String> size = tokenSec.getStringList(tokenName + ".size");
		if (size.size() != 3) {
			return new MineSize(10, 10, 10);
		}

		// attempts to parse the size, else gives that side 10
		int length = NumberUtils.parseNumber(size.get(0), 10);
		int height = NumberUtils.parseNumber(size.get(1), 10);
		int width = NumberUtils.parseNumber(size.get(2), 10);

		return new MineSize(length, height, width);
	}

	private Map<UpgradeType, String> getTokenUpgrades(String tokenName) {
		// maps the UpgradeType's with the different upgrade name's created
		Map<UpgradeType, String> upgrades = createMap(tokenName + ".upgrades", key -> {
			Optional<UpgradePath<?>> upgrade = plugin.getUpgrades().getUpgradePath(key);
			return upgrade.map(UpgradePath::getUpgradeType).orElse(null);
		}, (sec, key) -> key);

		// if not every UpgradeType was put on the token, adds default paths to it
		for (UpgradeType type : UpgradeType.values()) {
			if (!upgrades.containsKey(type)) {
				upgrades.put(type, plugin.getUpgrades().getDefaultPath(type));
			}
		}

		return upgrades;
	}

	public Optional<TokenItem> getToken(String tokenName) {
		return Optional.ofNullable(tokens.get(tokenName));
	}

	public static record TokenItem(ItemStack item, Map<UpgradeType, String> upgrades, MineSize size, Material borderType) {}
}
