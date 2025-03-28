package me.colingrimes.skymines.skymine.upgrades.types;

import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.upgrades.UpgradeType;

import javax.annotation.Nonnull;
import java.time.Duration;

public class ResetCooldownUpgrade extends SkyMineUpgrade {

	public ResetCooldownUpgrade(@Nonnull SkyMines plugin, int level) {
		super(plugin, level);
	}

	@Override
	@Nonnull
	public UpgradeType getType() {
		return UpgradeType.ResetCooldown;
	}

	@Override
	public int getMaxLevel() {
		return Settings.UPGRADES_RESET_COOLDOWN_MAX_LEVEL.get();
	}

	@Override
	public double getCost(int level) {
		return Settings.UPGRADES_RESET_COOLDOWN_COSTS.get().getOrDefault(level, -1D);
	}

	/**
	 * @return reset cooldown depending on the upgrade's level
	 */
	@Nonnull
	public Duration getResetCooldown() {
		return Settings.UPGRADES_RESET_COOLDOWN.get().get(getLevel());
	}
}
