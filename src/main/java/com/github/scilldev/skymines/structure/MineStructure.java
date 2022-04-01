package com.github.scilldev.skymines.structure;

import com.github.scilldev.config.BlockVariety;
import com.github.scilldev.skymines.structure.behavior.BuildBehavior;
import com.github.scilldev.skymines.structure.behavior.DefaultBuildBehavior;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineStructure {

	private final Location startCorner;
	private final Location endCorner;
	private final MineSize size;
	private final BuildBehavior buildBehavior;

	private final List<Block> parameter = new ArrayList<>();
	private final List<Block> blocksInside = new ArrayList<>();

	public MineStructure(Location startCorner, Location endCorner, MineSize size) {
		this(startCorner, endCorner, size, new DefaultBuildBehavior());
	}

	public MineStructure(Location startCorner, Location endCorner, MineSize size, BuildBehavior buildBehavior) {
		this.startCorner = startCorner;
		this.endCorner = endCorner;
		this.size = size;
		this.buildBehavior = buildBehavior;
	}

	/**
	 * Gets the mine structure's parameter
	 * along with the blocks inside it.
	 *
	 * @return true if there were no blocks in the way of the structure
	 */
	public boolean setup() {
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

		World world = startCorner.getWorld();
		for (int x=lowX; x<=highX; x++) {
			for (int y=lowY; y<=highY; y++) {
				for (int z=lowZ; z<=highZ; z++) {
					Block block = world.getBlockAt(x, y, z);

					// there's blocks in the way
					if (block.getType() != Material.AIR) {
						return false;
					}

					// don't build around top face
					if (y == highY && x != x1 && x != x2 && z != z1 && z != z2) {
						blocksInside.add(block);
					} else if (x == x1 || x == x2 || y == y1 || y == y2 || z == z1 || z == z2) {
						parameter.add(block);
					} else {
						blocksInside.add(block);
					}
				}
			}
		}

		return true;
	}

	public void buildParameter() {
		Map<Block, Material> blocksToChange = new HashMap<>();
		for (Block block : parameter) {
			blocksToChange.put(block, Material.BEDROCK);
		}
		buildBehavior.build(blocksToChange);
	}

	public void buildInside(BlockVariety blockVariety) {
		Map<Block, Material> blocksToChange = new HashMap<>();
		for (Block block : blocksInside) {
			blocksToChange.put(block, blockVariety.getRandom().orElse(Material.STONE));
		}
		buildBehavior.build(blocksToChange);
	}

	public List<Block> getParameter() {
		return parameter;
	}

	public List<Block> getBlocksInside() {
		return blocksInside;
	}

	public MineSize getSize() {
		return size;
	}
}
