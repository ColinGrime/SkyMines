package me.colingrimes.skymines.skymine.structure.behavior;

import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.structure.material.MaterialType;
import me.colingrimes.skymines.skymine.structure.model.BlockInfo;
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
	public void build(@Nonnull World world, @Nonnull Region region, @Nonnull MaterialType type, boolean replaceBlocks) {
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
