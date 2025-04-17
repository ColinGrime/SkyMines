package me.colingrimes.skymines.skymine.structure.border;

import me.colingrimes.midnight.functional.TriConsumer;
import me.colingrimes.midnight.functional.TriPredicate;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.geometry.Region;
import org.bukkit.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a skymine's border with the top platform removed.
 */
public class BorderRegion extends Region {

	private final List<Region> borders = new ArrayList<>();

	public BorderRegion(@Nonnull Position pos1, @Nonnull Position pos2) {
		super(pos1, pos2);
		init();
	}

	/**
	 * Initializes the 5 borders of the mine.
	 */
	private void init() {
		World world = min.getWorld();
		double x1 = min.getX(), z1 = min.getZ();
		double x2 = max.getX(), z2 = max.getZ();

		// Calculates the 5 borders.
		borders.add(Region.of(Position.of(world, x1, min.getY(), z1), Position.of(world, x2, min.getY(), z2)));
		borders.add(Region.of(Position.of(world, x2, min.getY(), z1), Position.of(world, x2, max.getY(), z2)));
		borders.add(Region.of(Position.of(world, x1, min.getY(), z2), Position.of(world, x2, max.getY(), z2)));
		borders.add(Region.of(Position.of(world, x1, min.getY(), z1), Position.of(world, x1, max.getY(), z2)));
		borders.add(Region.of(Position.of(world, x1, min.getY(), z1), Position.of(world, x2, max.getY(), z1)));
	}

	@Override
	public boolean containsWithin(@Nonnull Position pos, double distance) {
		return borders.stream().anyMatch(region -> region.containsWithin(pos, distance));
	}

	@Override
	public void handler(@Nonnull TriConsumer<Integer, Integer, Integer> action) {
		borders.forEach(region -> region.handler(action));
	}

	@Override
	public boolean handler(@Nonnull TriPredicate<Integer, Integer, Integer> action) {
		return borders.stream().allMatch(region -> region.handler(action));
	}
}
