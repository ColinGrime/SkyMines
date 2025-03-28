package me.colingrimes.skymines.skymine.structure.material;

import org.bukkit.Material;

import javax.annotation.Nonnull;

public class MaterialSingle implements MaterialType {

	private final Material material;

	public MaterialSingle(@Nonnull Material material) {
		this.material = material;
	}

	@Override
	@Nonnull
	public Material get() {
		return material;
	}
}
