package com.github.colingrime.config.custom.implementation;

import com.github.colingrime.SkyMines;
import com.github.colingrime.panel.setup.PanelData;
import com.github.colingrime.panel.setup.slot.PanelSlot;
import com.github.colingrime.panel.setup.slot.StandardPanelSlot;
import com.github.colingrime.panel.setup.slot.UpgradePanelSlot;
import com.github.colingrime.panel.setup.slot.meta.PanelSlotMeta;
import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PanelConfig extends AbstractConfiguration {

	private PanelData mainPanel;
	private PanelData upgradePanel;

	public PanelConfig(SkyMines plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "panels";
	}

	@Override
	void updateValues() {
		mainPanel = _getMainPanel();
		upgradePanel = _getUpgradePanel();
	}

	private PanelData _getMainPanel() {
		String panelName = "main-panel";

		String name = getName(panelName);
		int rows = getRows(panelName);
		Map<Integer, PanelSlot> slots = getSlots(panelName);
		fill(panelName, slots, rows);

		return new PanelData(name, rows, slots);
	}

	/**
	 * @return
	 */
	public PanelData getMainPanel() {
		return mainPanel;
	}

	private PanelData _getUpgradePanel() {
		String panelName = "upgrade-panel";
		String name = getName(panelName);
		int rows = getRows(panelName);

		ConfigurationSection sec = config.getConfigurationSection(panelName + ".slots");
		if (sec == null) {
			return null;
		}

		Map<Integer, PanelSlot> slots = getSlots(sec);
		for (String slotNum : sec.getKeys(false)) {
			if (slotNum.matches("\\d+")) {
				// replace with an upgrade slot if available
				UpgradePanelSlot upgradeSlot = getUpgradeSlot(sec.getString(slotNum + ".upgrade"));
				if (upgradeSlot != null) {
					slots.put(Integer.parseInt(slotNum) - 1, upgradeSlot);
				}
			}
		}

		fill("upgrade-panel", slots, rows);
		return new PanelData(name, rows, slots);
	}

	public PanelData getUpgradePanel() {
		return upgradePanel;
	}

	private String getName(String path) {
		return StringUtils.color(config.getString(path + ".name"));
	}

	private int getRows(String path) {
		int rows = config.getInt(path + ".rows");
		return rows < 1 ? 1 : Math.min(rows, 6);
	}

	private void fill(String path, Map<Integer, PanelSlot> slots, int rows) {
		String type = config.getString(path + ".fill");
		if (type == null) {
			return;
		}

		Material fill = getType(type);
		for (int i=0; i<rows*9; i++) {
			if (slots.get(i) == null) {
				slots.put(i, new StandardPanelSlot(new PanelSlotMeta(fill, " ")));
			}
		}
	}

	private Map<Integer, PanelSlot> getSlots(String path) {
		ConfigurationSection sec = config.getConfigurationSection(path + ".slots");
		return sec == null ? new HashMap<>() : getSlots(sec);
	}

	private Map<Integer, PanelSlot> getSlots(ConfigurationSection sec) {
		Map<Integer, PanelSlot> slots = new HashMap<>();
		for (String slotNum : sec.getKeys(false)) {
			if (slotNum.matches("\\d+")) {
				PanelSlotMeta slotMeta = getSlotMeta(sec.getCurrentPath() + "." + slotNum);
				slots.put(Integer.parseInt(slotNum) - 1, new StandardPanelSlot(slotMeta));
			}
		}

		return slots;
	}

	/*
	 * Gets an upgrade slot given the name of an upgrade.
	 */
	private UpgradePanelSlot getUpgradeSlot(String upgradeName) {
		Optional<UpgradeType> upgradeType = UpgradeType.from(upgradeName);
		if (upgradeType.isEmpty()) {
			return null;
		}

		String path = "upgrades." + upgradeName;

		// slot metas
		PanelSlotMeta slotMeta = getSlotMeta(path);
		PanelSlotMeta maxSlotMeta = getSlotMeta(path + ".max");

		ConfigurationSection sec = config.getConfigurationSection(path + ".lore");
		if (sec == null) {
			return new UpgradePanelSlot(upgradeType.get(), slotMeta, maxSlotMeta);
		}

		// upgrade lore levels
		Map<Integer, List<String>> lores = new HashMap<>();
		for (String level : sec.getKeys(false)) {
			if (level.matches("\\d+")) {
				lores.put(Integer.parseInt(level), StringUtils.color(sec.getStringList(level)));
			}
		}

		return new UpgradePanelSlot(upgradeType.get(), slotMeta, maxSlotMeta, lores);
	}

	/*
	 * Gets the type, name, and lore of the given path.
	 */
	private PanelSlotMeta getSlotMeta(String path) {
		Material type = getType(config.getString(path + ".type"));
		String name = StringUtils.color(config.getString(path + ".name"));
		List<String> lore = StringUtils.color(config.getStringList(path + ".lore"));
		String command = config.getString(path + ".command");
		return new PanelSlotMeta(type, name, lore, command);
	}

	/**
	 * @param name any name
	 * @return the specified type, or STONE if the item does not exist
	 */
	private Material getType(String name) {
		if (name == null) {
			return Material.STONE;
		}

		Material type = Material.matchMaterial(name);
		return type == null ? Material.STONE : type;
	}
}
