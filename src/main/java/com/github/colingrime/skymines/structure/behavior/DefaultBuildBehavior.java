package com.github.colingrime.skymines.structure.behavior;

import com.github.colingrime.config.BlockVariety;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;

public class DefaultBuildBehavior implements BuildBehavior {

	@Override
	public void build(List<Block> blocks, Material type) {
		// will change later, but right now it's default Bukkit#setType
		for (Block block : blocks) {
			block.setType(type);
		}
	}

	@Override
	public void build(List<Block> blocks, BlockVariety blockVariety) {
		for (Block block : blocks) {
			block.setType(blockVariety.getRandom());
		}
	}
}
