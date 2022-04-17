package com.github.colingrime.skymines.structure.behavior;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.structure.material.MaterialType;
import com.github.colingrime.skymines.structure.model.BlockInfo;
import com.github.colingrime.skymines.structure.region.Region;
import com.github.colingrime.tasks.BuildTask;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class DefaultBuildBehavior implements BuildBehavior {

	@Override
	public boolean isClear(World world, Region region) {
		return region.handler((x, y, z) -> world.getBlockAt(x, y, z).getType() == Material.AIR);
	}

	@Override
	public void build(World world, Region region, MaterialType type, boolean replaceBlocks) {
		BuildTask buildTask = new BuildTask();
		region.handler((x, y, z) -> {
			Block block = world.getBlockAt(x, y, z);
			if (replaceBlocks || block.getType() == Material.AIR) {
				buildTask.getBlocksToPlace().add(new BlockInfo(block, type.get()));
			}
			return true;
		});

		if (!buildTask.getBlocksToPlace().isEmpty()) {
			buildTask.runTaskTimer(SkyMines.getInstance(), 0L, 1L);
		}
	}
}
