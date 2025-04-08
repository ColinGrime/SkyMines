package me.colingrimes.skymines.skymine.upgrade;

import me.colingrimes.skymines.skymine.upgrade.type.CompositionUpgrade;
import me.colingrimes.skymines.skymine.upgrade.type.ResetCooldownUpgrade;
import me.colingrimes.skymines.skymine.upgrade.type.SkyMineUpgrade;

import javax.annotation.Nonnull;

public class SkyMineUpgrades {

	private final CompositionUpgrade composition;
	private final ResetCooldownUpgrade resetCooldown;

	public SkyMineUpgrades() {
		this(1, 1);
	}

	public SkyMineUpgrades(int compositionLevel, int resetCooldownLevel) {
		this(new CompositionUpgrade(compositionLevel), new ResetCooldownUpgrade(resetCooldownLevel));
	}

	public SkyMineUpgrades(@Nonnull CompositionUpgrade composition, @Nonnull ResetCooldownUpgrade resetCooldown) {
		this.composition = composition;
		this.resetCooldown = resetCooldown;
	}

	/**
	 * Gets the upgrade instance associated with the specified upgrade type.
	 *
	 * @param upgradeType the upgrade type
	 * @return the upgrade associated with the upgrade type
	 */
	@Nonnull
	public SkyMineUpgrade getUpgrade(UpgradeType upgradeType) {
		return switch (upgradeType) {
			case Composition -> composition;
			case ResetCooldown -> resetCooldown;
		};
	}

	/**
	 * Gets the composition upgrade.
	 *
	 * @return the composition upgrade
	 */
	@Nonnull
	public CompositionUpgrade getComposition() {
		return composition;
	}

	/**
	 * Gets the reset cooldown upgrade.
	 *
	 * @return the reset cooldown upgrade
	 */
	@Nonnull
	public ResetCooldownUpgrade getResetCooldown() {
		return resetCooldown;
	}

	@Nonnull
	public String serialize() {
		return composition.getLevel() + ":" + resetCooldown.getLevel();
	}

	@Nonnull
	public static SkyMineUpgrades deserialize(@Nonnull String text) {
		if (!text.contains("\n")) {
			return deserializeOld(text);
		}

		String[] texts = text.split(":");
		String compositionString = texts[0];
		String resetCooldownString = texts[1];

		String compositionName = compositionString.split(":")[0];
		String resetCooldownName = resetCooldownString.split(":")[0];

		int compositionLevel = 1;
		int resetCooldownLevel = 1;

		try {
			compositionLevel = Integer.parseInt(compositionString.split(":")[1]);
			resetCooldownLevel = Integer.parseInt(resetCooldownString.split(":")[1]);
		} catch (NumberFormatException ignored) {}


		return new SkyMineUpgrades(
				new CompositionUpgrade(compositionName, compositionLevel),
				new ResetCooldownUpgrade(resetCooldownName, resetCooldownLevel)
		);
	}

	@Nonnull
	private static SkyMineUpgrades deserializeOld(@Nonnull String text) {
		String[] texts = text.split(":");

		int compositionLevel = 1;
		int resetCooldownLevel = 1;

		try {
			compositionLevel = Integer.parseInt(texts[0]);
			resetCooldownLevel = Integer.parseInt(texts[1]);
		} catch (NumberFormatException ignored) {}

		return new SkyMineUpgrades(compositionLevel, resetCooldownLevel);
	}
}
