package me.colingrimes.skymines.skymine.structure.material;

import me.colingrimes.midnight.util.misc.Types;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.List;
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

	public MineMaterialDynamic(@Nonnull List<String> configMaterials) {
		addMaterials(configMaterials);
	}

	/**
	 * Adds the materials to the dynamic material map.
	 *
	 * @param configMaterials the list of materials (with percentages) given from the configuration file
	 */
	public void addMaterials(@Nonnull List<String> configMaterials) {
		for (String entry : configMaterials) {
			Material material = Material.getMaterial(entry.split(" ")[0].toUpperCase());
			String chance = entry.split(" ")[1].replace("%", "");
			if (material != null && Types.isDouble(chance)) {
				addMaterial(material, Double.parseDouble(chance));
			}
		}
	}

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
