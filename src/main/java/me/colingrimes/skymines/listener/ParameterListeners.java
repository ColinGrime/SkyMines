package me.colingrimes.skymines.listener;

import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;

import javax.annotation.Nonnull;

/**
 * Used to block access from breaking the parameter of a mine.
 */
public class ParameterListeners implements Listener {

	private final SkyMines plugin;

	public ParameterListeners(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		blockAccessIfParameter(event.getBlock(), event);
	}

	@EventHandler
	public void onEntityExplodeEvent(@Nonnull EntityExplodeEvent event) {
		for (Block block : event.blockList()) {
			blockAccessIfParameter(block, event);
		}
	}

	@EventHandler
	public void onPistonRetract(@Nonnull BlockPistonRetractEvent event) {
		for (Block block : event.getBlocks()) {
			blockAccessIfParameter(block, event);
		}
	}

	@EventHandler
	public void onPistonExtend(@Nonnull BlockPistonExtendEvent event) {
		for (Block block : event.getBlocks()) {
			blockAccessIfParameter(block, event);
		}
	}

	@EventHandler
	public void onBlockBurn(@Nonnull BlockBurnEvent event) {
		blockAccessIfParameter(event.getBlock(), event);
	}

	private void blockAccessIfParameter(@Nonnull Block block, @Nonnull Cancellable cancellableEvent) {
		if (cancellableEvent.isCancelled()) {
			return;
		}

		for (SkyMine skyMine : plugin.getSkyMineManager().getSkyMines()) {
			if (skyMine.getStructure().getParameter().contains(block.getLocation().toVector())) {
				cancellableEvent.setCancelled(true);
				return;
			}
		}
	}
}
