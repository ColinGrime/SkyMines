package me.colingrimes.skymines.api;

import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

import javax.annotation.Nonnull;

public class SkyMineBlockBreakEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final BlockBreakEvent breakEvent;
	private final SkyMine skyMine;

	public SkyMineBlockBreakEvent(@Nonnull BlockBreakEvent breakEvent, @Nonnull SkyMine skyMine) {
		this.breakEvent = breakEvent;
		this.skyMine = skyMine;
	}

	@Nonnull
	public BlockBreakEvent getBreakEvent() {
		return breakEvent;
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
