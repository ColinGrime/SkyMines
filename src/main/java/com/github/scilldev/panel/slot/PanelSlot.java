package com.github.scilldev.panel.slot;

import com.github.scilldev.SkyMines;
import com.github.scilldev.config.BlockVariety;
import com.github.scilldev.panel.PanelSettings;
import com.github.scilldev.skymines.SkyMine;
import com.github.scilldev.skymines.upgrades.SkyMineUpgrades;
import com.github.scilldev.utils.Utils;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class PanelSlot {

	private final SkyMines plugin;
	private final int slotNum;
	private final PanelSlotMeta slotMeta;
	private final SkyMineUpgrades.UpgradeType upgradeType;

	public PanelSlot(SkyMines plugin, int slotNum, PanelSlotMeta slotMeta) {
		this(plugin, slotNum, slotMeta, null);
	}

	public PanelSlot(SkyMines plugin, int slotNum, PanelSlotMeta slotMeta, SkyMineUpgrades.UpgradeType upgradeType) {
		this.plugin = plugin;
		this.slotNum = slotNum;
		this.slotMeta = slotMeta;
		this.upgradeType = upgradeType;
	}

	public int getSlotNum() {
		return slotNum;
	}

	public Material getType() {
		if (upgradeType == null) {
			return slotMeta.getType();
		}
		return plugin.getPanelSettings().getUpgrades().get(upgradeType).getType();
	}

	public String getName(SkyMine skyMine) {
		if (upgradeType == null) {
			return slotMeta.getName();
		}

		int level = skyMine.getUpgrades().getLevel(upgradeType);
		String name = plugin.getPanelSettings().getUpgrades().get(upgradeType).getName();

		// replaces level placeholders
		return name.replaceAll("%level%", String.valueOf(level))
				.replaceAll("%next-level%", String.valueOf(level + 1));
	}

	public List<String> getLore(SkyMine skyMine) {
		if (upgradeType == null) {
			return slotMeta.getLore();
		}

		switch (upgradeType) {
			case BlockVariety:
				return getBlockVarietyLore(skyMine);
		}

		// TODO finish
		return null;
	}

	private List<String> getBlockVarietyLore(SkyMine skyMine) {
		List<String> lore = plugin.getPanelSettings().getUpgrades().get(upgradeType).getLore();
		int level = skyMine.getUpgrades().getLevel(upgradeType);

		Map<Integer, BlockVariety> blockVariety = plugin.getSettings().getUpgradesBlockVariety();
		Map<Material, Double> currentVariety = blockVariety.get(level).getTypeChances();
		Map<Material, Double> nextBlockVariety = blockVariety.get(level + 1).getTypeChances();

		for (Map.Entry<Material, Double> type : nextBlockVariety.entrySet()) {
			// ADD
			if (!currentVariety.containsKey(type.getKey())) {
				String add = plugin.getPanelSettings().getLoreGenerator().get(PanelSettings.LoreType.ADD);
				String item = Utils.capitalization(type.getKey().name());
				String percent = plugin.getPanelSettings().getPlaceholderPercent().replace("%percent%", String.valueOf(type.getValue()));
				lore.add(add.replaceAll("%item%", item).replaceAll("%percent%", percent));
			}

			// CHANGE
			else {
				// TODO make ADD go first all the way, then CHANGE, then REMOVE
				// ^ probably from addAll'ing the 3 separate lists at the end
			}
		}

		for (Map.Entry<Material, Double> type : currentVariety.entrySet()) {
			// REMOVE
			if (!nextBlockVariety.containsKey(type.getKey())) {

			}
		}

		return null;
	}
}
