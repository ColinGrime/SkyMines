package me.colingrimes.skymines.skymine.structure.region.implementation;

import me.colingrimes.skymines.skymine.structure.region.functional.TriConsumer;
import me.colingrimes.skymines.skymine.structure.region.functional.TriPredicate;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public class CuboidRegion extends AbstractRegion {

	public CuboidRegion(@Nonnull Vector pt1, @Nonnull Vector pt2) {
		setPoints(pt1, pt2);
	}

	@Override
	public boolean containsWithin(@Nonnull Vector pt, int blocksAway) {
		int x = pt.getBlockX();
		int y = pt.getBlockY();
		int z = pt.getBlockZ();

		return x >= min.getBlockX() - blocksAway && x <= max.getBlockX() + blocksAway
				&& y >= min.getBlockY() - blocksAway && y <= max.getBlockY() + blocksAway
				&& z >= min.getBlockZ() - blocksAway && z <= max.getBlockZ() + blocksAway;
	}

	@Override
	public void handler(@Nonnull TriConsumer<Integer, Integer, Integer> action) {
		for (int y=max.getBlockY(); y>=min.getBlockY(); y--) {
			for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
				for (int z=min.getBlockZ(); z<=max.getBlockZ(); z++) {
					action.accept(x, y, z);
				}
			}
		}
	}

	@Override
	public boolean handler(@Nonnull TriPredicate<Integer, Integer, Integer> action) {
		for (int y=max.getBlockY(); y>=min.getBlockY(); y--) {
			for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
				for (int z=min.getBlockZ(); z<=max.getBlockZ(); z++) {
					if (!action.test(x, y, z)) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
