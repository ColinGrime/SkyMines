package me.colingrimes.skymines.skymine.structure.material;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * A dynamic material that changes each time {@link MineMaterial#get()} is called.
 * <p>
 * This is used for the inner blocks of a mine.
 */
public class MineMaterialDynamic implements MineMaterial {

	private static final Random random = new Random();
	private final NavigableMap<Double, Material> materialRandomizer = new TreeMap<>();
	private double totalPercentages = 0;

	/**
	 * Adds the material to the dynamic material map.
	 *
	 * @param material the material
	 * @param chance the chance that the material will drop
	 */
	public void addMaterial(@Nonnull Material material, double chance) {
		if (chance <= 0) {
			return;
		}

		totalPercentages += chance;
		materialRandomizer.put(totalPercentages, material);
	}

	@Nonnull
	@Override
	public Material get(){
		return materialRandomizer.higherEntry(random.nextDouble() * totalPercentages).getValue();
	}
}
