package com.github.colingrime.config;

import org.bukkit.Material;

import java.util.*;

public class BlockVariety {

	// used to get the chance of each material
	private final Map<Material, Double> typeChances = new HashMap<>();

	// used to get random materials
	private final NavigableMap<Double, Material> typeRandomizer = new TreeMap<>();

	private final Random random = new Random();
	private double totalPercentages = 0;

	public boolean addType(String materialString, String chanceString) {
		Material material = Material.matchMaterial(materialString);
		chanceString = chanceString.replaceAll("%", "");

		if (material != null && chanceString.matches("\\d+(\\.\\d+)?")) {
			addType(material, Double.parseDouble(chanceString));
			return true;
		}

		return false;
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

		typeChances.put(material, (int) (chance * 10) / 10.0);
		typeRandomizer.put(totalPercentages, material);
	}

	public Map<Material, Double> getTypeChances() {
		return typeChances;
	}

	public Optional<Material> getRandom() {
		return Optional.ofNullable(typeRandomizer.higherEntry(random.nextDouble() * totalPercentages).getValue());
	}
}
