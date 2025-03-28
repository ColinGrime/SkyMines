package me.colingrimes.skymines.skymine.upgrades;

import me.colingrimes.midnight.util.text.Parser;

import javax.annotation.Nullable;

public enum UpgradeType {

	BlockVariety,
	ResetCooldown;

	@Nullable
	public static UpgradeType parse(@Nullable String name) {
		return Parser.parse(UpgradeType.class, name).orElse(null);
	}
}
