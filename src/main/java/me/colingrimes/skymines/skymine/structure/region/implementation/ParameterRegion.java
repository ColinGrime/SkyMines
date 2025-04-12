package me.colingrimes.skymines.skymine.structure.region.implementation;

import me.colingrimes.midnight.functional.TriConsumer;
import me.colingrimes.midnight.functional.TriPredicate;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.skymines.skymine.structure.region.Region;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a skymine's parameter.
 * The top platform is removed from the parameter.
 */
public class ParameterRegion extends AbstractRegion {

	private final List<Region> parameterSides = new ArrayList<>();

	public ParameterRegion(@Nonnull Position pos1, @Nonnull Position pos2) {
		set(pos1, pos2);
	}

	@Override
	public void set(@Nonnull Position pos1, @Nonnull Position pos2) {
		super.set(pos1, pos2);

		// get the points of the mine
		int x1 = pos1.getBlockX();
		int z1 = pos1.getBlockZ();
		int x2 = pos2.getBlockX();
		int z2 = pos2.getBlockZ();

		// the 5 walls of the mine
		parameterSides.add(new CuboidRegion(Position.of(pos1.getWorld(), x1, min.getBlockY(), z1), Position.of(pos1.getWorld(), x2, min.getBlockY(), z2)));
		parameterSides.add(new CuboidRegion(Position.of(pos1.getWorld(), x1, min.getBlockY(), z1), Position.of(pos1.getWorld(), x2, max.getBlockY(), z1)));
		parameterSides.add(new CuboidRegion(Position.of(pos1.getWorld(), x2, min.getBlockY(), z1), Position.of(pos1.getWorld(), x2, max.getBlockY(), z2)));
		parameterSides.add(new CuboidRegion(Position.of(pos1.getWorld(), x1, min.getBlockY(), z1), Position.of(pos1.getWorld(), x1, max.getBlockY(), z2)));
		parameterSides.add(new CuboidRegion(Position.of(pos1.getWorld(), x1, min.getBlockY(), z2), Position.of(pos1.getWorld(), x2, max.getBlockY(), z2)));
	}

	@Override
	public boolean containsWithin(@Nonnull Position pos, int blocksAway) {
		return parameterSides.stream().anyMatch(region -> region.containsWithin(pos, blocksAway));
	}

	@Override
	public void handler(@Nonnull TriConsumer<Integer, Integer, Integer> action) {
		parameterSides.forEach(region -> region.handler(action));
	}

	@Override
	public boolean handler(@Nonnull TriPredicate<Integer, Integer, Integer> action) {
		return parameterSides.stream().allMatch(region -> region.handler(action));
	}
}
