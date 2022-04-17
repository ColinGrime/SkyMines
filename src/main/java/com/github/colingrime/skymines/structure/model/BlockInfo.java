package com.github.colingrime.skymines.structure.model;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockInfo {

	private final Block block;
	private final Material type;

	public BlockInfo(Block block, Material type) {
		this.block = block;
		this.type = type;
	}

	public Block getBlock() {
		return block;
	}

	public Material getType() {
		return type;
	}
}
