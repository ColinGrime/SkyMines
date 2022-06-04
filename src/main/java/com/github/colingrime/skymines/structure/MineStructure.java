package com.github.colingrime.skymines.structure;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.structure.behavior.BuildBehavior;
import com.github.colingrime.skymines.structure.material.MaterialSingle;
import com.github.colingrime.skymines.structure.material.MaterialType;
import com.github.colingrime.skymines.structure.region.implementation.CuboidRegion;
import com.github.colingrime.skymines.structure.region.implementation.ParameterRegion;
import com.github.colingrime.utils.LocationUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MineStructure {

	private final World world;
	private final Location startCorner;
	private final Location endCorner;
	private final MineSize size;
	private final Material borderType;

	private final ParameterRegion parameter;
	private final CuboidRegion inside;

	// if they have WG regions, they can hook into it
	private ProtectedRegion worldGuardRegion;

	public MineStructure(Location startCorner, Location endCorner, MineSize size, Material borderType) {
		this.world = startCorner.getWorld();
		this.startCorner = startCorner;
		this.endCorner = endCorner;
		this.size = size;
		this.borderType = borderType;
		this.parameter = ParameterRegion.of(startCorner.toVector(), endCorner.toVector());
		this.inside = _getInside();
	}

	/**
	 * Gets the mine structure's inside region.
	 */
	private CuboidRegion _getInside() {
		int x1 = startCorner.getBlockX();
		int y1 = startCorner.getBlockY();
		int z1 = startCorner.getBlockZ();
		int x2 = endCorner.getBlockX();
		int y2 = endCorner.getBlockY();
		int z2 = endCorner.getBlockZ();

		int minX = Math.min(x1, x2);
		int minY = Math.min(y1, y2);
		int minZ = Math.min(z1, z2);
		int maxX = minX == x1 ? x2 : x1;
		int maxY = minY == y1 ? y2 : y1;
		int maxZ = minZ == z1 ? z2 : z1;

		Vector pt1 = new Vector(minX + 1, minY + 1, minZ + 1);
		Vector pt2 = new Vector(maxX - 1, maxY, maxZ - 1);

		return CuboidRegion.of(pt1, pt2);
	}

	/**
	 * Checks for the WorldGuard hook.
	 *
	 * If it exists, a WorldGuard region
	 * gets created inside the mine.
	 *
	 * This allows us other plugins to interact
	 * with the inner mine (e.g. TokenEnchants).
	 *
	 * @param mineUUID UUID of the mine
	 */
	public void checkForWorldGuardHook(String mineUUID) {
		RegionContainer container = SkyMines.getInstance().getDependencyManager().getRegionContainer();
		if (container == null) {
			return;
		}

		com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(this.world);
		RegionManager manager = container.get(world);
		if (manager == null) {
			return;
		}

		worldGuardRegion = manager.getRegion(mineUUID);
		if (worldGuardRegion != null) {
			return;
		}

		BlockVector3 vector1 = BlockVector3.at(startCorner.getX(), startCorner.getY(), startCorner.getZ());
		BlockVector3 vector2 = BlockVector3.at(endCorner.getX(), endCorner.getY(), endCorner.getZ());
		worldGuardRegion = new ProtectedCuboidRegion(mineUUID, vector1, vector2);
		worldGuardRegion.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
		manager.addRegion(worldGuardRegion);
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
			BlockPlaceEvent fakeEvent = new BlockPlaceEvent(block, block.getState(), block, new ItemStack(Material.STONE), player, true, EquipmentSlot.HAND);
			Bukkit.getPluginManager().callEvent(fakeEvent);

			// player can't build here
			if (fakeEvent.isCancelled()) {
				return false;
			}
		}

		return true;
	}

	public void buildParameter() {
		getBehavior().build(world, parameter, new MaterialSingle(borderType));
	}

	public void buildInside(MaterialType materialType) {
		getBehavior().build(world, inside, materialType, SkyMines.getInstance().getSettings().shouldReplaceBlocks());
	}

	public void destroy(String mineUUID) {
		MaterialType air = new MaterialSingle(Material.AIR);
		getBehavior().build(world, parameter, air);
		getBehavior().build(world, inside, air);

		// destroy WG region if it exists
		if (worldGuardRegion != null) {
			RegionContainer container = SkyMines.getInstance().getDependencyManager().getRegionContainer();
			com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(this.world);
			RegionManager manager = container.get(world);

			if (manager != null) {
				manager.removeRegion(mineUUID);
			}
		}
	}

	private BuildBehavior getBehavior() {
		return SkyMines.getInstance().getDependencyManager().getBuildbehavior();
	}

	public MineSize getSize() {
		return size;
	}

	public Material getBorderType() {
		return borderType;
	}

	public ParameterRegion getParameter() {
		return parameter;
	}

	public CuboidRegion getInside() {
		return inside;
	}

	public static String serialize(MineStructure structure) {
		String startCorner = LocationUtils.serializeLocation(structure.startCorner);
		String endCorner = LocationUtils.serializeLocation(structure.endCorner);
		String size = MineSize.serialize(structure.size);
		String borderType = structure.borderType.name();
		return startCorner + '\n' + endCorner + '\n' + size + '\n' + borderType;
	}

	public static MineStructure deserialize(String text) {
		String[] texts = text.split("\n");
		if (texts.length < 3) {
			return null;
		}

		Optional<Location> startCorner = LocationUtils.deserializeLocation(texts[0]);
		Optional<Location> endCorner = LocationUtils.deserializeLocation(texts[1]);
		MineSize size = MineSize.deserialize(texts[2]);
		Material borderType = texts.length == 4 ? Material.getMaterial(texts[3]) : Material.BEDROCK;

		if (startCorner.isEmpty() || endCorner.isEmpty() || size == null) {
			return null;
		}

		return new MineStructure(startCorner.get(), endCorner.get(), size, borderType);
	}
}
