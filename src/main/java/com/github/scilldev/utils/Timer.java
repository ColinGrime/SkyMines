package com.github.scilldev.utils;

public final class Timer {

	private Timer() {}

	/**
	 * Times a method.
	 * @param start message sent on start
	 * @param action any method you want timed
	 * @param complete message sent on complete
	 */
	public static void time(String start, Action action, String complete) {
		Logger.log(start);
		long time = System.currentTimeMillis();
		action.run();
		Logger.log(String.format(complete, System.currentTimeMillis() - time));
	}

	@FunctionalInterface
	public interface Action {
		void run();
	}
}
