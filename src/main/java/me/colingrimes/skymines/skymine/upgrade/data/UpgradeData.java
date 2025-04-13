package me.colingrimes.skymines.skymine.upgrade.data;

import me.colingrimes.midnight.config.util.Configs;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public abstract class UpgradeData {

	private final int maxLevel;
	private final Map<Integer, Double> costs;

	public UpgradeData(@Nonnull ConfigurationSection section) {
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
