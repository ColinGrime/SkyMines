package me.colingrimes.skymines.skymine.upgrades;

import me.colingrimes.skymines.skymine.upgrades.types.BlockVarietyUpgrade;
import me.colingrimes.skymines.skymine.upgrades.types.ResetCooldownUpgrade;
import me.colingrimes.skymines.skymine.upgrades.types.SkyMineUpgrade;

import javax.annotation.Nonnull;

public class SkyMineUpgrades {

	private final BlockVarietyUpgrade blockVarietyUpgrade;
	private final ResetCooldownUpgrade resetCooldownUpgrade;

	public SkyMineUpgrades() {
		this(1, 1);
	}

	public SkyMineUpgrades(int blockVarietyLevel, int resetCooldownLevel) {
		this.blockVarietyUpgrade = new BlockVarietyUpgrade(blockVarietyLevel);
		this.resetCooldownUpgrade = new ResetCooldownUpgrade(resetCooldownLevel);
	}

	@Nonnull
	public SkyMineUpgrade getUpgrade(UpgradeType upgradeType) {
		return switch (upgradeType) {
			case BlockVariety -> blockVarietyUpgrade;
			case ResetCooldown -> resetCooldownUpgrade;
		};
	}

	@Nonnull
	public BlockVarietyUpgrade getBlockVarietyUpgrade() {
		return blockVarietyUpgrade;
	}

	@Nonnull
	public ResetCooldownUpgrade getResetCooldownUpgrade() {
		return resetCooldownUpgrade;
	}

	public static String parse(@Nonnull SkyMineUpgrades upgrades) {
		return upgrades.getBlockVarietyUpgrade().getLevel() + ":" + upgrades.getResetCooldownUpgrade().getLevel();
	}

	public static SkyMineUpgrades parse(@Nonnull String text) {
		String[] texts = text.split(":");

		int blockVarietyLevel = 1;
		int resetCooldownLevel = 1;

		try {
			blockVarietyLevel = Integer.parseInt(texts[0]);
			resetCooldownLevel = Integer.parseInt(texts[1]);
		} catch (NumberFormatException e) {
			// there's nothing to do
		}

		return new SkyMineUpgrades(blockVarietyLevel, resetCooldownLevel);
	}
}
