package com.github.colingrime.panel;

import com.github.colingrime.SkyMines;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.panel.setup.slot.PanelSlot;
import com.github.colingrime.panel.setup.slot.StandardPanelSlot;
import com.github.colingrime.panel.setup.slot.UpgradePanelSlot;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.skymines.upgrades.factory.SkyMineUpgrade;
import com.github.colingrime.utils.StringUtils;
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
				if (clickType == ClickType.LEFT && upgrades.getUpgrade(upgradeSlot.getUpgradeType()).isPresent()) {
					attemptUpgrade(player, upgrades.getUpgrade(upgradeSlot.getUpgradeType()).get());
					getViewer().closeInventory();
				}
			});
		}

		return true;
	}

	/**
	 * Attempts to upgrade the specified skymine upgrade.
	 * @param player any player
	 * @param upgrade any upgrade
	 */
	private void attemptUpgrade(Player player, SkyMineUpgrade<?> upgrade) {
		// check if the upgrade can be upgraded
		if (!upgrade.canBeUpgraded()) {
			Messages.FAILURE_ALREADY_MAXED.sendTo(player);
		}

		// check for permission
		else if (!upgrade.hasPermission(player)) {
			Messages.FAILURE_NO_PERMISSION.sendTo(player);
		}

		// attempt upgrade
		else if (upgrade.levelUp(player)) {
			Replacer replacer = new Replacer("%upgrade%", StringUtils.format(upgrade.getUpgradeType().name()));
			replacer.add("%level%", upgrade.getLevel());

			// send success message and save the skymine's state
			Messages.SUCCESS_UPGRADE.sendTo(player, replacer);
			skyMine.save();

			// reset the mine on upgrade
			if (plugin.getSettings().shouldResetOnUpgrade()) {
				skyMine.reset(true);
			}
		}

		// player has insuffient funds
		else {
			Messages.FAILURE_NO_FUNDS.sendTo(player);
		}
	}
}
