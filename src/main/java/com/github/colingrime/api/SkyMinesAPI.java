package com.github.colingrime.api;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.manager.SkyMineManager;

public final class SkyMinesAPI {

    private static final SkyMinesAPI instance;

    static {
        instance = new SkyMinesAPI();
    }

    private final SkyMines plugin;

    private SkyMinesAPI() {
        this.plugin = SkyMines.getInstance();
    }

    public static SkyMinesAPI getInstance() {
        return instance;
    }

    public SkyMineManager getManager() {
        return plugin.getSkyMineManager();
    }
}
