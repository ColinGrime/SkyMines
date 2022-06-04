package com.github.colingrime.skymines.upgrades.factory;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.skymines.upgrades.paths.UpgradePath;
import com.github.colingrime.utils.StringUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Instantiated in the {@link UpgradeFactory} class.
 * @param <T> the Object that refers to the upgrade's value
 *           (e.g. POJO object holding STONE material to represent block variety upgrade)
 */
public class SkyMineUpgrade<T> {

	private final T defaultUpgrade;
	private final String upgradeName;
	private int level;

	SkyMineUpgrade(T defaultUpgrade, String upgradeName) {
		this(defaultUpgrade, upgradeName, 0);
	}

	SkyMineUpgrade(T defaultUpgrade, String upgradeName, int level) {
		this.defaultUpgrade = defaultUpgrade;
		this.upgradeName = upgradeName;
		this.level = level;
	}

	/**
	 * @return UpgradeType of the upgrade
	 */
	public UpgradeType getUpgradeType() {
		if (getPath().isPresent()) {
			return getPath().get().getUpgradeType();
		} else {
			return null;
		}
	}

	/**
	 * @return name of the upgrade
	 */
	public String getUpgradeName() {
		return upgradeName;
	}

	/**
	 * @return current level of the upgrade
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return max level of the upgrade
	 */
	public int getMaxLevel() {
		if (getPath().isPresent()) {
			return getPath().get().getMaxLevel();
		} else {
			return 0;
		}
	}

	/**
	 * @return true if the upgrade can still be upgraded
	 */
	public boolean canBeUpgraded() {
		return getMaxLevel() > getLevel();
	}

	/**
	 * Increases the level of the upgrade by 1.
	 * In doing so, {@link #getCost(int)} is deducted from {@code player}'s account.
	 * Returns false if the {@code player} doesn't have the funds required.
	 *
	 * @param player the player buying the upgrade
	 * @return true if the upgrade was sucessfully leveled up
	 */
	public boolean levelUp(Player player) {
		double cost = getCost(level + 1);

		Economy econ = SkyMines.getInstance().getDependencyManager().getEcon();
		if (econ.getBalance(player) >= cost) {
			econ.withdrawPlayer(player, cost);
			level++;
			return true;
		}

		return false;
	}

	/**
	 * @param player any player
	 * @return true if the player has permission to upgrade the mine
	 */
	public boolean hasPermission(Player player) {
		String permission = "skymines.upgrades." + StringUtils.unformat(getUpgradeType().name());
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
	 * @param level any level
	 * @return amount of money required to level up (from the previous level)
	 */
	public double getCost(int level) {
		if (getPath().isPresent()) {
			return getPath().get().getCosts().get(level);
		} else {
			return -1;
		}
	}

	/**
	 * @return the upgrade value
	 */
	@SuppressWarnings("unchecked")
	public T getUpgrade() {
		return getPath().map(u -> u.getUpgrades().get(level))
				.filter(defaultUpgrade.getClass()::isInstance)
				.map(defaultUpgrade.getClass()::cast).stream().findAny()
				.map(o -> (T) o).orElse(defaultUpgrade);
	}

	/**
	 * @return Optional containing UpgradePath if available
	 */
	private Optional<UpgradePath<?>> getPath() {
		return SkyMines.getInstance().getUpgrades().getUpgradePath(upgradeName);
	}

	@Override
	public String toString() {
		return getUpgradeType() + ":" + upgradeName + ":" + level;
	}
}
