package com.github.colingrime.api;

import com.github.colingrime.SkyMines;

public class SkyMinesAPI {

	private static final SkyMinesAPI instance;
	private SkyMines plugin;

	static {
		instance = new SkyMinesAPI();
	}

	private SkyMinesAPI() {}

	public static SkyMinesAPI getInstance() {
		return instance;
	}

	public void setPlugin(SkyMines plugin) {
		this.plugin = plugin;
	}
}
