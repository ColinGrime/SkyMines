package me.colingrimes.skymines.skymine.structure.region.implementation;

import me.colingrimes.midnight.functional.TriConsumer;
import me.colingrimes.midnight.functional.TriPredicate;
import me.colingrimes.midnight.geometry.Position;

import javax.annotation.Nonnull;

public class CuboidRegion extends AbstractRegion {

	public CuboidRegion(@Nonnull Position pos1, @Nonnull Position pos2) {
		set(pos1, pos2);
	}

	@Override
	public boolean containsWithin(@Nonnull Position pos, int blocksAway) {
		int x = pos.getBlockX();
		int y = pos.getBlockY();
		int z = pos.getBlockZ();

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
