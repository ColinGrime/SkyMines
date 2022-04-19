package com.github.colingrime.listeners;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Used to block access from breaking the parameter of a mine.
 */
public record ParameterListeners(SkyMines plugin) implements Listener {

    public ParameterListeners(SkyMines plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        blockAccessIfParameter(event.getBlock(), event);
    }

    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            blockAccessIfParameter(block, event);
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            blockAccessIfParameter(block, event);
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            blockAccessIfParameter(block, event);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        blockAccessIfParameter(event.getBlock(), event);
    }

    private void blockAccessIfParameter(Block block, Cancellable cancellableEvent) {
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