package com.github.colingrime.skymines.structure.behavior;

import com.fastasyncworldedit.core.FaweAPI;
import com.github.colingrime.SkyMines;
import com.github.colingrime.config.BlockVariety;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.Set;

public class FastBuildBehavior implements BuildBehavior {

	@Override
	public boolean isClear(org.bukkit.World world, Set<Vector> vectors) {
		World faweWorld = FaweAPI.getWorld(world.getName());

		try (EditSession es = WorldEdit.getInstance().newEditSession(faweWorld)) {
			for (Vector vector : vectors) {
				BlockState state = es.getBlock(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
				if (!state.isAir()) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void buildParameter(org.bukkit.World world, Set<Vector> parameter, Material type) {
		if (parameter.isEmpty()) {
			return;
		}

		Bukkit.getScheduler().runTaskAsynchronously(SkyMines.getInstance(), () -> {
			World faweWorld = FaweAPI.getWorld(world.getName());
			BlockState state = BlockTypes.parse(type.name()).getDefaultState();

			try (EditSession es = WorldEdit.getInstance().newEditSession(faweWorld)) {
				for (Vector vector : parameter) {
					es.setBlock(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ(), state);
				}
				es.flushQueue();
			}
		});
	}

	@Override
	public void buildInside(org.bukkit.World world, Vector min, Vector max, Material type) {
		if (min == null || max == null) {
			return;
		}

		Bukkit.getScheduler().runTaskAsynchronously(SkyMines.getInstance(), () -> {
			World faweWorld = FaweAPI.getWorld(world.getName());
			BlockState state = BlockTypes.parse(type.name()).getDefaultState();

			try (EditSession es = WorldEdit.getInstance().newEditSession(faweWorld)) {
				for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
					for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
						for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
							es.setBlock(x, y, z, state);
						}
					}
				}

				es.flushQueue();
			}
		});
	}

	@Override
	public void buildInside(org.bukkit.World world, Vector min, Vector max, BlockVariety blockVariety, boolean replaceBlocks) {
		if (min == null || max == null) {
			return;
		}

		Bukkit.getScheduler().runTaskAsynchronously(SkyMines.getInstance(), () -> {
			World faweWorld = FaweAPI.getWorld(world.getName());
			try (EditSession es = WorldEdit.getInstance().newEditSession(faweWorld)) {
				for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
					for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
						for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
							if (replaceBlocks || es.getBlock(x, y, z).isAir()) {
								BlockState state = BlockTypes.parse(blockVariety.getRandom().name()).getDefaultState();
								es.setBlock(x, y, z, state);
							}
						}
					}
				}

				es.flushQueue();
			}
		});
	}
}
