package com.github.colingrime.skymines.structure.region.implementation;

import com.github.colingrime.skymines.structure.region.Region;
import org.bukkit.util.Vector;

abstract class AbstractRegion implements Region {

	Points points;

	AbstractRegion(Vector pt1, Vector pt2) {
		points = new Points(pt1, pt2);
		setPoints(pt1, pt2);
	}

	@Override
	public boolean contains(Vector pt) {
		return containsWithin(pt, 0);
	}

	/**
	 * Nested class to display the significant points of a given region.
	 */
	static class Points {

		final int minX, minY, minZ;
		final int maxX, maxY, maxZ;

		Points(Vector pt1, Vector pt2) {
			int x1 = pt1.getBlockX();
			int y1 = pt1.getBlockY();
			int z1 = pt1.getBlockZ();
			int x2 = pt2.getBlockX();
			int y2 = pt2.getBlockY();
			int z2 = pt2.getBlockZ();

			this.minX = Math.min(x1, x2);
			this.minY = Math.min(y1, y2);
			this.minZ = Math.min(z1, z2);
			this.maxX = this.minX == x1 ? x2 : x1;
			this.maxY = this.minY == y1 ? y2 : y1;
			this.maxZ = this.minZ == z1 ? z2 : z1;
		}
	}
}
