package com.github.colingrime.panel;

import com.github.colingrime.SkyMines;
import com.github.colingrime.panel.setup.slot.PanelSlot;
import com.github.colingrime.panel.setup.slot.UpgradePanelSlot;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class UpgradePanel extends Panel {

	private final SkyMines plugin;
	private final SkyMine skyMine;

	public UpgradePanel(SkyMines plugin, Player viewer, SkyMine skyMine) {
		super(plugin, Bukkit.createInventory(null, plugin.getPanelSettings().getRows() * 9, "UPGRADES"), viewer);
		this.plugin = plugin;
		this.skyMine = skyMine;
	}

	@Override
	protected boolean setupInventory(String[] args) {
		for (Map.Entry<Integer, PanelSlot> upgradePanel : plugin.getPanelSettings().getUpgradePanel().entrySet()) {
			setItem(upgradePanel.getKey(), upgradePanel.getValue().getItem(skyMine), (player, clickType) -> {
				if (clickType == ClickType.LEFT && upgradePanel.getValue() instanceof UpgradePanelSlot) {
					// TODO add money system and account for too high of leveling
					UpgradePanelSlot upgradeSlot = (UpgradePanelSlot) upgradePanel.getValue();
					skyMine.getUpgrades().increaseLevel(upgradeSlot.getUpgradeType());
					skyMine.reset();
					getViewer().closeInventory();
				}
			});
		}

		ItemStack fill = new ItemStack(plugin.getPanelSettings().getFill());
		if (fill.getType() != Material.AIR) {
			for (int i = 0; i < getInventory().getSize(); i++) {
				ItemStack item = getInventory().getItem(i);
				if (item != null && item.getType() == Material.AIR) {
					getInventory().setItem(i, fill);
				}
			}
		}

		return true;
	}
}
