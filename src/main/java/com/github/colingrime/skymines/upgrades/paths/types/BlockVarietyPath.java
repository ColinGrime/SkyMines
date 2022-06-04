package com.github.colingrime.skymines.upgrades.paths.types;

import com.github.colingrime.skymines.structure.material.MaterialType;
import com.github.colingrime.skymines.structure.material.MaterialVariety;
import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.skymines.upgrades.paths.UpgradePathException;
import com.github.colingrime.skymines.upgrades.paths.UpgradePath;
import com.github.colingrime.utils.NumberUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class BlockVarietyPath extends UpgradePath<MaterialType> {

	public BlockVarietyPath(String name, ConfigurationSection sec) throws UpgradePathException {
		super(name, sec);
	}

	@Override
	public UpgradeType getUpgradeType() {
		return UpgradeType.BlockVariety;
	}

	@Override
	protected MaterialType parseUpgradeData(ConfigurationSection sec, String level) {
		MaterialVariety materialVariety = new MaterialVariety();
		sec.getStringList(level + ".upgrade").stream()
				.map(v -> v.replaceAll("%", "").split(" "))
				.filter(v -> v[0] != null && NumberUtils.isDouble(v[1]))
				.forEach(v -> materialVariety.addType(Material.matchMaterial(v[0]), Double.parseDouble(v[1])));
		return materialVariety;
	}
}
