package com.github.colingrime.skymines.structure.region;

import com.github.colingrime.skymines.structure.region.functional.TriPredicate;
import org.bukkit.util.Vector;

public interface Region {

	/**
	 * Sets the points of the region.
	 * Explicitly called when a region is first created.
	 *
	 * @param pt1 first point
	 * @param pt2 second point
	 */
	void setPoints(Vector pt1, Vector pt2);

	/**
	 * @param pt any point
	 * @return true if the point is within the region
	 */
	boolean contains(Vector pt);

	/**
	 * @param pt any point
	 * @param blocksAway amount of blocks away the vector can be
	 * @return true if the point is within {@code blocksAway} blocks
	 */
	boolean containsWithin(Vector pt, int blocksAway);

	/**
	 * Performs mass actions on all points in the region.
	 * @param action action to be performed on all points
	 * @return true if the action was successful
	 */
	boolean handler(TriPredicate<Integer, Integer, Integer> action);
}
