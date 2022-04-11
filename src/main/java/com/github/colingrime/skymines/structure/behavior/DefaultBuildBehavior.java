package com.github.colingrime.skymines.structure.behavior;

import com.github.colingrime.config.BlockVariety;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.List;

public class DefaultBuildBehavior implements BuildBehavior {

	@Override
	public boolean isClear(World world, List<Vector> vectors) {
		for (Vector vector : vectors) {
			if (vector.toLocation(world).getBlock().getType() == Material.AIR) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void build(World world, List<Vector> vectors, Material type) {
		// will change later, but right now it's default Bukkit#setType
		for (Vector vector : vectors) {
			vector.toLocation(world).getBlock().setType(type);
		}
	}

	@Override
	public void build(World world, List<Vector> vectors, BlockVariety blockVariety) {
		for (Vector vector : vectors) {
			vector.toLocation(world).getBlock().setType(blockVariety.getRandom());
		}
	}
}
