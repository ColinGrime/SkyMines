package com.github.colingrime.skymines.structure.material;

import org.bukkit.Material;

public record MaterialSingle(Material material) implements MaterialType {

	@Override
	public Material get() {
		return material;
	}
}
