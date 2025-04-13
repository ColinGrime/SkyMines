package me.colingrimes.skymines.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;
import me.colingrimes.skymines.skymine.upgrade.data.CompositionData;
import me.colingrimes.skymines.skymine.upgrade.data.ResetCooldownData;
import me.colingrimes.skymines.skymine.upgrade.data.UpgradeData;

import javax.annotation.Nonnull;
import java.util.Map;

import static me.colingrimes.midnight.config.option.OptionFactory.keys;

@Configuration("upgrades.yml")
public interface Upgrades {

	Option<Map<String, CompositionData>> COMPOSITION = keys("composition", CompositionData::new);
	Option<Map<String, ResetCooldownData>> RESET_COOLDOWN = keys("reset-cooldown", ResetCooldownData::new);

	/**
	 * Gets the {@link UpgradeData} for the specified type and identifier.
	 *
	 * @param type the upgrade type
	 * @param identifier the upgrade identifier
	 * @return the upgrade data
	 */
	@Nonnull
	static UpgradeData getUpgradeData(@Nonnull UpgradeType type, @Nonnull String identifier) {
		return switch (type) {
			case Composition -> COMPOSITION.get().get(identifier);
			case ResetCooldown -> RESET_COOLDOWN.get().get(identifier);
		};
	}
}
