package com.github.colingrime.skymines.structure.model;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * POJO representing a Block and a Material type.
 */
public record BlockInfo(Block block, Material type) {}
