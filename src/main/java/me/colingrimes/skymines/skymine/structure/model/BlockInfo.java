package me.colingrimes.skymines.skymine.structure.model;

import org.bukkit.Material;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;

public class BlockInfo {

	private final Block block;
	private final Material type;

	public BlockInfo(@Nonnull Block block, @Nonnull Material type) {
		this.block = block;
		this.type = type;
	}

	@Nonnull
	public Block getBlock() {
		return block;
	}

	@Nonnull
	public Material getType() {
		return type;
	}
}
