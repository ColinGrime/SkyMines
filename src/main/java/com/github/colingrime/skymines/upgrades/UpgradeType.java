package com.github.colingrime.skymines.upgrades;

import com.github.colingrime.utils.Utils;

public enum UpgradeType {

	BlockVariety,
	ResetCooldown;

	public static UpgradeType getUpgradeType(String name) {
		name = Utils.strip(name);
		for (UpgradeType type : UpgradeType.values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
