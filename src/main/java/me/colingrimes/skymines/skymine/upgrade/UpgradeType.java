package me.colingrimes.skymines.skymine.upgrade;

import me.colingrimes.midnight.util.text.Parser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum UpgradeType {

	Composition("Composition", "composition"),
	ResetCooldown("Reset Cooldown", "reset-cooldown");

	private final String name;
	private final String path;

	UpgradeType(@Nonnull String name, @Nonnull String path) {
		this.name = name;
		this.path = path;
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

	/**
	 * Gets the path of the upgrade type.
	 *
	 * @return the configuration path
	 */
	@Nonnull
	public String getPath() {
		return path;
	}

	@Nullable
	public static UpgradeType parse(@Nullable String name) {
		return Parser.parse(UpgradeType.class, name).orElse(null);
	}
}
