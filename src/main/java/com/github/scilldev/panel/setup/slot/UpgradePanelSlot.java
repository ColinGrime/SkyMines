package com.github.scilldev.panel.setup.slot;

import com.github.scilldev.panel.setup.slot.meta.PanelSlotMeta;
import com.github.scilldev.skymines.SkyMine;
import com.github.scilldev.skymines.upgrades.SkyMineUpgrades;
import com.github.scilldev.utils.Utils;
import org.bukkit.Material;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradePanelSlot extends PanelSlot {

	private final SkyMineUpgrades.UpgradeType upgradeType;
	private final PanelSlotMeta slotMeta;
	private final PanelSlotMeta maxSlotMeta;
	private final Map<Integer, List<String>> lores;

	public UpgradePanelSlot(SkyMineUpgrades.UpgradeType upgradeType, PanelSlotMeta slotMeta, PanelSlotMeta maxSlotMeta) {
		this(upgradeType, slotMeta, maxSlotMeta, new HashMap<>());
	}

	public UpgradePanelSlot(SkyMineUpgrades.UpgradeType upgradeType, PanelSlotMeta slotMeta, PanelSlotMeta maxSlotMeta, Map<Integer, List<String>> levelLores) {
		this.upgradeType = upgradeType;
		this.slotMeta = slotMeta;
		this.maxSlotMeta = maxSlotMeta;
		this.lores = levelLores;
	}

	@Override
	public Material getType(SkyMine skyMine) {
		if (skyMine.canBeUpgraded(upgradeType)) {
			return slotMeta.getType();
		} else {
			return maxSlotMeta.getType();
		}
	}

	@Override
	public String getName(SkyMine skyMine) {
		int level = skyMine.getUpgrades().getLevel(upgradeType);
		if (skyMine.canBeUpgraded(upgradeType)) {
			return replaceLevels(slotMeta.getName(), level);
		} else {
			return replaceLevels(maxSlotMeta.getName(), level);
		}
	}

	@Override
	public List<String> getLore(SkyMine skyMine) {
		if (!skyMine.canBeUpgraded(upgradeType)) {
			return maxSlotMeta.getLore();
		}

		List<String> lore = lores.get(skyMine.getUpgrades().getLevel(upgradeType) + 1);
		if (lore == null || lore.isEmpty()) {
			return Collections.singletonList(Utils.color("&cFailed to retrieve lore values."));
		} else {
			return lore;
		}
	}

	public SkyMineUpgrades.UpgradeType getUpgradeType() {
		return upgradeType;
	}

	private String replaceLevels(String name, int level) {
		String currentLevel = String.valueOf(level);
		String nextLevel = String.valueOf(level + 1);
		return name.replaceAll("%level%", currentLevel).replaceAll("%next-level%", nextLevel);
	}
}
