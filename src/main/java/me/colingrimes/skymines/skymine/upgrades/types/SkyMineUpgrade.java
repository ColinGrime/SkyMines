package me.colingrimes.skymines.skymine.upgrades.types;

import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.skymine.upgrades.UpgradeType;
import me.colingrimes.midnight.util.Common;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public abstract class SkyMineUpgrade {

	private int level;

	public SkyMineUpgrade(int level) {
		this.level = level;
	}

	/**
	 * Gets the type of upgrade.
	 *
	 * @return the type of upgrade
	 */
	@Nonnull
	public abstract UpgradeType getType();

	/**
	 * Gets the current level of the upgrade type.
	 *
	 * @return the level of the upgrade type
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the max level of the type.
	 *
	 * @return max level of the upgrade type
	 */
	public abstract int getMaxLevel();

	/**
	 * Gets the cost to upgrade to the specified level.
	 *
	 * @param level the level
	 * @return amount of money required to level up (from the previous level)
	 */
	public abstract double getCost(int level);

	/**
	 * Checks if this upgrade type can still be upgraded based on its current level and the max level of its type.
	 *
	 * @return true if it can be further upgraded
	 */
	public boolean canBeUpgraded() {
		return getMaxLevel() > getLevel();
	}

	/**
	 * Increases the level of the upgrade by 1.
	 * In doing so, {@link SkyMineUpgrade#getCost(int)} is deducted from {@code player}'s account.
	 * Returns false if the {@code player} doesn't have the funds required.
	 *
	 * @param player the player buying the upgrade
	 * @return true if the upgrade was sucessfully leveled up, false if the player had insufficient funds
	 */
	public boolean levelUp(@Nonnull Player player) {
		double cost = getCost(level + 1);
		if (Common.economy().getBalance(player) >= cost) {
			Common.economy().withdrawPlayer(player, cost);
			level++;
			return true;
		}
		return false;
	}

	/**
	 * Checks if the specified player has permission to upgrade.
	 * This method has special permission handling which checks the negation of permissions.
	 * <p>
	 * It will first check if the whole type has been negated (e.g. -skymines.upgrades.block-variety).
	 * If it is negated, then the player must have the upgrade permission to upgrade it (e.g. skymines.upgrades.block-variety).
	 * <p>
	 * It will then check if a specific level has been negated (e.g. -skymines.upgrades.block-variety.2).
	 * If it is negated, then the player must have the upgrade level permission to upgrade it (e.g. skymines.upgrades.block-variety.2).
	 *
	 * @param player the player to check
	 * @return true if the player can upgrade this type
	 */
	public boolean hasPermission(@Nonnull Player player) {
		String permission = "skymines.upgrades." + Text.unformat(getType().name()).replace("_", "-");
		String negation = "-" + permission;
		if (player.hasPermission(negation)) {
			return player.hasPermission(permission);
		}
		if (player.hasPermission(negation + "." + (level + 1))) {
			return player.hasPermission(permission + "." + (level + 1));
		}
		return true;
	}
}
