package com.github.scilldev.skymines.factory;

import com.github.scilldev.skymines.SkyMine;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface SkyMineFactory {

	/**
	 * Creates a sky mine at the given location.
	 *
	 * @param owner player who placed the sky mine down
	 * @param location location of the sky mine
	 * @param length length of the sky mine
	 * @param width width of the ksy mine
	 * @param height height of the sky mine
	 * @return created SkyMine
	 */
	SkyMine createSkyMine(Player owner, Location location, int length, int width, int height);
}
