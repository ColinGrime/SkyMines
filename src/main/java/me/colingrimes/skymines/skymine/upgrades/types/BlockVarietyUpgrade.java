package me.colingrimes.skymines.skymine.upgrades.types;

import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.structure.material.MaterialVariety;
import me.colingrimes.skymines.skymine.upgrades.UpgradeType;

import javax.annotation.Nonnull;

public class BlockVarietyUpgrade extends SkyMineUpgrade {

	public BlockVarietyUpgrade(int level) {
		super(level);
	}

	@Override
	@Nonnull
	public UpgradeType getType() {
		return UpgradeType.BlockVariety;
	}

	@Override
	public int getMaxLevel() {
		return Settings.UPGRADES_BLOCK_VARIETY_MAX_LEVEL.get();
	}

	@Override
	public double getCost(int level) {
		return Settings.UPGRADES_BLOCK_VARIETY_COSTS.get().getOrDefault(level, -1D);
	}

	@Nonnull
	public MaterialVariety getBlockVariety() {
		return Settings.UPGRADES_BLOCK_VARIETY.get().get(getLevel());
	}
}
