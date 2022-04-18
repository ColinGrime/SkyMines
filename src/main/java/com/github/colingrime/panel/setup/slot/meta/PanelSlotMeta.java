package com.github.colingrime.panel.setup.slot.meta;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class PanelSlotMeta {

    private final Material type;
    private final String name;
    private final List<String> lore;
    private final String command;

    public PanelSlotMeta(Material type, String name) {
        this(type, name, new ArrayList<>(), "");
    }

    public PanelSlotMeta(Material type, String name, List<String> lore) {
        this(type, name, lore, "");
    }

    public PanelSlotMeta(Material type, String name, List<String> lore, String command) {
        this.type = type;
        this.name = name;
        this.lore = lore;
        this.command = command;
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

    public String getCommand() {
        return command;
    }
}
