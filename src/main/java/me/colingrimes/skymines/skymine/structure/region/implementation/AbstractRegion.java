package me.colingrimes.skymines.skymine.structure.region.implementation;

import me.colingrimes.skymines.skymine.structure.region.Region;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public abstract class AbstractRegion implements Region {

	protected Vector min;
	protected Vector max;

	@Nonnull
	@Override
	public Vector getMin() {
		return min;
	}

	@Nonnull
	@Override
	public Vector getMax() {
		return max;
	}

	@Override
	public void setPoints(@Nonnull Vector pt1, @Nonnull Vector pt2) {
		int x1 = pt1.getBlockX();
		int y1 = pt1.getBlockY();
		int z1 = pt1.getBlockZ();
		int x2 = pt2.getBlockX();
		int y2 = pt2.getBlockY();
		int z2 = pt2.getBlockZ();

		int minX = Math.min(x1, x2);
		int minY = Math.min(y1, y2);
		int minZ = Math.min(z1, z2);
		int maxX = Math.max(x1, x2);
		int maxY = Math.max(y1, y2);
		int maxZ = Math.max(z1, z2);

		min = new Vector(minX, minY, minZ);
		max = new Vector(maxX, maxY, maxZ);
	}
}
