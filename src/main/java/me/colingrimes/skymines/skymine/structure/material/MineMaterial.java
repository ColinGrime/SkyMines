package me.colingrimes.skymines.skymine.structure.material;

import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * Responsible for getting the material type used in mine creation.
 */
public interface MineMaterial {

	/**
	 * Gets a material.
	 *
	 * @return the material
	 */
	@Nonnull
	Material get();
}
