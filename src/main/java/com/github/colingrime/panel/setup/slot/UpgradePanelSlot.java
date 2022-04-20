package com.github.colingrime.panel.setup.slot;

import com.github.colingrime.panel.setup.slot.meta.PanelSlotMeta;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.skymines.upgrades.UpgradeType;
import com.github.colingrime.skymines.upgrades.types.SkyMineUpgrade;
import com.github.colingrime.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpgradePanelSlot extends PanelSlot {

    private final UpgradeType upgradeType;
    private final PanelSlotMeta slotMeta;
    private final PanelSlotMeta maxSlotMeta;
    private final Map<Integer, List<String>> lores;
    private SkyMineUpgrades upgrades;

    public UpgradePanelSlot(UpgradeType upgradeType, PanelSlotMeta slotMeta, PanelSlotMeta maxSlotMeta) {
        this(upgradeType, slotMeta, maxSlotMeta, new HashMap<>());
    }

    public UpgradePanelSlot(UpgradeType upgradeType, PanelSlotMeta slotMeta, PanelSlotMeta maxSlotMeta, Map<Integer, List<String>> levelLores) {
        this.upgradeType = upgradeType;
        this.slotMeta = slotMeta;
        this.maxSlotMeta = maxSlotMeta;
        this.lores = levelLores;
    }

    public ItemStack getUpgradeItem(SkyMineUpgrades upgrades) {
        this.upgrades = upgrades;
        return getItem();
    }

    @Override
    public Material getType() {
        if (upgrades.getUpgrade(upgradeType).canBeUpgraded()) {
            return slotMeta.getType();
        } else {
            return maxSlotMeta.getType();
        }
    }

    @Override
    public String getName() {
        SkyMineUpgrade upgrade = upgrades.getUpgrade(upgradeType);
        int level = upgrade.getLevel();

        if (upgrade.canBeUpgraded()) {
            return replaceLevels(slotMeta.getName(), level);
        } else {
            return replaceLevels(maxSlotMeta.getName(), level);
        }
    }

    @Override
    public List<String> getLore() {
        SkyMineUpgrade upgrade = upgrades.getUpgrade(upgradeType);
        if (!upgrade.canBeUpgraded()) {
            return maxSlotMeta.getLore();
        }

        List<String> lore = lores.get(upgrade.getLevel() + 1);
        if (lore == null || lore.isEmpty()) {
            return Collections.singletonList(Utils.color("&cFailed to retrieve lore values."));
        } else {
            return lore;
        }
    }

    @Override
    public String getCommand() {
        if (upgrades.getUpgrade(upgradeType).canBeUpgraded()) {
            return maxSlotMeta.getCommand();
        } else {
            return slotMeta.getCommand();
        }
    }

    public UpgradeType getUpgradeType() {
        return upgradeType;
    }

    private String replaceLevels(String name, int level) {
        String currentLevel = String.valueOf(level);
        String nextLevel = String.valueOf(level + 1);
        return name.replaceAll("%level%", currentLevel).replaceAll("%next-level%", nextLevel);
    }
}
