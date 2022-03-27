package com.github.scilldev.storage.database;

import com.github.scilldev.storage.MineData;

import java.sql.SQLException;

public interface Database {

	/**
	 * Sets up the table to keep track of versions / patches.
	 * @throws SQLException DB failed to load
	 */
	void setupVersionTable() throws SQLException;

	/**
	 * Checks for updates from the version table.
	 * @return true if an update is available
	 * @throws SQLException DB failed to load
	 */
	boolean checkForUpdates() throws SQLException;

	/**
	 * Performs the necessary updates.
	 */
	void performUpdates();

	/**
	 * Initializes the database.
	 * @throws SQLException DB failed to load
	 */
	void init() throws SQLException;

	/**
	 * @return mine data stored on the database
	 */
	MineData getMineData();

	/**
	 * Starts the timers necessary for saving data regularly, etc.
	 */
	void startTimers();
}
