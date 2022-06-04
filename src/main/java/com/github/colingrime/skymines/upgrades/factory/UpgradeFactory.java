package com.github.colingrime.skymines.upgrades.factory;

import com.github.colingrime.skymines.upgrades.UpgradeType;

public interface UpgradeFactory {

	/**
	 * @param type type of upgrade to create
	 * @param name name of the upgrade
	 * @return new SkyMineUpgrade object depending on {@code upgradeType}
	 */
	SkyMineUpgrade<?> createUpgrade(UpgradeType type, String name);

	/**
	 * @param type type of upgrade to create
	 * @param name name of the upgrade
	 * @param level level of the upgrade
	 * @return new SkyMineUpgrade object depending on {@code upgradeType}
	 */
	SkyMineUpgrade<?> createUpgrade(UpgradeType type, String name, int level);
}
