package com.github.colingrime.skymines.structure.model;

import org.bukkit.Material;
import org.bukkit.block.Block;

public record BlockInfo(Block block, Material type) {

    public Block getBlock() {
        return block;
    }

    public Material getType() {
        return type;
    }
}
