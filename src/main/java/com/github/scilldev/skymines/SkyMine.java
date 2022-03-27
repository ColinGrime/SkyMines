package com.github.scilldev.skymines;

import com.github.scilldev.skymines.upgrades.Upgrades;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;
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
	 * @return home of the sky mine
	 */
	Location getHome();

	/**
	 * @return blocks that make up the sky mine
	 */
	List<Block> getBlocks();

	/**
	 * @return upgrades of the sky mine
	 */
	Upgrades getUpgrades();

	void reset();
}
