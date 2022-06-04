package com.github.colingrime.utils;

import org.bukkit.Material;

import javax.annotation.Nonnull;

/**
 * Attempts to parse a Material, but provides a fallback Material if it fails.
 */
public final class MaterialParser {

	public static Material parseMaterial(String input) {
		return parseMaterial(input, Material.STONE);
	}

	public static Material parseMaterial(String input, @Nonnull Material def) {
		Material material = Material.matchMaterial(input);
		return material == null ? def : material;
	}

	private MaterialParser() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
