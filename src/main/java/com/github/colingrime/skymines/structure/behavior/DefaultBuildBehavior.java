package com.github.colingrime.skymines.structure.behavior;

import com.github.colingrime.config.BlockVariety;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Set;

public class DefaultBuildBehavior implements BuildBehavior {

	@Override
	public boolean isClear(World world, Set<Vector> vectors) {
		for (Vector vector : vectors) {
			if (vector.toLocation(world).getBlock().getType() == Material.AIR) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void buildParameter(World world, Set<Vector> parameter, Material type) {
		// will change later, but right now it's default Bukkit#setType
		for (Vector vector : parameter) {
			vector.toLocation(world).getBlock().setType(type);
		}
	}

	@Override
	public void buildInside(World world, Vector min, Vector max, Material type) {
		for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					world.getBlockAt(x, y, z).setType(type);
				}
			}
		}
	}

	@Override
	public void buildInside(World world, Vector min, Vector max, BlockVariety blockVariety, boolean replaceBlocks) {
		for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
					Block block = world.getBlockAt(x, y, z);
					if (replaceBlocks || block.getType() == Material.AIR) {
						block.setType(blockVariety.getRandom());
					}
				}
			}
		}
	}
}
