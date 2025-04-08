package me.colingrimes.skymines.skymine.upgrade.type;

import me.colingrimes.skymines.config.Upgrades;
import me.colingrimes.skymines.skymine.structure.material.MineMaterial;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;

import javax.annotation.Nonnull;

public class CompositionUpgrade extends SkyMineUpgrade {

	public CompositionUpgrade(int level) {
		this("default", level);
	}

	public CompositionUpgrade(@Nonnull String name, int level) {
		super(UpgradeType.Composition, name, level);
	}

	@Override
	public int getMaxLevel() {
		return Upgrades.COMPOSITION.get().get(name).getMaxLevel();
	}

	@Override
	public double getCost(int level) {
		return Upgrades.COMPOSITION.get().get(name).getCosts().getOrDefault(level, -1D);
	}

	@Nonnull
	public MineMaterial getComposition() {
		return Upgrades.COMPOSITION.get().get(name).getComposition().get(level);
	}
}
