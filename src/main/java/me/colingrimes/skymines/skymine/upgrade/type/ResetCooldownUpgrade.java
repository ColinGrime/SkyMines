package me.colingrimes.skymines.skymine.upgrade.type;

import me.colingrimes.skymines.config.Upgrades;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;

import javax.annotation.Nonnull;
import java.time.Duration;

public class ResetCooldownUpgrade extends SkyMineUpgrade {

	public ResetCooldownUpgrade(@Nonnull String identifier, int level) {
		super(UpgradeType.ResetCooldown, identifier, level);
	}

	@Override
	public int getMaxLevel() {
		return Upgrades.RESET_COOLDOWN.get().get(getUpgradeIdentifier()).getMaxLevel();
	}

	@Override
	public double getCost(int level) {
		return Upgrades.RESET_COOLDOWN.get().get(getUpgradeIdentifier()).getCosts().getOrDefault(level, -1D);
	}

	@Nonnull
	public Duration getResetCooldown() {
		return Upgrades.RESET_COOLDOWN.get().get(getUpgradeIdentifier()).getResetCooldown().get(level);
	}
}
