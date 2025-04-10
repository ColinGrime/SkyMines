package me.colingrimes.skymines.skymine.structure.region.implementation;

import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.skymines.skymine.structure.region.Region;

import javax.annotation.Nonnull;

public abstract class AbstractRegion implements Region {

	protected Position min;
	protected Position max;

	@Override
	public void set(@Nonnull Position pos1, @Nonnull Position pos2) {
		int x1 = pos1.getBlockX();
		int y1 = pos1.getBlockY();
		int z1 = pos1.getBlockZ();
		int x2 = pos2.getBlockX();
		int y2 = pos2.getBlockY();
		int z2 = pos2.getBlockZ();

		int minX = Math.min(x1, x2);
		int minY = Math.min(y1, y2);
		int minZ = Math.min(z1, z2);
		int maxX = Math.max(x1, x2);
		int maxY = Math.max(y1, y2);
		int maxZ = Math.max(z1, z2);

		min = Position.of(pos1.getWorld(), minX, minY, minZ);
		max = Position.of(pos2.getWorld(), maxX, maxY, maxZ);
	}

	@Nonnull
	@Override
	public Position getMin() {
		return min;
	}

	@Nonnull
	@Override
	public Position getMax() {
		return max;
	}
}
