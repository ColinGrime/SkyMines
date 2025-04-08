package me.colingrimes.skymines.skymine.upgrade;

import me.colingrimes.midnight.util.text.Parser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum UpgradeType {

	Composition("Composition"),
	ResetCooldown("Reset Cooldown");

	private final String name;

	UpgradeType(@Nonnull String name) {
		this.name = name;
	}

	/**
	 * Gets the name of the upgrade type.
	 *
	 * @return the name of the upgrade type
	 */
	@Nonnull
	public String getName() {
		return name;
	}

	@Nullable
	public static UpgradeType parse(@Nullable String name) {
		return Parser.parse(UpgradeType.class, name).orElse(null);
	}
}
