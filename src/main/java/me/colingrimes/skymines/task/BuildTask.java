package me.colingrimes.skymines.task;

import me.colingrimes.midnight.model.DeferredBlock;
import me.colingrimes.midnight.scheduler.task.Task;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class BuildTask implements Consumer<Task> {

	private static final int MAX_MILLIS_PER_TICK = 15;
	private final Deque<DeferredBlock> blocksToPlace = new ArrayDeque<>();

	@Override
	public void accept(@Nonnull Task task) {
		long stopTime = System.currentTimeMillis() + MAX_MILLIS_PER_TICK;

		DeferredBlock block;
		while (System.currentTimeMillis() <= stopTime && (block = blocksToPlace.poll()) != null) {
			block.apply();
		}

		// nothing left to place, cancel task
		if (blocksToPlace.isEmpty()) {
			task.stop();
		}
	}

	@Nonnull
	public Deque<DeferredBlock> getBlocksToPlace() {
		return blocksToPlace;
	}
}
