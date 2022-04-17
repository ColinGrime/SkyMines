package com.github.colingrime.skymines.structure.behavior;

import com.github.colingrime.skymines.structure.material.MaterialType;
import com.github.colingrime.skymines.structure.region.Region;
import org.bukkit.World;

public interface BuildBehavior {

	/**
	 * Checks if the area is all air.
	 * @param world any world
	 * @param region any type of region
	 * @return true if the area is clear
	 */
	boolean isClear(World world, Region region);

	/**
	 * @param world world that the region is in
	 * @param region any type of region
	 * @param type material type
	 */
	default void build(World world, Region region, MaterialType type) {
		build(world, region, type, true);
	}

	/**
	 * @param world world that the region is in
	 * @param region any type of region
	 * @param type material type
	 * @param replaceBlocks true if blocks should be replaced
	 */
	void build(World world, Region region, MaterialType type, boolean replaceBlocks);
}
