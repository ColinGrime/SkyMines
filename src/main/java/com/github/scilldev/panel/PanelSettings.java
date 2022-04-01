package com.github.scilldev.panel;

import com.github.scilldev.SkyMines;
import com.github.scilldev.panel.slot.PanelSlot;
import com.github.scilldev.panel.slot.PanelSlotMeta;
import com.github.scilldev.skymines.upgrades.SkyMineUpgrades;
import com.github.scilldev.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelSettings {

	public enum LoreType {
		ADD,
		CHANGE,
		REMOVE;

		public static LoreType getLoreType(String name) {
			for (LoreType type : LoreType.values()) {
				if (type.name().equalsIgnoreCase(name)) {
					return type;
				}
			}
			return null;
		}
	}

	private final SkyMines plugin;
	private final File file;
	private FileConfiguration config;

	private int rows;
	private Material fill;
	private List<PanelSlot> slots;
	private Map<SkyMineUpgrades.UpgradeType, PanelSlotMeta> upgrades;
	private Map<LoreType, String> loreGenerator;

	// placeholders
	private String placeholderPercent;
	private String placeholderNextPercent;

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
		slots = _getSlots();
		upgrades = _getUpgrades();
		loreGenerator = _getLoreGenerator();
		placeholderPercent = _getPlaceholderPercent();
		placeholderNextPercent = _getPlaceholderNextPercent();
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

	private List<PanelSlot> _getSlots() {
		List<PanelSlot> slots = new ArrayList<>();
		ConfigurationSection sec = config.getConfigurationSection("upgrade-panel.slots");
		if (sec == null) {
			return slots;
		}

		for (String slotNum : sec.getKeys(false)) {
			if (slotNum.matches("\\d+")) {
				continue;
			}

			// gets the slot options
			Material type = getType(sec.getString(slotNum + ".type"));
			String name = Utils.color(sec.getString(slotNum + ".name"));
			List<String> lore = Utils.color(sec.getStringList(slotNum + ".lore"));
			SkyMineUpgrades.UpgradeType upgrade = getUpgrade(sec.getString(slotNum + ".upgrade"));

			// creates and adds the slot
			PanelSlotMeta slotMeta = new PanelSlotMeta(type, name, lore);
			slots.add(new PanelSlot(plugin, Integer.parseInt(slotNum), slotMeta, upgrade));
		}

		return slots;
	}

	public List<PanelSlot> getSlots() {
		return slots;
	}

	private Map<SkyMineUpgrades.UpgradeType, PanelSlotMeta> _getUpgrades() {
		Map<SkyMineUpgrades.UpgradeType, PanelSlotMeta> upgrades = new HashMap<>();
		ConfigurationSection sec = config.getConfigurationSection("upgrades");
		if (sec == null) {
			return upgrades;
		}

		for (String upgradeString : sec.getKeys(false)) {
			SkyMineUpgrades.UpgradeType upgrade = getUpgrade(upgradeString);
			if (upgrade == null) {
				continue;
			}

			// gets the slot options
			Material type = getType(sec.getString(upgradeString + ".type"));
			String name = Utils.color(sec.getString(upgradeString + ".name"));
			List<String> lore = Utils.color(sec.getStringList(upgradeString + ".lore"));

			// adds the slot meta
			upgrades.put(upgrade, new PanelSlotMeta(type, name, lore));
		}

		return upgrades;
	}

	public Map<SkyMineUpgrades.UpgradeType, PanelSlotMeta> getUpgrades() {
		return upgrades;
	}

	private Map<LoreType, String> _getLoreGenerator() {
		Map<LoreType, String> loreGenerator = new HashMap<>();
		ConfigurationSection sec = config.getConfigurationSection("upgrades.lore");
		if (sec == null) {
			return loreGenerator;
		}

		for (String loreTypeString : sec.getKeys(false)) {
			LoreType loreType = LoreType.getLoreType(loreTypeString);
			if (loreType != null) {
				loreGenerator.put(loreType, sec.getString(loreTypeString));
			}
		}

		return loreGenerator;
	}

	public Map<LoreType, String> getLoreGenerator() {
		return loreGenerator;
	}

	private String _getPlaceholderPercent() {
		return Utils.color(config.getString("placeholders.percent"));
	}

	public String getPlaceholderPercent() {
		return placeholderPercent;
	}

	private String _getPlaceholderNextPercent() {
		return Utils.color(config.getString("placeholders.next-percent"));
	}

	public String getPlaceholderNextPercent() {
		return placeholderNextPercent;
	}

	// gets the material, or returns null
	private Material getType(String name) {
		if (name == null) {
			return Material.AIR;
		}
		return Material.matchMaterial(name);
	}

	// gets the upgrade type, or returns null
	private SkyMineUpgrades.UpgradeType getUpgrade(String name) {
		if (name == null) {
			return null;
		}
		return SkyMineUpgrades.UpgradeType.getUpgradeType(name);
	}
}
