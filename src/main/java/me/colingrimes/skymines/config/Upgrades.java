package me.colingrimes.skymines.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;
import me.colingrimes.skymines.skymine.upgrade.data.UpgradeData;

import javax.annotation.Nullable;
import java.util.Map;

import static me.colingrimes.midnight.config.option.OptionFactory.keys;

@Configuration("upgrades.yml")
public interface Upgrades {

	Option<Map<String, UpgradeData>> COMPOSITION = keys("composition", (section) -> UpgradeData.of(UpgradeType.Composition, section));
	Option<Map<String, UpgradeData>> RESET_COOLDOWN = keys("reset-cooldown", (section) -> UpgradeData.of(UpgradeType.ResetCooldown, section));

	/**
	 * Gets the {@link UpgradeData} for the specified type and identifier.
	 *
	 * @param type the upgrade type
	 * @param identifier the upgrade identifier
	 * @return the upgrade data
	 */
	@Nullable
	static UpgradeData getUpgradeData(@Nullable UpgradeType type, @Nullable String identifier) {
		if (type == null || identifier == null || identifier.isEmpty()) {
			return null;
		}
		return switch (type) {
			case Composition -> COMPOSITION.get().get(identifier);
			case ResetCooldown -> RESET_COOLDOWN.get().get(identifier);
		};
	}
}
