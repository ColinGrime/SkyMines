package me.colingrimes.skymines.skymine.upgrade.type;

import me.colingrimes.skymines.config.Upgrades;
import me.colingrimes.skymines.skymine.structure.material.MineMaterial;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;

import javax.annotation.Nonnull;

public class CompositionUpgrade extends SkyMineUpgrade {

	public CompositionUpgrade(@Nonnull String identifier, int level) {
		super(UpgradeType.Composition, identifier, level);
	}

	@Nonnull
	public MineMaterial getComposition() {
		return Upgrades.COMPOSITION.get().get(getUpgradeIdentifier()).getComposition(level);
	}
}
