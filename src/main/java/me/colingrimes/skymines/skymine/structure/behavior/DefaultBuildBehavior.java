package me.colingrimes.skymines.skymine.structure.behavior;

import me.colingrimes.midnight.geometry.Region;
import me.colingrimes.midnight.model.DeferredBlock;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.structure.material.MineMaterial;
import me.colingrimes.skymines.task.BuildTask;
import org.bukkit.Material;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;

public class DefaultBuildBehavior implements BuildBehavior {

	@Override
	public boolean canBuild(@Nonnull Region region) {
		return region.handler((x, y, z) -> {
			Material type = region.getWorld().getBlockAt(x, y, z).getType();
			if (type.isAir()) {
				return true;
			}

			boolean tryOverride = Settings.OPTION_SKYMINE_OVERRIDE_TRANSPARENT_BLOCKS.get();

			// checks for transparent blocks
			return tryOverride && !type.isOccluding() && !type.name().endsWith("CHEST");
		});
	}

	@Override
	public int build(@Nonnull Region region, @Nonnull MineMaterial material, boolean replaceBlocks) {
		int[] blocksChanged = {0};
		BuildTask buildTask = new BuildTask();
		region.handler((x, y, z) -> {
			Block block = region.getWorld().getBlockAt(x, y, z);
			if (!replaceBlocks && block.getType() != Material.AIR) {
				return;
			}

			Material change = material.get();
			if (block.getType() != change) {
				buildTask.getBlocksToPlace().add(new DeferredBlock(block, material.get()));
				blocksChanged[0]++;
			}
		});

		if (!buildTask.getBlocksToPlace().isEmpty()) {
			Scheduler.sync().runRepeating(buildTask, 0L, 1L);
		}

		return blocksChanged[0];
	}
}
