package com.github.scilldev.api;

import com.github.scilldev.SkyMines;

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
