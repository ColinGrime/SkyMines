package com.github.colingrime.skymines.structure.material;

import org.bukkit.Material;

public class MaterialSingle implements MaterialType {

    private final Material material;

    public MaterialSingle(Material material) {
        this.material = material;
    }

    @Override
    public Material get() {
        return material;
    }
}
