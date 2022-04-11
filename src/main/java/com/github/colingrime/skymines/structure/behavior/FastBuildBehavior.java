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

import java.util.List;

public class FastBuildBehavior implements BuildBehavior {

	@Override
	public boolean isClear(org.bukkit.World world, List<Vector> vectors) {
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
	public void build(org.bukkit.World world, List<Vector> vectors, Material type) {
		if (vectors.isEmpty()) {
			return;
		}

		Bukkit.getScheduler().runTaskAsynchronously(SkyMines.getInstance(), () -> {
			World faweWorld = FaweAPI.getWorld(world.getName());
			BlockState state = BlockTypes.parse(type.name()).getDefaultState();

			try (EditSession es = WorldEdit.getInstance().newEditSession(faweWorld)) {
				for (Vector vector : vectors) {
					es.setBlock(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ(), state);
				}
				es.flushQueue();
			}
		});
	}

	@Override
	public void build(org.bukkit.World world, List<Vector> vectors, BlockVariety blockVariety) {
		if (vectors.isEmpty()) {
			return;
		}

		Bukkit.getScheduler().runTaskAsynchronously(SkyMines.getInstance(), () -> {
			World faweWorld = FaweAPI.getWorld(world.getName());
			try (EditSession es = WorldEdit.getInstance().newEditSession(faweWorld)) {
				for (Vector vector : vectors) {
					BlockState state = BlockTypes.parse(blockVariety.getRandom().name()).getDefaultState();
					es.setBlock(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ(), state);
				}
				es.flushQueue();
			}
		});
	}
}
