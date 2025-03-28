package me.colingrimes.skymines.skymine.upgrades.types;

import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.skymine.upgrades.UpgradeType;
import me.colingrimes.midnight.util.Common;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public abstract class SkyMineUpgrade {

	private final SkyMines plugin;
	private int level;

	public SkyMineUpgrade(@Nonnull SkyMines plugin, int level) {
		this.plugin = plugin;
		this.level = level;
	}

	@Nonnull
	protected SkyMines getPlugin() {
		return plugin;
	}

	public int getLevel() {
		return level;
	}

	public boolean canBeUpgraded() {
		return getMaxLevel() > getLevel();
	}

	public boolean hasPermission(@Nonnull Player player) {
		String permission = "skymines.upgrades." + Text.unformat(getType().name());
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
	 * @return UpgradeType of the upgrade
	 */
	@Nonnull
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
