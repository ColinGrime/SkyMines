package com.github.colingrime.panel.setup.slot;

import com.github.colingrime.panel.setup.slot.meta.PanelSlotMeta;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.Material;

import java.util.List;

public class StandardPanelSlot extends PanelSlot {

	private final PanelSlotMeta slotMeta;

	public StandardPanelSlot(PanelSlotMeta slotMeta) {
		this.slotMeta = slotMeta;
	}

	@Override
	public Material getType(SkyMine skyMine) {
		return slotMeta.getType();
	}

	@Override
	public String getName(SkyMine skyMine) {
		return slotMeta.getName();
	}

	@Override
	public List<String> getLore(SkyMine skyMine) {
		return slotMeta.getLore();
	}
}
