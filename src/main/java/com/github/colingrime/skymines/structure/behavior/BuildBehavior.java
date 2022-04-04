package com.github.colingrime.skymines.structure.behavior;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Map;

public interface BuildBehavior {

	/**
	 * Builds the structure given the material for each block.
	 * @param blocksToChange Map of what material each block should change into
	 */
	void build(Map<Block, Material> blocksToChange);
}
