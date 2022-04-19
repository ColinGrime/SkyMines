package com.github.colingrime.panel.setup;

import com.github.colingrime.panel.setup.slot.PanelSlot;

import java.util.Map;

public record PanelData(String name, int rows,
                        Map<Integer, PanelSlot> slots) {

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
