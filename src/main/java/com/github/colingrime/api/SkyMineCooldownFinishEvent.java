package com.github.colingrime.api;

import com.github.colingrime.skymines.SkyMine;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SkyMineCooldownFinishEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final SkyMine skyMine;

	public SkyMineCooldownFinishEvent(SkyMine skyMine) {
		this.skyMine = skyMine;
	}

	public SkyMine getSkyMine() {
		return skyMine;
	}

	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
