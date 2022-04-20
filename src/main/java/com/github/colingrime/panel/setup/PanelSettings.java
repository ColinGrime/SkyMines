package com.github.colingrime.panel.setup;

import com.github.colingrime.SkyMines;
import com.github.colingrime.panel.setup.slot.PanelSlot;
import com.github.colingrime.panel.setup.slot.StandardPanelSlot;
import com.github.colingrime.panel.setup.slot.UpgradePanelSlot;
import com.github.colingrime.panel.setup.slot.meta.PanelSlotMeta;
import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelSettings {

    private final File file;
    private FileConfiguration config;

    private PanelData mainPanel;
    private PanelData upgradePanel;

    public PanelSettings(SkyMines plugin) {
        this.file = new File(plugin.getDataFolder(), "panel.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource("panel.yml", false);
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);

        // set new values
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
        return Utils.color(config.getString(path + ".name"));
    }

    private int getRows(String path) {
        int rows = config.getInt(path + ".rows");
        if (rows < 1) {
            return 1;
        } else {
            return Math.min(rows, 6);
        }
    }

    private void fill(String path, Map<Integer, PanelSlot> slots, int rows) {
        String type = config.getString(path + ".fill");
        if (type == null) {
            return;
        }

        Material fill = getType(type);
        for (int i = 0; i < rows * 9; i++) {
            if (slots.get(i) == null) {
                slots.put(i, new StandardPanelSlot(new PanelSlotMeta(fill, " ")));
            }
        }
    }

    private Map<Integer, PanelSlot> getSlots(String path) {
        ConfigurationSection sec = config.getConfigurationSection(path + ".slots");
        if (sec == null) {
            return new HashMap<>();
        } else {
            return getSlots(sec);
        }
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
        UpgradeType upgradeType = UpgradeType.parse(upgradeName);
        if (upgradeType == null) {
            return null;
        }

        String path = "upgrades." + upgradeName;

        // slot metas
        PanelSlotMeta slotMeta = getSlotMeta(path);
        PanelSlotMeta maxSlotMeta = getSlotMeta(path + ".max");

        ConfigurationSection sec = config.getConfigurationSection(path + ".lore");
        if (sec == null) {
            return new UpgradePanelSlot(upgradeType, slotMeta, maxSlotMeta);
        }

        // upgrade lore levels
        Map<Integer, List<String>> lores = new HashMap<>();
        for (String level : sec.getKeys(false)) {
            if (level.matches("\\d+")) {
                lores.put(Integer.parseInt(level), Utils.color(sec.getStringList(level)));
            }
        }

        return new UpgradePanelSlot(upgradeType, slotMeta, maxSlotMeta, lores);
    }

    /*
     * Gets the type, name, and lore of the given path.
     */
    private PanelSlotMeta getSlotMeta(String path) {
        Material type = getType(config.getString(path + ".type"));
        String name = Utils.color(config.getString(path + ".name"));
        List<String> lore = Utils.color(config.getStringList(path + ".lore"));
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
