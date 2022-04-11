package com.github.colingrime.skymines;

import com.github.colingrime.skymines.structure.MineStructure;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.skymines.upgrades.types.BlockVarietyUpgrade;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface SkyMine {

	/**
	 * @return UUID of mine
	 */
	UUID getUUID();

	/**
	 * @return UUID of the owner
	 */
	UUID getOwner();

	/**
	 * @return gets the ID of the skymine
	 */
	int getId();

	/**
	 * @return structure of the sky mine
	 */
	MineStructure getStructure();

	/**
	 * @return home of the sky mine
	 */
	Location getHome();

	/**
	 * @return upgrades of the sky mine
	 */
	SkyMineUpgrades getUpgrades();

	/**
	 * Resets the skymine depending on the {@link BlockVarietyUpgrade#getLevel()}.
	 */
	void reset();

	/**
	 * Picks the skymine up. Only works if it's the owner who is requesting pickup.
	 * It will also return false if the owner's inventory is full.
	 *
	 * @param player any player
	 * @return true if pickup was successful
	 */
	boolean pickup(Player player);
}
