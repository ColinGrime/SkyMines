package com.github.colingrime.panel.setup.slot;

import com.github.colingrime.panel.setup.slot.meta.PanelSlotMeta;
import org.bukkit.Material;

import java.util.List;

public class StandardPanelSlot extends PanelSlot {

	private final PanelSlotMeta slotMeta;

	public StandardPanelSlot(PanelSlotMeta slotMeta) {
		this.slotMeta = slotMeta;
	}

	@Override
	public Material getType() {
		return slotMeta.getType();
	}

	@Override
	public String getName() {
		return slotMeta.getName();
	}

	@Override
	public List<String> getLore() {
		return slotMeta.getLore();
	}

	@Override
	public String getCommand() {
		return slotMeta.getCommand();
	}
}
