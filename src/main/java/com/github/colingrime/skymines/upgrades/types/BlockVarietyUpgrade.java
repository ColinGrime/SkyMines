package com.github.colingrime.skymines.upgrades.types;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.structure.material.MaterialVariety;
import com.github.colingrime.skymines.upgrades.UpgradeType;

import java.util.Map;

public class BlockVarietyUpgrade extends SkyMineUpgrade {

	public BlockVarietyUpgrade(SkyMines plugin, int level) {
		super(plugin, level);
	}

	@Override
	public UpgradeType getType() {
		return UpgradeType.BlockVariety;
	}

	@Override
	public int getMaxLevel() {
		return getPlugin().getSettings().getMaxBlockVariety();
	}

	@Override
	public double getCost(int level) {
		Map<Integer, Double> costs = getPlugin().getSettings().getCostsBlockVariety();
		if (costs.get(level) == null) {
			return -1;
		}
		return costs.get(level);
	}

	/**
	 * @return BlockVariety object depending on the upgrade's level
	 */
	public MaterialVariety getBlockVariety() {
		return getPlugin().getSettings().getUpgradesBlockVariety().get(getLevel());
	}
}
