package com.github.colingrime.skymines.structure;

import com.github.colingrime.SkyMines;
import com.github.colingrime.config.BlockVariety;
import com.github.colingrime.skymines.structure.behavior.BuildBehavior;
import com.github.colingrime.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MineStructure {

	private final World world;
	private final Location startCorner;
	private final Location endCorner;
	private final MineSize size;

	private final List<Vector> parameter = new ArrayList<>();
	private final List<Vector> blocksInside = new ArrayList<>();

	public MineStructure(Location startCorner, Location endCorner, MineSize size) {
		this.world = startCorner.getWorld();
		this.startCorner = startCorner;
		this.endCorner = endCorner;
		this.size = size;
	}

	/**
	 * Gets the mine structure's parameter
	 * along with the blocks inside it.
	 *
	 * @param checkForBlockage checks if there's blocks in the way (will cost more performance-wise)
	 * @return true if there were no blocks in the way of the structure
	 */
	public boolean setup(boolean checkForBlockage) {
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

		for (int x=lowX; x<=highX; x++) {
			for (int y=lowY; y<=highY; y++) {
				for (int z=lowZ; z<=highZ; z++) {
					Vector vector = new Vector(x, y, z);

					// don't build around top face
					if (y == highY && x != x1 && x != x2 && z != z1 && z != z2) {
						blocksInside.add(vector);
					} else if (x == x1 || x == x2 || y == y1 || y == y2 || z == z1 || z == z2) {
						parameter.add(vector);
					} else {
						blocksInside.add(vector);
					}
				}
			}
		}

		if (checkForBlockage) {
			return getBehavior().isClear(world, parameter);
		}

		return true;
	}

	public void buildParameter() {
		getBehavior().build(world, parameter, Material.BEDROCK);
	}

	public void buildInside(BlockVariety blockVariety) {
		getBehavior().build(world, blocksInside, blockVariety);
	}

	public void destroy() {
		// TODO refactor class
		getBehavior().build(world, parameter, Material.AIR);
		getBehavior().build(world, blocksInside, Material.AIR);
	}

	public BuildBehavior getBehavior() {
		return SkyMines.getInstance().getDependencyManager().getBuildbehavior();
	}

	public List<Vector> getParameter() {
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

		MineStructure structure = new MineStructure(startCorner, endCorner, size);
		Bukkit.getScheduler().runTaskAsynchronously(SkyMines.getInstance(), () -> structure.setup(false));
		return structure;
	}
}
