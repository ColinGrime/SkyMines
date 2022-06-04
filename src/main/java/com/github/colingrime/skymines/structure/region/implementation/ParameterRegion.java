package com.github.colingrime.skymines.structure.region.implementation;

import com.github.colingrime.skymines.structure.region.Region;
import com.github.colingrime.skymines.structure.region.functional.TriPredicate;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a skymine's parameter.
 * The top platform is removed from the parameter.
 */
public class ParameterRegion extends AbstractRegion {

	private final List<Region> parameterSides = new ArrayList<>();

	private ParameterRegion(Vector pt1, Vector pt2) {
		super(pt1, pt2);
	}

	/**
	 * @param pt1 first point
	 * @param pt2 second point
	 * @return ParameterRegion with the specified points
	 */
	public static ParameterRegion of(Vector pt1, Vector pt2) {
		return new ParameterRegion(pt1, pt2);
	}

	@Override
	public void setPoints(Vector pt1, Vector pt2) {
		// minimum points
		int minX = points.minX;
		int minZ = points.minZ;
		int minY = points.minY;

		// maximum points
		int maxX = points.maxX;
		int maxZ = points.maxZ;
		int maxY = points.maxY;

		// the 5 walls of the mine
		parameterSides.add(CuboidRegion.of(new Vector(minX, minY, minZ), new Vector(maxX, minY, maxZ)));
		parameterSides.add(CuboidRegion.of(new Vector(minX, minY, minZ), new Vector(maxX, maxY, minZ)));
		parameterSides.add(CuboidRegion.of(new Vector(maxX, minY, minZ), new Vector(maxX, maxY, maxZ)));
		parameterSides.add(CuboidRegion.of(new Vector(minX, minY, minZ), new Vector(minX, maxY, maxZ)));
		parameterSides.add(CuboidRegion.of(new Vector(minX, minY, maxZ), new Vector(maxX, maxY, maxZ)));
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
