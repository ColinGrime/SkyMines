package com.github.colingrime.panel;

import com.github.colingrime.SkyMines;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.panel.setup.slot.PanelSlot;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class MainPanel extends Panel {

	private final SkyMines plugin;
	private final SkyMine skyMine;

	public MainPanel(SkyMines plugin, Player viewer, SkyMine skyMine) {
		super(plugin, Bukkit.createInventory(null, plugin.getPanelSettings().getMainPanel().getRows() * 9,
				plugin.getPanelSettings().getMainPanel().getName()), viewer);
		this.plugin = plugin;
		this.skyMine = skyMine;
	}

	@Override
	protected boolean setupInventory(String[] args) {
		for (Map.Entry<Integer, PanelSlot> upgradePanel : plugin.getPanelSettings().getMainPanel().getSlots().entrySet()) {
			int slotNum = upgradePanel.getKey();
			PanelSlot panelSlot = upgradePanel.getValue();
			ItemStack item = panelSlot.getItem();

			// check for time placeholder
			if (item.getItemMeta() != null && item.getItemMeta().getDisplayName().contains("%time%")) {
				ItemMeta meta = item.getItemMeta();
				Replacer replacer = new Replacer("%time%", Utils.formatTime(skyMine.getCooldownTime()));
				meta.setDisplayName(replacer.replace(meta.getDisplayName()));
				item.setItemMeta(meta);
			}

			setItem(slotNum, item, (player, clickType) -> {
				if (clickType == ClickType.LEFT && !panelSlot.getCommand().isEmpty()) {
					player.closeInventory();

					Replacer replacer = new Replacer("/", "").add("%id%", skyMine.getId());
					player.performCommand(replacer.replace(panelSlot.getCommand()));
				}
			});
		}

		return true;
	}
}
