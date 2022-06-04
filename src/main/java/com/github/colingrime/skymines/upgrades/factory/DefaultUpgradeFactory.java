package com.github.colingrime.skymines.upgrades.factory;

import com.github.colingrime.skymines.structure.material.MaterialSingle;
import com.github.colingrime.skymines.structure.material.MaterialType;
import com.github.colingrime.skymines.upgrades.UpgradeType;
import org.bukkit.Material;

public class DefaultUpgradeFactory implements UpgradeFactory {

	@Override
	public SkyMineUpgrade<?> createUpgrade(UpgradeType type, String name) {
		return createUpgrade(type, name, 1);
	}

	@Override
	public SkyMineUpgrade<?> createUpgrade(UpgradeType type, String name, int level) {
		return switch(type) {
			case BlockVariety -> new SkyMineUpgrade<MaterialType>(new MaterialSingle(Material.STONE), name, level);
			case DepthIncrease -> new SkyMineUpgrade<>(0, name, level);
			case ResetCooldown -> new SkyMineUpgrade<>(0.0, name, level);
		};
	}
}
