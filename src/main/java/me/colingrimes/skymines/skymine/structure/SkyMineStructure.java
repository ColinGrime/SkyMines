package me.colingrimes.skymines.skymine.structure;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.geometry.Region;
import me.colingrimes.midnight.geometry.Size;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.misc.Validator;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.structure.border.BorderRegion;
import me.colingrimes.skymines.skymine.structure.material.MineMaterialStatic;
import me.colingrimes.skymines.skymine.structure.material.MineMaterial;
import me.colingrimes.skymines.skymine.structure.behavior.BuildBehavior;
import me.colingrimes.skymines.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
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

public class SkyMineStructure extends Region implements Serializable {

	private final Region inner;
	private final BorderRegion border;
	private final Material borderType;

	public SkyMineStructure(@Nonnull Position pos1, @Nonnull Position pos2, @Nonnull Material borderType) {
		super(pos1, pos2);
		this.inner = Region.of(min.add(1, 1, 1), max.subtract(1, 0, 1));
		this.border = new BorderRegion(pos1, pos2);
		this.borderType = borderType;
	}

	/**
	 * Checks if the player can build this structure.
	 * <p>
	 * This method performs a number of checks to determine whether the mine can be safely built.
	 * It does <b>not</b> guarantee absolute validity, as it does not check every block within the region.
	 * <p>
	 * The following checks are performed:
	 * <ul>
	 *     <li>All blocks must be air or transparent blocks.</li>
	 *     <li>15 critical points within the mine are validated for player access:
	 *         <ul>
	 *             <li>8 corners of the region</li>
	 *             <li>6 face centers (center of each wall)</li>
	 *             <li>1 center point (middle of the entire region)</li>
	 *         </ul>
	 *     </li>
	 * </ul>
	 */
	public boolean canBuild(@Nonnull Player player) {
		if (!getBehavior().canBuild(this)) {
			return false;
		}

		List<Position> critical = new ArrayList<>();

		int x1 = min.getBlockX(), y1 = min.getBlockY(), z1 = min.getBlockZ();
		int x2 = max.getBlockX(), y2 = max.getBlockY(), z2 = max.getBlockZ();
		int midX = (x1 + x2) / 2;
		int midY = (y1 + y2) / 2;
		int midZ = (z1 + z2) / 2;

		// 8 corners
		critical.add(Position.of(getWorld(), x1, y1, z1));
		critical.add(Position.of(getWorld(), x1, y1, z2));
		critical.add(Position.of(getWorld(), x1, y2, z1));
		critical.add(Position.of(getWorld(), x1, y2, z2));
		critical.add(Position.of(getWorld(), x2, y1, z1));
		critical.add(Position.of(getWorld(), x2, y1, z2));
		critical.add(Position.of(getWorld(), x2, y2, z1));
		critical.add(Position.of(getWorld(), x2, y2, z2));

		// 6 face centers
		critical.add(Position.of(getWorld(), midX, y1, midZ));
		critical.add(Position.of(getWorld(), midX, y2, midZ));
		critical.add(Position.of(getWorld(), x1, midY, midZ));
		critical.add(Position.of(getWorld(), x2, midY, midZ));
		critical.add(Position.of(getWorld(), midX, midY, z1));
		critical.add(Position.of(getWorld(), midX, midY, z2));

		// 1 center
		critical.add(Position.of(getWorld(), midX, midY, midZ));

		// Validate for player access.
		for (Position pos : critical) {
			Block block = pos.toBlock();
			BlockPlaceEvent fakeEvent = new BlockPlaceEvent(block, block.getState(), block, new ItemStack(Material.STONE), player, true, EquipmentSlot.HAND);
			Common.call(fakeEvent);

			// Player has no permission to build here.
			if (fakeEvent.isCancelled()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Builds the entire mine structure.
	 * This includes the inner region and the borders of the mine.
	 *
	 * @param material the material of the inner region
	 */
	public void build(@Nonnull MineMaterial material) {
		buildMine(material);
		buildBorders();
	}

	/**
	 * Builds the inner region of the mine.
	 *
	 * @param material the material to build
	 */
	public void buildMine(@Nonnull MineMaterial material) {
		getBehavior().build(inner, material, Settings.OPTION_RESET_REPLACE_BLOCKS.get());
	}

	/**
	 * Builds the borders of the mine.
	 */
	public void buildBorders() {
		getBehavior().build(border, new MineMaterialStatic(borderType));
	}

	/**
	 * Destroys the entire {@link SkyMineStructure} including the inner mine and the 5 borders.
	 */
	public void destroy() {
		getBehavior().build(this, new MineMaterialStatic(Material.AIR));
	}

	/**
	 * Gets the inner region of the mine.
	 *
	 * @return the inner region
	 */
	@Nonnull
	public Region getInnerRegion() {
		return inner;
	}

	/**
	 * Gets the region that represents the 5 borders of the mine.
	 *
	 * @return the border region
	 */
	@Nonnull
	public BorderRegion getBorderRegion() {
		return border;
	}

	/**
	 * Gets the border material type.
	 *
	 * @return the border type
	 */
	@Nonnull
	public Material getBorderType() {
		return borderType;
	}

	/**
	 * Gets the mine size.
	 * The mine size is equal to the size of the inner region.
	 *
	 * @return the mine size
	 */
	@Nonnull
	public Size getMineSize() {
		return inner.getSize();
	}

	/**
	 * Gets the build behavior that will be used to construct the regions.
	 *
	 * @return the build behavior
	 */
	@Nonnull
	private BuildBehavior getBehavior() {
		return SkyMines.getInstance().getBuildbehavior();
	}

	@Nonnull
	@Override
	public JsonElement serialize() {
		return Json.of(super.serialize()).add("borderType", borderType.name()).build();
	}

	@Nonnull
	public static SkyMineStructure deserialize(@Nonnull JsonElement element) {
		JsonObject object = Validator.checkJson(element, "min", "max", "borderType");
		Position min = Position.deserialize(object.get("min"));
		Position max = Position.deserialize(object.get("max"));
		Material borderType = Material.getMaterial(object.get("borderType").getAsString());
		return new SkyMineStructure(min, max, Objects.requireNonNull(borderType));
	}

	/**
	 * Deserializes the string into a mine structure.
	 *
	 * @param text the text to parse
	 * @return the mine structure if available
	 */
	@Deprecated
	@Nullable
	public static SkyMineStructure deserializeOld(@Nullable String text) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		String[] texts = text.split("\n");
		if (texts.length < 4) {
			return null;
		}

		Location startCorner = Utils.deserializeLocation(texts[0]);
		Location endCorner = Utils.deserializeLocation(texts[1]);
		// Size size = Size.of(texts[2]);
		Material borderType = Material.getMaterial(texts[3]);
		if (startCorner == null || endCorner == null || borderType == null) {
			return null;
		}

		return new SkyMineStructure(Position.of(startCorner), Position.of(endCorner), borderType);
	}
}
