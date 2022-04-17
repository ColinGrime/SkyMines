package com.github.colingrime.api;

import com.github.colingrime.skymines.SkyMine;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class SkyMineBlockBreakEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final BlockBreakEvent breakEvent;
	private final SkyMine skyMine;

	public SkyMineBlockBreakEvent(BlockBreakEvent breakEvent, SkyMine skyMine) {
		this.breakEvent = breakEvent;
		this.skyMine = skyMine;
	}

	public BlockBreakEvent getBreakEvent() {
		return breakEvent;
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
