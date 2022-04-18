package com.github.colingrime.storage;

import com.github.colingrime.skymines.SkyMine;

public interface Storage {

    /**
     * Initializes the storage.
     */
    void init() throws Exception;

    /**
     * Shuts down the storage.
     */
    void shutdown();

    /**
     * Loads mine data.
     *
     * @throws Exception loading failed
     */
    void loadMines() throws Exception;

    /**
     * Saves a single mine's data.
     *
     * @param skyMine any skymine
     * @throws Exception saving failed
     */
    void saveMine(SkyMine skyMine) throws Exception;

    /**
     * Deletes a single mine's data.
     *
     * @param skyMine any skymine
     * @throws Exception deleting failed
     */
    void deleteMine(SkyMine skyMine) throws Exception;
}
