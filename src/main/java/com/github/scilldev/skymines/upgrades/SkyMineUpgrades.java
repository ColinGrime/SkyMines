package com.github.scilldev.skymines.upgrades;

public class SkyMineUpgrades {

	public enum UpgradeType {
		BlockVariety,
		ResetCooldown,
		IslandLimit;

		public static UpgradeType getUpgradeType(String name) {
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
	private int islandLimitLevel;

	public SkyMineUpgrades() {
		this(1, 1, 1);
	}

	public SkyMineUpgrades(int blockVarietyLevel, int resetCooldownLevel, int islandLimitLevel) {
		this.blockVarietyLevel = blockVarietyLevel;
		this.resetCooldownLevel = resetCooldownLevel;
		this.islandLimitLevel = islandLimitLevel;
	}

	public int getLevel(UpgradeType upgradeType) {
		switch (upgradeType) {
			case BlockVariety:
				return blockVarietyLevel;
			case ResetCooldown:
				return resetCooldownLevel;
			case IslandLimit:
				return islandLimitLevel;
		}

		return -1;
	}

	public int getBlockVarietyLevel() {
		return blockVarietyLevel;
	}

	public int getResetCooldownLevel() {
		return resetCooldownLevel;
	}

	public int getIslandLimitLevel() {
		return islandLimitLevel;
	}
}
