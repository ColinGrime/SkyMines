package me.colingrimes.skymines.skymine.upgrade.data;

import me.colingrimes.midnight.config.util.Configs;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public abstract class UpgradeData {

	private final int maxLevel;
	private final Map<Integer, Double> costs;

	/**
	 * Constructs the {@link UpgradeData} with the given configuration section and key.
	 * It will generate a {@link CompositionData} object if the key is {@code composition}
	 * or a {@link ResetCooldownData} object if the key is {@code reset-cooldown}.
	 * <p>
	 * The data will also be validated corresponding to the upgrade type to ensure the
	 * upgrade data can be used properly. It will return {@code null} if it's invalid.
	 *
	 * @param section the configuration section
	 * @param key the key
	 * @return the upgrade data or null if invalid
	 */
	@Nullable
	public static UpgradeData of(@Nonnull ConfigurationSection section, @Nonnull String key) {
		return switch (key) {
			case "composition" -> CompositionData.of(section);
			case "reset-cooldown" -> ResetCooldownData.of(section);
			default -> null;
		};
	}

	UpgradeData(@Nonnull ConfigurationSection section) {
		this.maxLevel = section.getKeys(false).size();
		this.costs = Configs.mapIntegerKeys(section, sec -> sec.getDouble("cost"));
	}

	/**
	 * Gets the max level of the upgrade.
	 *
	 * @return the max level
	 */
	public int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * Gets the cost to upgrade to the specified level.
	 *
	 * @param level the level
	 * @return the amount of money required to level up (from the previous level)
	 */
	public double getCost(int level) {
		return costs.getOrDefault(level, 0D);
	}

	/**
	 * Gets the lore of the current level.
	 *
	 * @param level the level
	 * @return the lore
	 */
	@Nonnull
	public abstract List<String> getLore(int level);
}
