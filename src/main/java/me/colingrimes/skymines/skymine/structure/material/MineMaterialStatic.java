package me.colingrimes.skymines.skymine.structure.material;

import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * A static material that does not change once set.
 * <p>
 * This is used for the walls of a mine.
 */
public class MineMaterialStatic implements MineMaterial {

	private final Material material;

	public MineMaterialStatic(@Nonnull Material material) {
		this.material = material;
	}

	@Override
	@Nonnull
	public Material get() {
		return material;
	}
}
