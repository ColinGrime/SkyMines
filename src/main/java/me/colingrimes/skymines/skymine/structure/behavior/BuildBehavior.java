package me.colingrimes.skymines.skymine.structure.behavior;

import me.colingrimes.midnight.geometry.Region;
import me.colingrimes.skymines.skymine.structure.material.MineMaterial;

import javax.annotation.Nonnull;

public interface BuildBehavior {

	/**
	 * Checks if the area can be built on. An area can be built on if it's all air.
	 * However, there's an additional check for transparent blocks if enabled.
	 *
	 * @param region the region to check
	 * @return true if the region can be built on
	 */
	boolean canBuild(@Nonnull Region region);

	/**
	 * Replaces every block in the {@link Region} with the specified {@link MineMaterial}.
	 *
	 * @param region the region to build
	 * @param mineMaterial the material
	 */
	default void build(@Nonnull Region region, @Nonnull MineMaterial mineMaterial) {
		build(region, mineMaterial, true);
	}

	/**
	 * Replaces every block in the {@link Region} with the specified {@link MineMaterial}.
	 * If {@code replaceBlocks} is false, the blocks must be air.
	 *
	 * @param region the region
	 * @param material the material
	 * @param replaceBlocks true if only air should be replaced
	 */
	void build(@Nonnull Region region, @Nonnull MineMaterial material, boolean replaceBlocks);
}
