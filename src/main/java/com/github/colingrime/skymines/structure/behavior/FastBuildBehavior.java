package com.github.colingrime.skymines.structure.behavior;

import com.fastasyncworldedit.core.FaweAPI;
import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.structure.material.MaterialType;
import com.github.colingrime.skymines.structure.region.Region;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Bukkit;

public class FastBuildBehavior implements BuildBehavior {

	@Override
	public boolean isClear(org.bukkit.World world, Region region) {
		World faweWorld = FaweAPI.getWorld(world.getName());
		try (EditSession es = WorldEdit.getInstance().newEditSession(faweWorld)) {
			return region.handler((x, y, z) -> es.getBlock(x, y, z).isAir());
		}
	}

	@Override
	public void build(org.bukkit.World world, Region region, MaterialType type, boolean replaceBlocks) {
		Bukkit.getScheduler().runTaskAsynchronously(SkyMines.getInstance(), () -> {
			World faweWorld = FaweAPI.getWorld(world.getName());
			try (EditSession es = WorldEdit.getInstance().newEditSession(faweWorld)) {
				region.handler((x, y, z) -> {
					if (replaceBlocks || es.getBlock(x, y, z).isAir()) {
						BlockState state = BlockTypes.parse(type.get().name()).getDefaultState();
						es.setBlock(x, y, z, state);
					}
					return true;
				});

				es.flushQueue();
			}
		});
	}
}
