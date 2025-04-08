package me.colingrimes.skymines.skymine.upgrade.type;

import me.colingrimes.skymines.config.Upgrades;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;

import javax.annotation.Nonnull;
import java.time.Duration;

public class ResetCooldownUpgrade extends SkyMineUpgrade {

	public ResetCooldownUpgrade(int level) {
		this("default", level);
	}

	public ResetCooldownUpgrade(@Nonnull String name, int level) {
		super(UpgradeType.ResetCooldown, name, level);
	}

	@Override
	public int getMaxLevel() {
		return Upgrades.RESET_COOLDOWN.get().get(name).getMaxLevel();
	}

	@Override
	public double getCost(int level) {
		return Upgrades.RESET_COOLDOWN.get().get(name).getCosts().getOrDefault(level, -1D);
	}

	@Nonnull
	public Duration getResetCooldown() {
		return Upgrades.RESET_COOLDOWN.get().get(name).getResetCooldown().get(level);
	}
}
