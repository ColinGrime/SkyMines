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
}
