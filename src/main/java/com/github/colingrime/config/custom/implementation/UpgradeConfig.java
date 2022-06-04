package com.github.colingrime.config.custom.implementation;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.skymines.upgrades.paths.UpgradePath;
import com.github.colingrime.skymines.upgrades.paths.UpgradePathException;
import com.github.colingrime.skymines.upgrades.paths.types.BlockVarietyPath;
import com.github.colingrime.skymines.upgrades.paths.types.DepthIncreasePath;
import com.github.colingrime.skymines.upgrades.paths.types.ResetCooldownPath;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Optional;

public class UpgradeConfig extends AbstractConfiguration {

	private Map<String, UpgradePath<?>> upgradePaths;
	private Map<UpgradeType, String> defaultPaths;

	public UpgradeConfig(SkyMines plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "upgrades.yml";
	}

	@Override
	void updateValues() {
		upgradePaths = _getUpgradePaths();
	}

	private Map<String, UpgradePath<?>> _getUpgradePaths() {
		return createMap("", (sec, key) -> {
			Optional<UpgradeType> upgradeType = UpgradeType.from(key + ".type");
			if (upgradeType.isEmpty()) {
				return null;
			}

			try {
				return switch (upgradeType.get()) {
					case BlockVariety -> new BlockVarietyPath(key, sec);
					case DepthIncrease -> new DepthIncreasePath(key, sec);
					case ResetCooldown -> new ResetCooldownPath(key, sec);
				};
			} catch (UpgradePathException ex) {
				ex.printStackTrace();
			}

			return null;
		});
	}

	public Optional<UpgradePath<?>> getUpgradePath(String name) {
		return Optional.ofNullable(upgradePaths.get(name));
	}

	private Map<UpgradeType, String> _getDefaultPaths() {
		return createMap("defaults", (key) -> UpgradeType.from(key).orElse(null), ConfigurationSection::getString);
	}

	public String getDefaultPath(UpgradeType upgradeType) {
		return defaultPaths.get(upgradeType);
	}
}
