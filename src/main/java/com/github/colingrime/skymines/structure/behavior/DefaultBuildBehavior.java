package com.github.colingrime.skymines.structure.behavior;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Map;

public class DefaultBuildBehavior implements BuildBehavior {

	@Override
	public void build(Map<Block, Material> blocksToChange) {
		// will change later, but right now it's default Bukkit#setType
		for (Map.Entry<Block, Material> blockToChange : blocksToChange.entrySet()) {
			blockToChange.getKey().setType(blockToChange.getValue());
		}
	}
}
