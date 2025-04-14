package me.colingrimes.skymines.skymine.upgrade.type;

import me.colingrimes.skymines.skymine.upgrade.UpgradeType;
import me.colingrimes.skymines.skymine.upgrade.data.ResetCooldownData;

import javax.annotation.Nonnull;
import java.time.Duration;

public class ResetCooldownUpgrade extends SkyMineUpgrade {

	public ResetCooldownUpgrade(@Nonnull String identifier, int level) {
		super(UpgradeType.ResetCooldown, identifier, level);
	}

	@Nonnull
	public Duration getResetCooldown() {
		return ((ResetCooldownData) getUpgradeData()).getResetCooldown(level);
	}
}
