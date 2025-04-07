package me.colingrimes.skymines.skymine.structure.region.implementation;

import me.colingrimes.skymines.skymine.structure.region.Region;
import me.colingrimes.skymines.skymine.structure.region.functional.TriConsumer;
import me.colingrimes.skymines.skymine.structure.region.functional.TriPredicate;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a skymine's parameter.
 * The top platform is removed from the parameter.
 */
public class ParameterRegion extends AbstractRegion {

	private final List<Region> parameterSides = new ArrayList<>();

	public ParameterRegion(@Nonnull Vector pt1, @Nonnull Vector pt2) {
		setPoints(pt1, pt2);
	}

	@Override
	public void setPoints(@Nonnull Vector pt1, @Nonnull Vector pt2) {
		super.setPoints(pt1, pt2);

		// get the points of the mine
		int x1 = pt1.getBlockX();
		int z1 = pt1.getBlockZ();
		int x2 = pt2.getBlockX();
		int z2 = pt2.getBlockZ();

		// the 5 walls of the mine
		parameterSides.add(new CuboidRegion(new Vector(x1, min.getBlockY(), z1), new Vector(x2, min.getBlockY(), z2)));
		parameterSides.add(new CuboidRegion(new Vector(x1, min.getBlockY(), z1), new Vector(x2, max.getBlockY(), z1)));
		parameterSides.add(new CuboidRegion(new Vector(x2, min.getBlockY(), z1), new Vector(x2, max.getBlockY(), z2)));
		parameterSides.add(new CuboidRegion(new Vector(x1, min.getBlockY(), z1), new Vector(x1, max.getBlockY(), z2)));
		parameterSides.add(new CuboidRegion(new Vector(x1, min.getBlockY(), z2), new Vector(x2, max.getBlockY(), z2)));
	}

	@Override
	public boolean containsWithin(@Nonnull Vector pt, int blocksAway) {
		return parameterSides.stream().anyMatch(region -> region.containsWithin(pt, blocksAway));
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
