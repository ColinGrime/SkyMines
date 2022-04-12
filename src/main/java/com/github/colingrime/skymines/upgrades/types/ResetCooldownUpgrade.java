package com.github.colingrime.skymines.upgrades.types;

import com.github.colingrime.SkyMines;

import java.util.Map;

public class ResetCooldownUpgrade extends SkyMineUpgrade {

	public ResetCooldownUpgrade(SkyMines plugin, int level) {
		super(plugin, level);
	}

	@Override
	public int getMaxLevel() {
		return getPlugin().getSettings().getMaxResetCooldown();
	}

	@Override
	public double getCost(int level) {
		Map<Integer, Double> costs = getPlugin().getSettings().getCostsResetCooldown();
		if (costs.get(level) == null) {
			return -1;
		}
		return costs.get(level);
	}

	/**
	 * @return reset cooldown depending on the upgrade's level
	 */
	public double getResetCooldown() {
		return getPlugin().getSettings().getUpgradesResetCooldown().get(getLevel());
	}
}
