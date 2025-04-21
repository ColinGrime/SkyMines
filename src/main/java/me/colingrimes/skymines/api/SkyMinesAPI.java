package me.colingrimes.skymines.api;

import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.manager.SkyMineManager;

import javax.annotation.Nonnull;

public final class SkyMinesAPI {

	private static final SkyMinesAPI instance;
	private final SkyMines plugin;

	static {
		instance = new SkyMinesAPI();
	}

	private SkyMinesAPI() {
		this.plugin = SkyMines.getInstance();
	}

	@Nonnull
	public static SkyMinesAPI getInstance() {
		return instance;
	}

	@Nonnull
	public SkyMineManager getManager() {
		return plugin.getSkyMineManager();
	}
}
