package com.github.colingrime.skymines.structure.material;

import org.bukkit.Material;

import java.util.*;

public class MaterialVariety implements MaterialType {

	// used to get random materials
	private final NavigableMap<Double, Material> typeRandomizer = new TreeMap<>();
	private final List<Material> materials = new ArrayList<>();

	private final Random random = new Random();
	private double totalPercentages = 0;

	/**
	 * @param material any material
	 * @param chance chance that the specified material will drop
	 */
	public void addType(Material material, double chance) {
		if (material == null || chance <= 0) {
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
