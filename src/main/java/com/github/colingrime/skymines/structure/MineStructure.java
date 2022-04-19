package com.github.colingrime.skymines.structure;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.structure.behavior.BuildBehavior;
import com.github.colingrime.skymines.structure.material.MaterialSingle;
import com.github.colingrime.skymines.structure.material.MaterialType;
import com.github.colingrime.skymines.structure.material.MaterialVariety;
import com.github.colingrime.skymines.structure.region.CuboidRegion;
import com.github.colingrime.skymines.structure.region.ParameterRegion;
import com.github.colingrime.utils.Utils;
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

public class MineStructure {

    private final World world;
    private final Location startCorner;
    private final Location endCorner;
    private final MineSize size;
    private final Material borderType;

    private final ParameterRegion parameter;
    private final CuboidRegion inside;

    public MineStructure(Location startCorner, Location endCorner, MineSize size, Material borderType) {
        this.world = startCorner.getWorld();
        this.startCorner = startCorner;
        this.endCorner = endCorner;
        this.size = size;
        this.borderType = borderType;
        this.parameter = new ParameterRegion(startCorner.toVector(), endCorner.toVector());
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

        CuboidRegion inside = new CuboidRegion();
        inside.setMin(new Vector(minX + 1, minY + 1, minZ + 1));
        inside.setMax(new Vector(maxX - 1, maxY, maxZ - 1));
        return inside;
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

    public static String serialize(MineStructure structure) {
        String startCorner = Utils.parseLocation(structure.startCorner);
        String endCorner = Utils.parseLocation(structure.endCorner);
        String size = MineSize.parse(structure.size);
        String borderType = structure.borderType.name();
        return startCorner + '\n' + endCorner + '\n' + size + '\n' + borderType;
    }

    public void buildInside(MaterialVariety blockVariety) {
        getBehavior().build(world, inside, blockVariety, SkyMines.getInstance().getSettings().shouldReplaceBlocks());
    }

    public void destroy() {
        MaterialType air = new MaterialSingle(Material.AIR);
        getBehavior().build(world, parameter, air);
        getBehavior().build(world, inside, air);
    }

    private BuildBehavior getBehavior() {
        return SkyMines.getInstance().getDependencyManager().getBuildbehavior();
    }

    public MineSize getSize() {
        return size;
    }

    public static MineStructure deserialize(String text) {
        String[] texts = text.split("\n");
        if (texts.length < 3) {
            return null;
        }

        Location startCorner = Utils.parseLocation(texts[0]);
        Location endCorner = Utils.parseLocation(texts[1]);
        MineSize size = MineSize.parse(texts[2]);
        Material borderType = texts.length == 4 ? Material.getMaterial(texts[3]) : Material.BEDROCK;

        if (startCorner == null || endCorner == null || size == null) {
            return null;
        }

        return new MineStructure(startCorner, endCorner, size, borderType);
    }

    public ParameterRegion getParameter() {
        return parameter;
    }

    public CuboidRegion getInside() {
        return inside;
    }

    public void buildParameter() {
        getBehavior().build(world, parameter, new MaterialSingle(borderType));
    }

    public Material getBorderType() {
        return borderType;
    }
}