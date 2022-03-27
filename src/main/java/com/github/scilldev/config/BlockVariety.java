package com.github.scilldev.config;

import org.bukkit.Material;

import java.util.NavigableMap;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

public class BlockVariety {

	private final NavigableMap<Double, Material> types = new TreeMap<>();
	private final Random random = new Random();
	private double totalPercentages = 0;

	public boolean addType(String materialString, String chanceString) {
		Material material = Material.getMaterial(materialString);
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
		types.put(totalPercentages, material);
	}

	public Optional<Material> getRandom() {
		return Optional.ofNullable(types.higherEntry(random.nextDouble() * totalPercentages).getValue());
	}
}
