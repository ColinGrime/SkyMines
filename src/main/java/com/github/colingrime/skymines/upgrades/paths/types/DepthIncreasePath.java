package com.github.colingrime.skymines.upgrades.paths.types;

import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.skymines.upgrades.paths.UpgradePath;
import com.github.colingrime.skymines.upgrades.paths.UpgradePathException;
import com.github.colingrime.utils.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;

public class DepthIncreasePath extends UpgradePath<Integer> {

	public DepthIncreasePath(String name, ConfigurationSection sec) throws UpgradePathException {
		super(name, sec);
	}

	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.DepthIncrease;
	}

	@Override
	protected Integer parseUpgradeData(ConfigurationSection sec, String level) throws UpgradePathException {
		String str = sec.getString(level + ".upgrade");
		if (str == null || !NumberUtils.isDigit(str)) {
			throw new UpgradePathException("Failed to parse the depth data (see: " + getName() + ")");
		}

		return Integer.parseInt(str);
	}
}
