package com.github.colingrime.skymines.upgrades;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.structure.material.MaterialSingle;
import com.github.colingrime.skymines.structure.material.MaterialType;
import com.github.colingrime.skymines.upgrades.factory.DefaultUpgradeFactory;
import com.github.colingrime.skymines.upgrades.factory.SkyMineUpgrade;
import com.github.colingrime.skymines.upgrades.factory.UpgradeFactory;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SkyMineUpgrades {

	private static final UpgradeFactory upgradeFactory = new DefaultUpgradeFactory();
	private final List<SkyMineUpgrade<?>> upgrades = new ArrayList<>();

	public SkyMineUpgrades(SkyMines plugin) {
		for (UpgradeType upgradeType : UpgradeType.values()) {
			String upgradeName = plugin.getUpgrades().getDefaultPath(upgradeType);
			upgrades.add(upgradeFactory.createUpgrade(upgradeType, upgradeName));
		}
	}

	public SkyMineUpgrades(List<SkyMineUpgrade<?>> upgrades) {
		this.upgrades.addAll(upgrades);
	}

	public Optional<SkyMineUpgrade<?>> getUpgrade(UpgradeType upgradeType) {
		return upgrades.stream().filter(u -> u.getUpgradeType() == upgradeType).findAny();
	}

	public MaterialType getBlockVariety() {
		return getUpgradeValue(UpgradeType.BlockVariety, new MaterialSingle(Material.STONE));
	}

	public double getResetCooldown() {
		return getUpgradeValue(UpgradeType.ResetCooldown, 0.0);
	}

	public int getDepthIncrease() {
		return getUpgradeValue(UpgradeType.DepthIncrease, 0);
	}

	private <T> T getUpgradeValue(UpgradeType upgradeType, T def) {
		Optional<SkyMineUpgrade<?>> upgrade = getUpgrade(upgradeType);
		if (upgrade.isEmpty()) {
			return def;
		}

		Object obj = upgrade.get().getUpgrade();
		if (def.getClass().isInstance(obj)) {
			@SuppressWarnings("unchecked")
			T value = (T) def.getClass().cast(obj);
			return value;
		}

		return def;
	}

	public static String serialize(SkyMineUpgrades upgrades) {
		StringBuilder sb = new StringBuilder();
		for (SkyMineUpgrade<?> upgrade : upgrades.upgrades) {
			sb.append(upgrade).append("\n");
		}

		return sb.toString();
	}

	public static Optional<SkyMineUpgrades> deserialize(String text) {
		if (text == null || text.split("\n").length == 0) {
			return Optional.empty();
		}

		List<SkyMineUpgrade<?>> upgrades = new ArrayList<>();
		String[] texts = text.split("\n");

		for (String upgradeString : texts) {
			SkyMineUpgrade<?> upgrade = deserialize(upgradeString.split(":"));
			if (upgrade != null) {
				upgrades.add(upgrade);
			}
		}

		return Optional.of(new SkyMineUpgrades(upgrades));
	}

	private static SkyMineUpgrade<?> deserialize(String[] upgradeArray) {
		Optional<UpgradeType> type = UpgradeType.from(upgradeArray[0]);
		if (type.isEmpty()) {
			return null;
		}

		String name = upgradeArray[1];
		int level = 1;

		try {
			level = Integer.parseInt(upgradeArray[2]);
		} catch (NumberFormatException ex) {
			// if type are name are found, we can ignore this and use default level
		}

		return upgradeFactory.createUpgrade(type.get(), name, level);
	}
}
