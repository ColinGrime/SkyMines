package me.colingrimes.skymines.skymine.structure.region;

import me.colingrimes.skymines.skymine.structure.region.functional.TriConsumer;
import me.colingrimes.skymines.skymine.structure.region.functional.TriPredicate;
import me.colingrimes.skymines.skymine.structure.region.implementation.CuboidRegion;
import me.colingrimes.skymines.skymine.structure.region.implementation.ParameterRegion;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

/**
 * Represents a region used for the mine structures.
 * This can either be a {@link ParameterRegion} for the outer walls of the mine
 * or a {@link CuboidRegion} for the inner blocks of the mine.
 */
public interface Region {

	/**
	 * Gets the minimum point of the region.
	 *
	 * @return the minimum point
	 */
	@Nonnull
	Vector getMin();

	/**
	 * Gets the maximum point of the region.
	 *
	 * @return the maximum point
	 */
	@Nonnull
	Vector getMax();

	/**
	 * Sets the points of the region.
	 *
	 * @param pt1 first point
	 * @param pt2 second point
	 */
	void setPoints(@Nonnull Vector pt1, @Nonnull Vector pt2);

	/**
	 * Checks if the region contains the specified point.
	 *
	 * @param pt the point
	 * @return true if the point is within the region
	 */
	default boolean contains(@Nonnull Vector pt) {
		return containsWithin(pt, 0);
	}

	/**
	 * Checks if the region is within the specified number of blocks away from the point.
	 *
	 * @param pt the point
	 * @param blocksAway the number of blocks away from the point to check
	 * @return true if the point is within {@code blocksAway} blocks
	 */
	boolean containsWithin(@Nonnull Vector pt, int blocksAway);

	/**
	 * Performs mass actions on all points in the region.
	 *
	 * @param action the action to perform on all points
	 */
	void handler(@Nonnull TriConsumer<Integer, Integer, Integer> action);

	/**
	 * Performs mass actions on all points in the region.
	 * Stops early if one of the predicates returns false.
	 *
	 * @param action the action to perform on all points
	 * @return true if the action was successful
	 */
	boolean handler(@Nonnull TriPredicate<Integer, Integer, Integer> action);
}
