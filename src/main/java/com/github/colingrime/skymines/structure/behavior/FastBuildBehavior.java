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
import org.bukkit.block.Block;

import java.util.List;

public class FastBuildBehavior implements BuildBehavior {

	private final SkyMines plugin;

	public FastBuildBehavior(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void build(List<Block> blocks, Material type) {
		if (blocks.isEmpty()) {
			return;
		}

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			Block randomBlock = blocks.get(0);
			World world = FaweAPI.getWorld(randomBlock.getWorld().getName());
			BlockState state = BlockTypes.parse(type.name()).getDefaultState();

			try (EditSession es = WorldEdit.getInstance().newEditSession(world)) {
				for (Block block : blocks) {
					es.setBlock(block.getX(), block.getY(), block.getZ(), state);
				}
				es.flushQueue();
			}
		});
	}

	@Override
	public void build(List<Block> blocks, BlockVariety blockVariety) {
		if (blocks.isEmpty()) {
			return;
		}

		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			Block randomBlock = blocks.get(0);
			World world = FaweAPI.getWorld(randomBlock.getWorld().getName());

			try (EditSession es = WorldEdit.getInstance().newEditSession(world)) {
				for (Block block : blocks) {
					BlockState state = BlockTypes.parse(blockVariety.getRandom().name()).getDefaultState();
					es.setBlock(block.getX(), block.getY(), block.getZ(), state);
				}
				es.flushQueue();
			}
		});
	}
}
