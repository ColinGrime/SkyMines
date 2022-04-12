package com.github.colingrime.skymines.structure.behavior;

import com.github.colingrime.config.BlockVariety;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Set;

// TODO find a way to refactor #blockInside
public interface BuildBehavior {

	/**
	 * Checks if the area is all air.
	 * @param world any world
	 * @param vectors list of vectors to check
	 * @return true if the area is clear
	 */
	boolean isClear(World world, Set<Vector> vectors);

	/**
	 * Builds the structure given the material for each block.
	 * @param world any world
	 * @param parameter list of vectors to change
	 * @param type material to change each block into
	 */
	void buildParameter(World world, Set<Vector> parameter, Material type);

	/**
	 * Builds the structure given the material for each block.
	 * @param world any world
	 * @param min minimum vector
	 * @param max maximum vector
	 * @param type material to change each block into
	 */
	void buildInside(World world, Vector min, Vector max, Material type);

	/**
	 * Builds the structure given the material for each block.
	 * @param world any world
	 * @param min minimum vector
	 * @param max maximum vector
	 * @param blockVariety variety of block types
	 */
	void buildInside(World world, Vector min, Vector max, BlockVariety blockVariety, boolean replaceBlocks);
}
