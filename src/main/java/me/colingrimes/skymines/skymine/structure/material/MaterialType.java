package me.colingrimes.skymines.skymine.structure.material;

import org.bukkit.Material;

import javax.annotation.Nonnull;

public interface MaterialType {

	/**
	 * @return a material
	 */
	@Nonnull
	Material get();
}
