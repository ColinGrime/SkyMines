package me.colingrimes.skymines.skymine.structure.material;

import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.*;

public class MaterialVariety implements MaterialType {

	// used to get random materials
	private final NavigableMap<Double, Material> typeRandomizer = new TreeMap<>();
	private final List<Material> materials = new ArrayList<>();

	private final Random random = new Random();
	private double totalPercentages = 0;

	public void addType(@Nonnull String materialString, @Nonnull String chanceString) {
		Material material = Material.matchMaterial(materialString);
		chanceString = chanceString.replace("%", "");

		if (material != null && chanceString.matches("\\d+(\\.\\d+)?")) {
			addType(material, Double.parseDouble(chanceString));
		}
	}

	/**
	 * @param material any material
	 * @param chance chance that the specified material will drop
	 */
	public void addType(@Nonnull Material material, double chance) {
		if (chance <= 0) {
			return;
		}

		totalPercentages += chance;
		typeRandomizer.put(totalPercentages, material);
		materials.add(material);
	}

	@Nonnull
	@Override
	public Material get(){
		return typeRandomizer.higherEntry(random.nextDouble() * totalPercentages).getValue();
	}

	@Nonnull
	public List<Material> getMaterials() {
		return materials;
	}
}
