package me.colingrimes.skymines.skymine.upgrade.type;

import me.colingrimes.skymines.config.Upgrades;
import me.colingrimes.skymines.skymine.structure.material.MineMaterial;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;

import javax.annotation.Nonnull;

public class CompositionUpgrade extends SkyMineUpgrade {

	public CompositionUpgrade(@Nonnull String identifier, int level) {
		super(UpgradeType.Composition, identifier, level);
	}

	@Override
	public int getMaxLevel() {
		return Upgrades.COMPOSITION.get().get(getUpgradeIdentifier()).getMaxLevel();
	}

	@Override
	public double getCost(int level) {
		return Upgrades.COMPOSITION.get().get(getUpgradeIdentifier()).getCosts().getOrDefault(level, -1D);
	}

	@Nonnull
	public MineMaterial getComposition() {
		return Upgrades.COMPOSITION.get().get(getUpgradeIdentifier()).getComposition().get(level);
	}
}
