package com.github.colingrime.skymines.structure.behavior;

import com.github.colingrime.config.BlockVariety;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;

public interface BuildBehavior {

	/**
	 * Builds the structure given the material for each block.
	 * @param blocks list of blocks to change
	 * @param type material to change each block into
	 */
	void build(List<Block> blocks, Material type);

	/**
	 * Builds the structure given the material for each block.
	 * @param blocks list of blocks to change
	 * @param blockVariety variety of block types
	 */
	void build(List<Block> blocks, BlockVariety blockVariety);
}
