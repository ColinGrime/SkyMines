package com.github.colingrime.skymines.structure.material;

import org.bukkit.Material;

import java.util.*;

public class MaterialVariety implements MaterialType {

	// used to get random materials
	private final NavigableMap<Double, Material> typeRandomizer = new TreeMap<>();
	private final List<Material> materials = new ArrayList<>();

	private final Random random = new Random();
	private double totalPercentages = 0;

	public void addType(String materialString, String chanceString) {
		Material material = Material.matchMaterial(materialString);
		chanceString = chanceString.replaceAll("%", "");

		if (material != null && chanceString.matches("\\d+(\\.\\d+)?")) {
			addType(material, Double.parseDouble(chanceString));
		}
	}

	/**
	 * @param material any material
	 * @param chance chance that the specified material will drop
	 */
	public void addType(Material material, double chance) {
		if (chance <= 0) {
			return;
		}

		totalPercentages += chance;
		typeRandomizer.put(totalPercentages, material);
		materials.add(material);
	}

	@Override
	public Material get(){
		return typeRandomizer.higherEntry(random.nextDouble() * totalPercentages).getValue();
	}

	public List<Material> getMaterials() {
		return materials;
	}
}
