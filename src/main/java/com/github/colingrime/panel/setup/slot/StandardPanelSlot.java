package com.github.colingrime.panel.setup.slot;

import com.github.colingrime.panel.setup.slot.meta.PanelSlotMeta;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import org.bukkit.Material;

import java.util.List;

public class StandardPanelSlot extends PanelSlot {

	private final PanelSlotMeta slotMeta;

	public StandardPanelSlot(PanelSlotMeta slotMeta) {
		this.slotMeta = slotMeta;
	}

	@Override
	public Material getType(SkyMineUpgrades upgrades) {
		return slotMeta.getType();
	}

	@Override
	public String getName(SkyMineUpgrades upgrades) {
		return slotMeta.getName();
	}

	@Override
	public List<String> getLore(SkyMineUpgrades upgrades) {
		return slotMeta.getLore();
	}
}
