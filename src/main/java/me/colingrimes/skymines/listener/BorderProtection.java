package me.colingrimes.skymines.listener;

import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.structure.SkyMineStructure;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;

import javax.annotation.Nonnull;

/**
 * Responsible for protecting the {@link SkyMineStructure#getBorderRegion()} of every mine against all break attempts.
 */
public class BorderProtection implements Listener {

	private final SkyMines plugin;

	public BorderProtection(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		cancelIfSkyMineBorder(event, event.getBlock(), true);
	}

	@EventHandler
	public void onEntityExplode(@Nonnull EntityExplodeEvent event) {
		event.blockList().removeIf(block -> cancelIfSkyMineBorder(event, block, false));
	}

	@EventHandler
	public void onBlockExplode(@Nonnull BlockExplodeEvent event) {
		event.blockList().removeIf(block -> cancelIfSkyMineBorder(event, block, false));
	}

	@EventHandler
	public void onPistonRetract(@Nonnull BlockPistonRetractEvent event) {
		for (Block block : event.getBlocks()) {
			if (cancelIfSkyMineBorder(event, block, true)) {
				return;
			}
		}
	}

	@EventHandler
	public void onPistonExtend(@Nonnull BlockPistonExtendEvent event) {
		for (Block block : event.getBlocks()) {
			if (cancelIfSkyMineBorder(event, block, true)) {
				return;
			}
		}
	}

	@EventHandler
	public void onBlockBurn(@Nonnull BlockBurnEvent event) {
		cancelIfSkyMineBorder(event, event.getBlock(), true);
	}

	@EventHandler
	public void onBlockFade(@Nonnull BlockFadeEvent event) {
		cancelIfSkyMineBorder(event, event.getBlock(), true);
	}

	@EventHandler
	public void onLeaveDecay(@Nonnull LeavesDecayEvent event) {
		cancelIfSkyMineBorder(event, event.getBlock(), true);
	}

	/**
	 * Cancels the specified event if the block is part of a skymine border.
	 *
	 * @param event the event to cancel
	 * @param block the block to check
	 * @param cancel whether to cancel the event
	 * @return true if the event would be cancelled
	 */
	private boolean cancelIfSkyMineBorder(@Nonnull Cancellable event, @Nonnull Block block, boolean cancel) {
		if (event.isCancelled()) {
			return true;
		}

		for (SkyMine skyMine : plugin.getSkyMineManager().getSkyMines()) {
			if (skyMine.getStructure().getBorderRegion().contains(Position.of(block.getLocation()))) {
				if (cancel) {
					event.setCancelled(true);
				}
				return true;
			}
		}
		return false;
	}
}
