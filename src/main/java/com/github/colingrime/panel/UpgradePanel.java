package com.github.colingrime.panel;

import com.github.colingrime.SkyMines;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.panel.setup.slot.PanelSlot;
import com.github.colingrime.panel.setup.slot.StandardPanelSlot;
import com.github.colingrime.panel.setup.slot.UpgradePanelSlot;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.skymines.upgrades.types.SkyMineUpgrade;
import com.github.colingrime.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Map;

public class UpgradePanel extends Panel {

	private final SkyMines plugin;
	private final SkyMine skyMine;
	private final SkyMineUpgrades upgrades;

	public UpgradePanel(SkyMines plugin, Player viewer, SkyMine skyMine) {
		super(plugin, Bukkit.createInventory(null, plugin.getPanelSettings().getUpgradePanel().getRows() * 9,
						plugin.getPanelSettings().getUpgradePanel().getName()), viewer);
		this.plugin = plugin;
		this.skyMine = skyMine;
		this.upgrades = skyMine.getUpgrades();
	}

	@Override
	protected boolean setupInventory(String[] args) {
		for (Map.Entry<Integer, PanelSlot> upgradePanel : plugin.getPanelSettings().getUpgradePanel().getSlots().entrySet()) {
			int slotNum = upgradePanel.getKey();
			PanelSlot panelSlot = upgradePanel.getValue();

			if (panelSlot instanceof StandardPanelSlot) {
				setItem(slotNum, panelSlot.getItem());
				continue;
			}

			UpgradePanelSlot upgradeSlot = (UpgradePanelSlot) panelSlot;
			setItem(slotNum, upgradeSlot.getUpgradeItem(upgrades), (player, clickType) -> {
				if (clickType == ClickType.LEFT) {
					UpgradeType type = upgradeSlot.getUpgradeType();
					SkyMineUpgrade upgrade = upgrades.getUpgrade(type);

					if (upgrade.canBeUpgraded()) {
						if (upgrade.levelUp(player)) {
							Replacer replacer = new Replacer("%upgrade%", Utils.format(type.name())).add("%level%", upgrade.getLevel());
							Messages.SUCCESS_UPGRADE.sendTo(player, replacer);
							skyMine.reset();
							skyMine.save();
						} else {
							Messages.FAILURE_NO_FUNDS.sendTo(player);
						}
					} else {
						Messages.FAILURE_ALREADY_MAXED.sendTo(player);
					}

					getViewer().closeInventory();
				}
			});
		}

		return true;
	}
}
