package com.github.colingrime.skymines.upgrades;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.upgrades.types.BlockVarietyUpgrade;
import com.github.colingrime.skymines.upgrades.types.ResetCooldownUpgrade;
import com.github.colingrime.skymines.upgrades.types.SkyMineUpgrade;

public class SkyMineUpgrades {

	private final BlockVarietyUpgrade blockVarietyUpgrade;
	private final ResetCooldownUpgrade resetCooldownUpgrade;

	public SkyMineUpgrades(SkyMines plugin) {
		this(plugin, 1, 1);
	}

	public SkyMineUpgrades(SkyMines plugin, int blockVarietyLevel, int resetCooldownLevel) {
		this.blockVarietyUpgrade = new BlockVarietyUpgrade(plugin, blockVarietyLevel);
		this.resetCooldownUpgrade = new ResetCooldownUpgrade(plugin, resetCooldownLevel);
	}

	public SkyMineUpgrade getUpgrade(UpgradeType upgradeType) {
		switch (upgradeType) {
			case BlockVariety:
				return blockVarietyUpgrade;
			case ResetCooldown:
				return resetCooldownUpgrade;
		}

		return null;
	}

	public BlockVarietyUpgrade getBlockVarietyUpgrade() {
		return blockVarietyUpgrade;
	}

	public ResetCooldownUpgrade getResetCooldownUpgrade() {
		return resetCooldownUpgrade;
	}

	public static String parse(SkyMineUpgrades upgrades) {
		return upgrades.getBlockVarietyUpgrade().getLevel() + ":" + upgrades.getResetCooldownUpgrade().getLevel();
	}

	public static SkyMineUpgrades parse(String text) {
		String[] texts = text.split(":");

		int blockVarietyLevel = 1;
		int resetCooldownLevel = 1;

		try {
			blockVarietyLevel = Integer.parseInt(texts[0]);
			resetCooldownLevel = Integer.parseInt(texts[1]);
		} catch (NumberFormatException ex) {
			// there's nothing to do
		}

		return new SkyMineUpgrades(SkyMines.getInstance(), blockVarietyLevel, resetCooldownLevel);
	}
}
