package com.github.colingrime.tasks;

import com.github.colingrime.skymines.structure.model.BlockInfo;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;
import java.util.Deque;

public class BuildTask extends BukkitRunnable {

    private static final int MAX_MILLIS_PER_TICK = 15;
    private final Deque<BlockInfo> blocksToPlace = new ArrayDeque<>();

    @Override
    public void run() {
        long stopTime = System.currentTimeMillis() + MAX_MILLIS_PER_TICK;

        BlockInfo blockInfo;
        while (System.currentTimeMillis() <= stopTime && (blockInfo = blocksToPlace.poll()) != null) {
            blockInfo.getBlock().setType(blockInfo.getType());
        }

        // nothing left to place, cancel task
        if (blocksToPlace.isEmpty()) {
            cancel();
        }
    }

    public Deque<BlockInfo> getBlocksToPlace() {
        return blocksToPlace;
    }
}
