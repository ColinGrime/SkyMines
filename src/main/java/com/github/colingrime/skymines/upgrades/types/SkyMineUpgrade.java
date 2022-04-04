package com.github.colingrime.skymines.upgrades.types;

import com.github.colingrime.SkyMines;
import org.bukkit.entity.Player;

public abstract class SkyMineUpgrade {

	private final SkyMines plugin;
	private int level;

	public SkyMineUpgrade(SkyMines plugin, int level) {
		this.plugin = plugin;
		this.level = level;
	}

	protected SkyMines getPlugin() {
		return plugin;
	}

	public int getLevel() {
		return level;
	}

	public boolean canBeUpgraded() {
		return getMaxLevel() > getLevel();
	}

	/**
	 * Increases the level of the upgrade by 1.
	 * In doing so, {@link SkyMineUpgrade#getCost(int)} is deducted from {@code player}'s account.
	 * Returns false if the {@code player} doesn't have the funds required.
	 *
	 * @param player the player buying the upgrade
	 * @return true if the upgrade was sucessfully leveled up
	 */
	public boolean levelUp(Player player) {
		double cost = getCost(level + 1);
		if (plugin.getEconomy().getBalance(player) >= cost) {
			plugin.getEconomy().withdrawPlayer(player, cost);
			level++;
			return true;
		}

		return false;
	}

	/**
	 * @return max level of the upgrade
	 */
	public abstract int getMaxLevel();

	/**
	 * @param level any level
	 * @return amount of money required to level up (from the previous level)
	 */
	public abstract double getCost(int level);
}
