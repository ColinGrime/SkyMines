package me.colingrimes.skymines.skymine.structure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.geometry.Size;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.misc.Validator;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.structure.material.MineMaterialStatic;
import me.colingrimes.skymines.skymine.structure.material.MineMaterial;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MineStructure implements Serializable {

	private final World world;
	private final Position corner1;
	private final Position corner2;
	private final Size mineSize;
	private final Material borderType;

	private final ParameterRegion parameter;
	private final CuboidRegion inside;

	public MineStructure(@Nonnull Position corner1, @Nonnull Position corner2, @Nonnull Size mineSize, @Nonnull Material borderType) {
		this.world = corner1.getWorld();
		this.corner1 = corner1;
		this.corner2 = corner2;
		this.mineSize = mineSize;
		this.borderType = borderType;
		this.parameter = new ParameterRegion(corner1, corner2);
		this.inside = new CuboidRegion(parameter.getMin().add(1, 1, 1), parameter.getMax().subtract(1, 0, 1));
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
		int x1 = corner1.getBlockX();
		int y1 = corner1.getBlockY();
		int z1 = corner1.getBlockZ();
		int x2 = corner2.getBlockX();
		int y2 = corner2.getBlockY();
		int z2 = corner2.getBlockZ();

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
		getBehavior().build(world, parameter, new MineMaterialStatic(borderType));
	}

	public void buildInside(@Nonnull MineMaterial blockVariety) {
		getBehavior().build(world, inside, blockVariety, Settings.OPTIONS_REPLACE_BLOCKS.get());
	}

	public void destroy() {
		MineMaterial air = new MineMaterialStatic(Material.AIR);
		getBehavior().build(world, parameter, air);
		getBehavior().build(world, inside, air);
	}

	@Nonnull
	private BuildBehavior getBehavior() {
		return SkyMines.getInstance().getBuildbehavior();
	}

	@Nonnull
	public Size getMineSize() {
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

	@Nonnull
	@Override
	public JsonElement serialize() {
		return Json.create()
				.add("corner1", corner1.serialize())
				.add("corner2", corner2.serialize())
				.add("mineSize", mineSize.serialize())
				.add("borderType", borderType.name())
				.build();
	}

	@Nonnull
	public static MineStructure deserialize(@Nonnull JsonElement element) {
		JsonObject object = Validator.checkJson(element, "corner1", "corner2", "mineSize", "borderType");
		Position corner1 = Position.deserialize(object.get("corner1"));
		Position corner2 = Position.deserialize(object.get("corner2"));
		Size mineSize = Size.deserialize(object.get("mineSize"));
		Material borderType = Material.getMaterial(object.get("borderType").getAsString());
		return new MineStructure(corner1, corner2, mineSize, Objects.requireNonNull(borderType));
	}

	/**
	 * Deserializes the string into a mine structure.
	 *
	 * @param text the text to parse
	 * @return the mine structure if available
	 */
	@Deprecated
	@Nullable
	public static MineStructure deserializeOld(@Nullable String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		String[] texts = text.split("\n");
		if (texts.length < 4) {
			return null;
		}

		Location startCorner = Utils.parseLocation(texts[0]);
		Location endCorner = Utils.parseLocation(texts[1]);
		Size size = Size.of(texts[2]);
		Material borderType = Material.getMaterial(texts[3]);
		if (startCorner == null || endCorner == null || size == null || borderType == null) {
			return null;
		}

		return new MineStructure(Position.of(startCorner), Position.of(endCorner), size, borderType);
	}
}
