package com.github.colingrime.cache;

public interface Cooldown {

	default boolean isCooldownFinished() {
		return getCooldownLeft() == 0;
	}

	/**
	 * @return time left on the cooldown (in seconds)
	 */
	long getCooldownLeft();

	/**
	 * Action to execute after cooldown is complete.
	 */
	void completionAction();

	/**
	 * Invalidates the cooldown.
	 */
	void invalidate();
}
