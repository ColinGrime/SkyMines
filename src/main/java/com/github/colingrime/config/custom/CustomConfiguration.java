package com.github.colingrime.config.custom;

public interface CustomConfiguration {

	/**
	 * @return name of the custom config
	 */
	String getName();

	/**
	 * Reloads the custom config file.
	 */
	void reload();
}
