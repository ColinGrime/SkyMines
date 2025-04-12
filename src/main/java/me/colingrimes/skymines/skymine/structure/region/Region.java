package me.colingrimes.skymines.skymine.structure.region;

import me.colingrimes.midnight.functional.TriConsumer;
import me.colingrimes.midnight.functional.TriPredicate;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.skymines.skymine.structure.region.implementation.CuboidRegion;
import me.colingrimes.skymines.skymine.structure.region.implementation.ParameterRegion;

import javax.annotation.Nonnull;

/**
 * Represents a region used for the mine structures.
 * This can either be a {@link ParameterRegion} for the outer walls of the mine
 * or a {@link CuboidRegion} for the inner blocks of the mine.
 */
public interface Region {

	/**
	 * Sets the two positions of the region.
	 *
	 * @param pos1 the first position
	 * @param pos2 the second position
	 */
	void set(@Nonnull Position pos1, @Nonnull Position pos2);

	/**
	 * Gets the minimum position of the region.
	 *
	 * @return the minimum position
	 */
	@Nonnull
	Position getMin();

	/**
	 * Gets the maximum position of the region.
	 *
	 * @return the maximum position
	 */
	@Nonnull
	Position getMax();

	/**
	 * Checks if the region contains the specified position.
	 *
	 * @param pos the position
	 * @return true if the position is within the region
	 */
	default boolean contains(@Nonnull Position pos) {
		return containsWithin(pos, 0);
	}

	/**
	 * Checks if the region is within the specified number of blocks away from the position.
	 *
	 * @param pos the position
	 * @param blocksAway the number of blocks away from the position to check
	 * @return true if the position is within {@code blocksAway} blocks
	 */
	boolean containsWithin(@Nonnull Position pos, int blocksAway);

	/**
	 * Performs mass actions on all positions in the region.
	 *
	 * @param action the action to perform on all positions
	 */
	void handler(@Nonnull TriConsumer<Integer, Integer, Integer> action);

	/**
	 * Performs mass actions on all positions in the region.
	 * Stops early if one of the predicates returns false.
	 *
	 * @param action the action to perform on all positions
	 * @return true if the action was successful
	 */
	boolean handler(@Nonnull TriPredicate<Integer, Integer, Integer> action);
}
