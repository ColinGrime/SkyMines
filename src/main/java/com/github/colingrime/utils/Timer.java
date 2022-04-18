package com.github.colingrime.utils;

public final class Timer {

    private Timer() {
    }

    /**
     * Times a method.
     *
     * @param action   any method you want timed
     * @param complete message sent on complete
     */
    public static void time(Action action, String complete) throws Exception {
        long time = System.currentTimeMillis();
        action.run();
        Logger.log(String.format(complete, System.currentTimeMillis() - time));
    }

    @FunctionalInterface
    public interface Action {
        void run() throws Exception;
    }
}
