package com.github.colingrime.skymines.upgrades.types;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public abstract class SkyMineUpgrade {

	private final SkyMines plugin;
	private final Economy econ;
	private int level;

	public SkyMineUpgrade(SkyMines plugin, int level) {
		this.plugin = plugin;
		this.econ = plugin.getDependencyManager().getEcon();
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

	public boolean hasPermission(Player player) {
		String permission = "skymines.upgrades." + Utils.unformat(getType().name());
		String negation = "-" + permission;

		// check for the whole type
		if (player.hasPermission(negation)) {
			return player.hasPermission(permission);
		}

		// check for a specific level
		else if (player.hasPermission(negation + "." + (level + 1))) {
			return player.hasPermission(permission + "." + (level + 1));
		}

		// no permission negation is happening, so player has permission
		return true;
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
		if (econ.getBalance(player) >= cost) {
			econ.withdrawPlayer(player, cost);
			level++;
			return true;
		}

		return false;
	}

	/**
	 * @return UpgradeType of the upgrade
	 */
	public abstract UpgradeType getType();

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
