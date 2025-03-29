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
 * Responsible for preventing all attempts to break the parameter of a skymine.
 */
public class ParameterListeners implements Listener {

	private final SkyMines plugin;

	public ParameterListeners(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		cancelIfSkyMineParameter(event, event.getBlock());
	}

	@EventHandler
	public void onEntityExplode(@Nonnull EntityExplodeEvent event) {
		for (Block block : event.blockList()) {
			if (cancelIfSkyMineParameter(event, block)) {
				return;
			}
		}
	}

	@EventHandler
	public void onPistonRetract(@Nonnull BlockPistonRetractEvent event) {
		for (Block block : event.getBlocks()) {
			if (cancelIfSkyMineParameter(event, block)) {
				return;
			}
		}
	}

	@EventHandler
	public void onPistonExtend(@Nonnull BlockPistonExtendEvent event) {
		for (Block block : event.getBlocks()) {
			if (cancelIfSkyMineParameter(event, block)) {
				return;
			}
		}
	}

	@EventHandler
	public void onBlockBurn(@Nonnull BlockBurnEvent event) {
		cancelIfSkyMineParameter(event, event.getBlock());
	}

	/**
	 * Cancels the specified event if the block is part of a skymine parameter.
	 *
	 * @param event the event to cancel
	 * @param block the block to check
	 * @return true if the event was cancelled
	 */
	private boolean cancelIfSkyMineParameter(@Nonnull Cancellable event, @Nonnull Block block) {
		if (event.isCancelled()) {
			return true;
		}

		for (SkyMine skyMine : plugin.getSkyMineManager().getSkyMines()) {
			if (skyMine.getStructure().getParameter().contains(block.getLocation().toVector())) {
				event.setCancelled(true);
				return true;
			}
		}
		return false;
	}
}
