package me.colingrimes.skymines.skymine.structure;

import me.colingrimes.midnight.util.Common;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.structure.material.MaterialSingle;
import me.colingrimes.skymines.skymine.structure.material.MaterialType;
import me.colingrimes.skymines.skymine.structure.material.MaterialVariety;
import me.colingrimes.skymines.skymine.structure.behavior.BuildBehavior;
import me.colingrimes.skymines.skymine.structure.region.implementation.CuboidRegion;
import me.colingrimes.skymines.skymine.structure.region.implementation.ParameterRegion;
import me.colingrimes.skymines.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MineStructure {

	private final World world;
	private final Location startCorner;
	private final Location endCorner;
	private final MineSize mineSize;
	private final Material borderType;

	private final ParameterRegion parameter;
	private final CuboidRegion inside;

	public MineStructure(@Nonnull Location startCorner, @Nonnull Location endCorner, @Nonnull MineSize mineSize, @Nonnull Material borderType) {
		this.world = startCorner.getWorld();
		this.startCorner = startCorner;
		this.endCorner = endCorner;
		this.mineSize = mineSize;
		this.borderType = borderType;
		this.parameter = new ParameterRegion(startCorner.toVector(), endCorner.toVector());
		this.inside = new CuboidRegion(parameter.getMin().add(new Vector(1, 1, 1)), parameter.getMax().subtract(new Vector(1, 0, 1)));
	}

	/**
	 * Checks if the player has access to build.
	 * Also checks if there are blocks in the way.
	 *
	 * @param player player to check against
	 * @return true if player can build here and there are no blocks in the way
	 */
	public boolean doBlockCheck(@Nonnull Player player) {
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
			Common.call(fakeEvent);

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

	public void buildInside(@Nonnull MaterialVariety blockVariety) {
		getBehavior().build(world, inside, blockVariety, Settings.OPTIONS_REPLACE_BLOCKS.get());
	}

	public void destroy() {
		MaterialType air = new MaterialSingle(Material.AIR);
		getBehavior().build(world, parameter, air);
		getBehavior().build(world, inside, air);
	}

	@Nonnull
	private BuildBehavior getBehavior() {
		return SkyMines.getInstance().getBuildbehavior();
	}

	@Nonnull
	public MineSize getMineSize() {
		return mineSize;
	}

	@Nonnull
	public Material getBorderType() {
		return borderType;
	}

	@Nonnull
	public ParameterRegion getParameter() {
		return parameter;
	}

	@Nonnull
	public CuboidRegion getInside() {
		return inside;
	}

	/**
	 * Serializes the mine structure into a string.
	 *
	 * @return a string representing the mine structure
	 */
	@Nonnull
	public String serialize() {
		String corner1 = Utils.parseLocation(startCorner);
		String corner2 = Utils.parseLocation(endCorner);
		String size = mineSize.serialize();
		String border = borderType.name();
		return corner1 + '\n' + corner2 + '\n' + size + '\n' + border;
	}

	/**
	 * Deserializes the string into a mine structure.
	 *
	 * @param text the text to parse
	 * @return the mine structure if available
	 */
	@Nullable
	public static MineStructure deserialize(@Nullable String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		String[] texts = text.split("\n");
		if (texts.length < 4) {
			return null;
		}

		Location startCorner = Utils.parseLocation(texts[0]);
		Location endCorner = Utils.parseLocation(texts[1]);
		MineSize size = MineSize.deserialize(texts[2]);
		Material borderType = Material.getMaterial(texts[3]);
		if (startCorner == null || endCorner == null || size == null || borderType == null) {
			return null;
		}

		return new MineStructure(startCorner, endCorner, size, borderType);
	}
}
