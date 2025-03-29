package me.colingrimes.skymines.api;

import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class SkyMineCooldownFinishEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final SkyMine skyMine;

	public SkyMineCooldownFinishEvent(@Nonnull SkyMine skyMine) {
		this.skyMine = skyMine;
	}

	@Nonnull
	public SkyMine getSkyMine() {
		return skyMine;
	}

	@Nonnull
	public HandlerList getHandlers() {
		return handlers;
	}

	@Nonnull
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
