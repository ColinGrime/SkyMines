package me.colingrimes.skymines.skymine.structure.behavior;

import me.colingrimes.skymines.skymine.structure.material.MaterialType;
import me.colingrimes.skymines.skymine.structure.region.Region;
import org.bukkit.World;

import javax.annotation.Nonnull;

public interface BuildBehavior {

	/**
	 * Checks if the area is all air.
	 * @param world any world
	 * @param region any type of region
	 * @return true if the area is clear
	 */
	boolean isClear(@Nonnull World world, @Nonnull Region region);

	/**
	 * @param world world that the region is in
	 * @param region any type of region
	 * @param type material type
	 */
	default void build(@Nonnull World world, @Nonnull Region region, @Nonnull MaterialType type) {
		build(world, region, type, true);
	}

	/**
	 * @param world world that the region is in
	 * @param region any type of region
	 * @param type material type
	 * @param replaceBlocks true if blocks should be replaced
	 */
	void build(@Nonnull World world, @Nonnull Region region, @Nonnull MaterialType type, boolean replaceBlocks);
}
