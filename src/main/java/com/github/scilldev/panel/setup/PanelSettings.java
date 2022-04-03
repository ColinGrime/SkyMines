package com.github.scilldev.panel.setup;

import com.github.scilldev.SkyMines;
import com.github.scilldev.panel.setup.slot.PanelSlot;
import com.github.scilldev.panel.setup.slot.StandardPanelSlot;
import com.github.scilldev.panel.setup.slot.meta.PanelSlotMeta;
import com.github.scilldev.panel.setup.slot.UpgradePanelSlot;
import com.github.scilldev.skymines.upgrades.SkyMineUpgrades;
import com.github.scilldev.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelSettings {

	private final SkyMines plugin;
	private final File file;
	private FileConfiguration config;

	private int rows;
	private Material fill;
	private Map<Integer, PanelSlot> upgradePanel;

	public PanelSettings(SkyMines plugin) {
		this.plugin = plugin;
		this.file = new File(plugin.getDataFolder(), "panel.yml");

		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource("panel.yml", false);
		}
	}

	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);

		// set new values
		rows = _getRows();
		fill = _getFill();
		upgradePanel = _getUpgradePanel();
	}

	private int _getRows() {
		int rows = config.getInt("upgrade-panel.rows");
		if (rows < 1) {
			return 1;
		} else {
			return Math.min(rows, 6);
		}
	}

	public int getRows() {
		return rows;
	}

	private Material _getFill() {
		String type = config.getString("upgrade-panel.fill");
		if (type == null) {
			return null;
		}

		return Material.matchMaterial(type);
	}

	public Material getFill() {
		return fill;
	}

	private Map<Integer, PanelSlot> _getUpgradePanel() {
		Map<Integer, PanelSlot> slots = new HashMap<>();
		ConfigurationSection sec = config.getConfigurationSection("upgrade-panel.slots");
		if (sec == null) {
			return slots;
		}

		for (String slotNum : sec.getKeys(false)) {
			if (!slotNum.matches("\\d+")) {
				continue;
			}

			// gets the upgrade slot if available
			UpgradePanelSlot upgradeSlot = getUpgradeSlot(sec.getString(slotNum + ".upgrade"));
			if (upgradeSlot != null) {
				slots.put(Integer.parseInt(slotNum) - 1, upgradeSlot);
				continue;
			}

			// standard slot
			Material type = getType(sec.getString(slotNum + ".type"));
			String name = Utils.color(sec.getString(slotNum + ".name"));
			List<String> lore = Utils.color(sec.getStringList(slotNum + ".lore"));

			PanelSlotMeta slotMeta = new PanelSlotMeta(type, name, lore);
			slots.put(Integer.parseInt(slotNum) - 1, new StandardPanelSlot(slotMeta));
		}

		return slots;
	}

	public Map<Integer, PanelSlot> getUpgradePanel() {
		return upgradePanel;
	}

	private UpgradePanelSlot getUpgradeSlot(String upgradeName) {
		SkyMineUpgrades.UpgradeType upgradeType = SkyMineUpgrades.UpgradeType.getUpgradeType(upgradeName);
		if (upgradeType == null) {
			return null;
		}

		String path = "upgrades." + upgradeName;

		// initial slot meta
		Material type = getType(config.getString(path + ".type"));
		String name = Utils.color(config.getString(path + ".name"));
		PanelSlotMeta slotMeta = new PanelSlotMeta(type, name);

		// max slot meta
		Material maxType = getType(config.getString(path + ".max.type"));
		String maxName = Utils.color(config.getString(path + ".max.name"));
		List<String> maxLore = Utils.color(config.getStringList(path + ".max.lore"));
		PanelSlotMeta maxSlotMeta = new PanelSlotMeta(maxType, maxName, maxLore);

		Map<Integer, List<String>> lores = new HashMap<>();
		ConfigurationSection sec = config.getConfigurationSection(path + ".lore");
		if (sec == null) {
			return new UpgradePanelSlot(upgradeType, slotMeta, maxSlotMeta);
		}

		// upgrade lore levels
		for (String level : sec.getKeys(false)) {
			if (level.matches("\\d+")) {
				lores.put(Integer.parseInt(level), Utils.color(sec.getStringList(level)));
			}
		}

		return new UpgradePanelSlot(upgradeType, slotMeta, maxSlotMeta, lores);
	}

	// gets the material, or returns AIR
	private Material getType(String name) {
		if (name == null) {
			return Material.AIR;
		}
		return Material.matchMaterial(name);
	}
}
