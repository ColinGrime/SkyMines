package com.github.colingrime.skymines.structure.region;

import com.github.colingrime.skymines.structure.region.functional.TriPredicate;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a skymine's parameter.
 * The top platform is removed from the parameter.
 */
public class ParameterRegion implements Region {

	private List<Region> parameterSides = new ArrayList<>();

	public ParameterRegion(Vector pt1, Vector pt2) {
		setPoints(pt1, pt2);
	}

	@Override
	public void setPoints(Vector pt1, Vector pt2) {
		int x1 = pt1.getBlockX();
		int y1 = pt1.getBlockY();
		int z1 = pt1.getBlockZ();
		int x2 = pt2.getBlockX();
		int y2 = pt2.getBlockY();
		int z2 = pt2.getBlockZ();

		int minY = Math.min(y1, y2);
		int maxY = minY == y1 ? y2 : y1;

		// the 5 walls of the mine
		parameterSides.add(new CuboidRegion(new Vector(x1, minY, z1), new Vector(x2, minY, z2)));
		parameterSides.add(new CuboidRegion(new Vector(x1, minY, z1), new Vector(x2, maxY, z1)));
		parameterSides.add(new CuboidRegion(new Vector(x2, minY, z1), new Vector(x2, maxY, z2)));
		parameterSides.add(new CuboidRegion(new Vector(x1, minY, z1), new Vector(x1, maxY, z2)));
		parameterSides.add(new CuboidRegion(new Vector(x1, minY, z2), new Vector(x2, maxY, z2)));
	}

	@Override
	public boolean contains(Vector pt) {
		return containsWithin(pt, 0);
	}

	@Override
	public boolean containsWithin(Vector pt, int blocksAway) {
		for (Region region : parameterSides) {
			if (region.containsWithin(pt, blocksAway)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean handler(TriPredicate<Integer, Integer, Integer> action) {
		for (Region region : parameterSides) {
			if (!region.handler(action)) {
				return false;
			}
		}

		return true;
	}
}
