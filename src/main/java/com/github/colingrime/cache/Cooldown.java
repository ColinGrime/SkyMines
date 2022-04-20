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
     * Message for if the cooldown is not finished.
     */
    String getDenyMessage();

    /**
     * Invalidates the cooldown.
     */
    void invalidate();
}
