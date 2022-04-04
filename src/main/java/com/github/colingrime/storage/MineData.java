package com.github.colingrime.storage;

import java.util.UUID;

public interface MineData {

	/**
	 * Loads mine data.
	 * @return amount of mines loaded.
	 */
	int loadMines();

	/**
	 * Saves mine data.
	 * @return amount of mines saved.
	 */
	int saveMines();

	/**
	 * Saves a single mine's data.
	 * @param uuid uuid of mine
	 * @return true if mine data was saved successfully
	 */
	boolean saveMine(UUID uuid);
}
