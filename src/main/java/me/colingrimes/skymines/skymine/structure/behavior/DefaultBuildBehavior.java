package me.colingrimes.skymines.skymine.structure.behavior;

import me.colingrimes.midnight.model.DeferredBlock;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.structure.material.MineMaterial;
import me.colingrimes.skymines.skymine.structure.region.Region;
import me.colingrimes.skymines.task.BuildTask;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;

public class DefaultBuildBehavior implements BuildBehavior {

	@Override
	public boolean isClear(@Nonnull World world, @Nonnull Region region) {
		return region.handler((x, y, z) -> {
			Material type = world.getBlockAt(x, y, z).getType();
			if (type.isAir()) {
				return true;
			}

			boolean tryOverride = Settings.OPTIONS_OVERRIDE_TRANSPARENT_BLOCKS.get();

			// checks for transparent blocks
			return tryOverride && !type.isOccluding() && !type.name().contains("CHEST");
		});
	}

	@Override
	public void build(@Nonnull World world, @Nonnull Region region, @Nonnull MineMaterial material, boolean replaceBlocks) {
		BuildTask buildTask = new BuildTask();
		region.handler((x, y, z) -> {
			Block block = world.getBlockAt(x, y, z);
			if (replaceBlocks || block.getType() == Material.AIR) {
				buildTask.getBlocksToPlace().add(new DeferredBlock(block, material.get()));
			}
		});

		if (!buildTask.getBlocksToPlace().isEmpty()) {
			Scheduler.sync().runRepeating(buildTask, 0L, 1L);
		}
	}
}
