package com.github.colingrime.skymines.structure;

import com.github.colingrime.SkyMines;
import com.github.colingrime.config.BlockVariety;
import com.github.colingrime.skymines.structure.behavior.BuildBehavior;
import com.github.colingrime.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MineStructure {

	private final World world;
	private final Location startCorner;
	private final Location endCorner;
	private final MineSize size;

	// must hold references of parameter for the menu to open on right-click
	private Set<Vector> parameter = new HashSet<>();
	private Vector minInside, maxInside;

	public MineStructure(Location startCorner, Location endCorner, MineSize size) {
		this.world = startCorner.getWorld();
		this.startCorner = startCorner;
		this.endCorner = endCorner;
		this.size = size;
	}

	/**
	 * Gets the mine structure's parameter
	 * along with the blocks inside it.
	 */
	public void setup() {
		// all major points around the mine
		int x1 = startCorner.getBlockX();
		int y1 = startCorner.getBlockY();
		int z1 = startCorner.getBlockZ();
		int x2 = endCorner.getBlockX();
		int y2 = endCorner.getBlockY();
		int z2 = endCorner.getBlockZ();

		// lowest and highest points around the mine
		int lowX = Math.min(x1, x2);
		int lowY = Math.min(y1, y2);
		int lowZ = Math.min(z1, z2);
		int highX = lowX == x1 ? x2 : x1;
		int highY = lowY == y1 ? y2 : y1;
		int highZ = lowZ == z1 ? z2 : z1;

		// get minimum/maximum inside locations
		minInside = new Vector(lowX + 1, lowY + 1, lowZ + 1);
		maxInside = new Vector(highX - 1, highY, highZ - 1);

		for (int x=lowX; x<=highX; x++) {
			for (int y=lowY; y<=highY; y++) {
				for (int z=lowZ; z<=highZ; z++) {
					// it's part of the parameter
					if (x == x1 || x == x2 || y == y1 || y == y2 || z == z1 || z == z2) {
						// the top face is not part of the parameter
						if (!(y == highY && x != x1 && x != x2 && z != z1 && z != z2)) {
							parameter.add(new Vector(x, y, z));
						}
					}
				}
			}
		}
	}

	/**
	 * Checks if the player has access to build.
	 * Also checks if there are blocks in the way.
	 *
	 * @param player player to check against
	 * @return true if player can build here and there are no blocks in the way
	 */
	public boolean doBlockCheck(Player player) {
		if (!getBehavior().isClear(world, parameter)) {
			return false;
		}

		// all major points around the mine
		int x1 = startCorner.getBlockX();
		int y1 = startCorner.getBlockY();
		int z1 = startCorner.getBlockZ();
		int x2 = endCorner.getBlockX();
		int y2 = endCorner.getBlockY();
		int z2 = endCorner.getBlockZ();

		List<Location> locationsToCheck = new ArrayList<>();
		locationsToCheck.add(new Location(world, x1, y1, z1));
		locationsToCheck.add(new Location(world, x1, y1, z2));
		locationsToCheck.add(new Location(world, x1, y2, z1));
		locationsToCheck.add(new Location(world, x1, y2, z2));
		locationsToCheck.add(new Location(world, x2, y1, z1));
		locationsToCheck.add(new Location(world, x2, y1, z2));
		locationsToCheck.add(new Location(world, x2, y2, z1));
		locationsToCheck.add(new Location(world, x2, y2, z2));

		for (Location location : locationsToCheck) {
			Block block = location.getBlock();
			BlockPlaceEvent fakeEvent = new BlockPlaceEvent(block, block.getState(), block, player.getInventory().getItemInMainHand(), player, true, EquipmentSlot.HAND);
			Bukkit.getPluginManager().callEvent(fakeEvent);

			// player can't build here
			if (fakeEvent.isCancelled()) {
				return false;
			}
		}

		return true;
	}

	public void buildParameter() {
		getBehavior().buildParameter(world, parameter, Material.BEDROCK);
	}

	public void buildInside(BlockVariety blockVariety) {
		getBehavior().buildInside(world, minInside, maxInside, blockVariety, SkyMines.getInstance().getSettings().shouldReplaceBlocks());
	}

	public void destroy() {
		getBehavior().buildParameter(world, parameter, Material.AIR);
		getBehavior().buildInside(world, minInside, maxInside, Material.AIR);
		parameter = null;
	}

	public BuildBehavior getBehavior() {
		return SkyMines.getInstance().getDependencyManager().getBuildbehavior();
	}

	public Set<Vector> getParameter() {
		return parameter;
	}

	public MineSize getSize() {
		return size;
	}

	public static String parse(MineStructure structure) {
		String startCorner = Utils.parseLocation(structure.startCorner);
		String endCorner = Utils.parseLocation(structure.endCorner);
		String size = MineSize.parse(structure.size);
		return startCorner + '\n' + endCorner + '\n' + size;
	}

	public static MineStructure parse(String text) {
		String[] texts = text.split("\n");
		if (texts.length != 3) {
			return null;
		}

		Location startCorner = Utils.parseLocation(texts[0]);
		Location endCorner = Utils.parseLocation(texts[1]);
		MineSize size = MineSize.parse(texts[2]);

		if (startCorner == null || endCorner == null || size == null) {
			return null;
		}

		return new MineStructure(startCorner, endCorner, size);
	}
}
