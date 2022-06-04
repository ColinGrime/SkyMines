package com.github.colingrime.skymines.upgrades;

import com.github.colingrime.utils.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum UpgradeType {

	BlockVariety,
	DepthIncrease,
	ResetCooldown;

	/**
	 * @param name name to parse
	 * @return Optional containing parsed UpgradeType if available
	 */
	public static Optional<UpgradeType> from(String name) {
		return Arrays.stream(UpgradeType.values()).filter(t -> t.name().equalsIgnoreCase(StringUtils.strip(name))).findAny();
	}
}
