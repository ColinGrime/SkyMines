package me.colingrimes.skymines.task;

import me.colingrimes.midnight.scheduler.task.Task;
import me.colingrimes.skymines.skymine.structure.model.BlockInfo;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class BuildTask implements Consumer<Task> {

	private static final int MAX_MILLIS_PER_TICK = 15;
	private final Deque<BlockInfo> blocksToPlace = new ArrayDeque<>();

	@Override
	public void accept(@Nonnull Task task) {
		long stopTime = System.currentTimeMillis() + MAX_MILLIS_PER_TICK;

		BlockInfo blockInfo;
		while (System.currentTimeMillis() <= stopTime && (blockInfo = blocksToPlace.poll()) != null) {
			blockInfo.getBlock().setType(blockInfo.getType());
		}

		// nothing left to place, cancel task
		if (blocksToPlace.isEmpty()) {
			task.stop();
		}
	}

	@Nonnull
	public Deque<BlockInfo> getBlocksToPlace() {
		return blocksToPlace;
	}
}
