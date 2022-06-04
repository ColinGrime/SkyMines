package com.github.colingrime.skymines.upgrades.paths;

import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.utils.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class UpgradePath<T> {

	private final Map<Integer, Double> costs = new HashMap<>();
	private final Map<Integer, T> upgrades = new HashMap<>();
	private int maxLevel;

	private final String name;
	private final ConfigurationSection sec;

	public UpgradePath(String name, ConfigurationSection sec) throws UpgradePathException {
		this.name = name;
		this.sec = sec;
		parseLevelData();
	}

	private void parseLevelData() throws UpgradePathException {
		if (sec == null) {
			throw new UpgradePathException("Failed to parse the upgrades.yml file (see: " + name + ")");
		}

		for (String levelPath : sec.getKeys(false)) {
			if (levelPath.equalsIgnoreCase("default")) {
				upgrades.put(1, parseUpgradeData(sec, levelPath));
				continue;
			} else if (!NumberUtils.isDigit(levelPath)) {
				continue;
			}

			int level = Integer.parseInt(levelPath);
			costs.put(level, parseCostData(levelPath));
			upgrades.put(level, parseUpgradeData(sec, levelPath));
		}

		maxLevel = Collections.max(upgrades.keySet());
	}

	private double parseCostData(String level) throws UpgradePathException {
		double cost = sec.getDouble(level + ".cost");
		if (cost == 0) {
			throw new UpgradePathException("Failed to parse the cost data or cost is $0 (see: " + name + ")");
		}
		return cost;
	}

	/**
	 * @param sec the configuration section that has parsable upgrade data
	 * @param level the level to check in the configuration section
	 * @return parsed data
	 */
	protected abstract T parseUpgradeData(ConfigurationSection sec, String level) throws UpgradePathException;

	/**
	 * @return UpgradeType of the path
	 */
	public abstract UpgradeType getUpgradeType();

	public Map<Integer, Double> getCosts() {
		return costs;
	}

	public Map<Integer, T> getUpgrades() {
		return upgrades;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public String getName() {
		return name;
	}
}
