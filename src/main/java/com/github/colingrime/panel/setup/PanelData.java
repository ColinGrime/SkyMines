package com.github.colingrime.panel.setup;

import com.github.colingrime.panel.setup.slot.PanelSlot;

import java.util.Map;

public record PanelData(String name, int rows, Map<Integer, PanelSlot> slots) {}
