package com.github.colingrime.skymines.structure.region.implementation;

import com.github.colingrime.skymines.structure.region.functional.TriPredicate;
import org.bukkit.util.Vector;

public class CuboidRegion extends AbstractRegion {

	private Vector min;
	private Vector max;

	private CuboidRegion(Vector pt1, Vector pt2) {
		super(pt1, pt2);
	}

	/**
	 * @param pt1 first point
	 * @param pt2 second point
	 * @return CuboidRegion with the specified points
	 */
	public static CuboidRegion of(Vector pt1, Vector pt2) {
		return new CuboidRegion(pt1, pt2);
	}

	@Override
	public void setPoints(Vector pt1, Vector pt2) {
		this.min = new Vector(points.minX, points.minY, points.minZ);
		this.max = new Vector(points.maxX, points.maxY, points.maxZ);
	}

	@Override
	public boolean containsWithin(Vector pt, int blocksAway) {
		int x = pt.getBlockX();
		int y = pt.getBlockY();
		int z = pt.getBlockZ();

		return x >= min.getBlockX() - blocksAway && x <= max.getBlockX() + blocksAway
				&& y >= min.getBlockY() - blocksAway && y <= max.getBlockY() + blocksAway
				&& z >= min.getBlockZ() - blocksAway && z <= max.getBlockZ() + blocksAway;
	}

	@Override
	public boolean handler(TriPredicate<Integer, Integer, Integer> action) {
		for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
			for (int y=min.getBlockY(); y<=max.getBlockY(); y++) {
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
