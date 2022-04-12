package com.github.colingrime.panel.setup;

import com.github.colingrime.panel.setup.slot.PanelSlot;

import java.util.Map;

public class PanelData {

	private final String name;
	private final int rows;
	private final Map<Integer, PanelSlot> slots;

	public PanelData(String name, int rows, Map<Integer, PanelSlot> slots) {
		this.name = name;
		this.rows = rows;
		this.slots = slots;
	}

	public String getName() {
		return name;
	}

	public int getRows() {
		return rows;
	}

	public Map<Integer, PanelSlot> getSlots() {
		return slots;
	}
}
