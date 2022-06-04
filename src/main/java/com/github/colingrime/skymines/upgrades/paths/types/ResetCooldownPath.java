package com.github.colingrime.skymines.upgrades.paths.types;

import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.skymines.upgrades.paths.UpgradePath;
import com.github.colingrime.skymines.upgrades.paths.UpgradePathException;
import com.github.colingrime.utils.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;

public class ResetCooldownPath extends UpgradePath<Double> {

	public ResetCooldownPath(String name, ConfigurationSection sec) throws UpgradePathException {
		super(name, sec);
	}

	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.ResetCooldown;
	}

	@Override
	protected Double parseUpgradeData(ConfigurationSection sec, String level) throws UpgradePathException {
		String str = sec.getString(level + ".upgrade");
		if (str == null || !NumberUtils.isDigit(str.split(" ")[0])) {
			throw new UpgradePathException("Failed to parse the cooldown data (see: " + getName() + ")");
		}

		String[] cooldown = str.split(" ");
		if (cooldown.length > 1 && cooldown[1].contains("minute")) {
			return Double.parseDouble(cooldown[0]) * 60;
		} else {
			return Double.parseDouble(cooldown[0]);
		}
	}
}
