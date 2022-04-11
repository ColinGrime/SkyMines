package com.github.colingrime.config;

import org.bukkit.Material;

import java.util.NavigableMap;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

public class BlockVariety {

	// used to get random materials
	private final NavigableMap<Double, Material> typeRandomizer = new TreeMap<>();

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
	}

	public Material getRandom() {
		return typeRandomizer.higherEntry(random.nextDouble() * totalPercentages).getValue();
	}
}
