package com.github.scilldev.panel.slot;

import org.bukkit.Material;

import java.util.List;

public class PanelSlotMeta {

	private final Material type;
	private final String name;
	private final List<String> lore;

	public PanelSlotMeta(Material type, String name, List<String> lore) {
		this.type = type;
		this.name = name;
		this.lore = lore;
	}

	public Material getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}
}
