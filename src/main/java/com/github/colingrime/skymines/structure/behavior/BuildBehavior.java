package com.github.colingrime.skymines.structure.behavior;

import com.github.colingrime.config.BlockVariety;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.List;

public interface BuildBehavior {

	/**
	 * Checks if the area is all air.
	 * @param world any world
	 * @param vectors list of vectors to check
	 * @return true if the area is clear
	 */
	boolean isClear(World world, List<Vector> vectors);

	/**
	 * Builds the structure given the material for each block.
	 * @param world any world
	 * @param vectors list of vectors to change
	 * @param type material to change each block into
	 */
	void build(World world, List<Vector> vectors, Material type);

	/**
	 * Builds the structure given the material for each block.
	 * @param world any world
	 * @param vectors list of vectors to change
	 * @param blockVariety variety of block types
	 */
	void build(World world, List<Vector> vectors, BlockVariety blockVariety);
}
