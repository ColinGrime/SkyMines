package com.github.scilldev.skymines.upgrades;

import com.github.scilldev.utils.Utils;

public class SkyMineUpgrades {

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

	private int blockVarietyLevel;
	private int resetCooldownLevel;

	public SkyMineUpgrades() {
		this(1, 1);
	}

	public SkyMineUpgrades(int blockVarietyLevel, int resetCooldownLevel) {
		this.blockVarietyLevel = blockVarietyLevel;
		this.resetCooldownLevel = resetCooldownLevel;
	}

	public int getLevel(UpgradeType upgradeType) {
		switch (upgradeType) {
			case BlockVariety:
				return blockVarietyLevel;
			case ResetCooldown:
				return resetCooldownLevel;
		}

		return -1;
	}

	public int getBlockVarietyLevel() {
		return blockVarietyLevel;
	}

	public int getResetCooldownLevel() {
		return resetCooldownLevel;
	}

	public void increaseLevel(UpgradeType upgradeType) {
		switch (upgradeType) {
			case BlockVariety:
				blockVarietyLevel++;
				break;
			case ResetCooldown:
				resetCooldownLevel++;
				break;
		}
	}
}
