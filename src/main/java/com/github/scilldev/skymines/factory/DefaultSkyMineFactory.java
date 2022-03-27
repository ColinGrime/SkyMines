package com.github.scilldev.skymines.factory;

import com.github.scilldev.skymines.DefaultSkyMine;
import com.github.scilldev.skymines.SkyMine;
import com.github.scilldev.skymines.upgrades.StandardUpgrades;
import com.github.scilldev.skymines.upgrades.Upgrades;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DefaultSkyMineFactory implements SkyMineFactory {

	private final Upgrades upgradeOptions;

	public DefaultSkyMineFactory() {
		this.upgradeOptions = new StandardUpgrades();
	}

	public DefaultSkyMineFactory(Upgrades upgradeOptions) {
		this.upgradeOptions = upgradeOptions;
	}

	@Override
	public SkyMine createSkyMine(Player owner, Location location) {
		int testLength = 15;
		int testWidth = 10;
		int testHeight = 5;

		float yaw = owner.getLocation().getYaw();
		if (yaw < 0) {
			yaw += 360;
		}

		Location endLoc;
		if (yaw >= 0 && yaw <= 35)
			endLoc = location.clone().add(-testWidth, -testHeight, testLength);
		else if (yaw >= 35 && yaw <= 90)
			endLoc = location.clone().add(-testLength, -testHeight, testWidth);
		else if (yaw >= 90 && yaw <= 135)
			endLoc = location.clone().add(-testLength, -testHeight, -testWidth);
		else if (yaw >= 135 && yaw <= 180)
			endLoc = location.clone().add(-testWidth, -testHeight, -testLength);
		else if (yaw >= 180 && yaw <= 225)
			endLoc = location.clone().add(testWidth, -testHeight, -testLength);
		else if (yaw >= 225 && yaw <= 270)
			endLoc = location.clone().add(testLength, -testHeight, -testWidth);
		else if (yaw >= 270 && yaw <= 325)
			endLoc = location.clone().add(testLength, -testHeight, testWidth);
		else
			endLoc = location.clone().add(testWidth, -testHeight, testLength);

		// builds parameter and gets blocks within it
		List<Block> blocksInside = buildParameter(location, endLoc);

		// creates the home of the mine
		Location home = location.clone().add(0, 1, 0);
		home.setYaw(yaw);

		// creates new sky mine
		return new DefaultSkyMine(owner.getUniqueId(), home, blocksInside, upgradeOptions);
	}

	/**
	 * Builds the parameter around two locations.
	 * Return blocks that weren't a part of the parameter.
	 *
	 * @param startLoc any location
	 * @param endLoc any location
	 * @return blocks inside the parameter
	 */
	private List<Block> buildParameter(Location startLoc, Location endLoc) {
		// all major points around the mine
		int x1 = startLoc.getBlockX();
		int y1 = startLoc.getBlockY();
		int z1 = startLoc.getBlockZ();
		int x2 = endLoc.getBlockX();
		int y2 = endLoc.getBlockY();
		int z2 = endLoc.getBlockZ();

		// lowest and highest points around the mine
		int lowX = Math.min(x1, x2);
		int lowY = Math.min(y1, y2);
		int lowZ = Math.min(z1, z2);
		int highX = lowX == x1 ? x2 : x1;
		int highY = lowY == y1 ? y2 : y1;
		int highZ = lowZ == z1 ? z2 : z1;

		World world = startLoc.getWorld();
		List<Block> blocksBetween = new ArrayList<>();

		for (int x=lowX; x<=highX; x++) {
			for (int y=lowY; y<=highY; y++) {
				for (int z=lowZ; z<=highZ; z++) {
					if (y == highY && x != x1 && x != x2 && z != z1 && z != z2) {
						blocksBetween.add(world.getBlockAt(x, y, z));
					} else if (x == x1 || x == x2 || y == y1 || y == y2 || z == z1 || z == z2) {
						world.getBlockAt(x, y, z).setType(Material.BEDROCK);
					} else {
						blocksBetween.add(world.getBlockAt(x, y, z));
					}
				}
			}
		}

		return blocksBetween;
	}
}
