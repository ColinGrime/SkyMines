package com.github.scilldev.skymines;

import com.github.scilldev.skymines.structure.MineStructure;
import com.github.scilldev.skymines.upgrades.Upgrades;
import org.bukkit.Location;

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
	Upgrades getUpgrades();

	void reset();
}
