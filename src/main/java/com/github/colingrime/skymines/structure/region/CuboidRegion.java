package com.github.colingrime.skymines.structure.region;

import com.github.colingrime.skymines.structure.region.functional.TriPredicate;
import org.bukkit.util.Vector;

public class CuboidRegion implements Region {

    private Vector min;
    private Vector max;

    public CuboidRegion() {
    }

    public CuboidRegion(Vector pt1, Vector pt2) {
        setPoints(pt1, pt2);
    }

    @Override
    public void setPoints(Vector pt1, Vector pt2) {
        int x1 = pt1.getBlockX();
        int y1 = pt1.getBlockY();
        int z1 = pt1.getBlockZ();
        int x2 = pt2.getBlockX();
        int y2 = pt2.getBlockY();
        int z2 = pt2.getBlockZ();

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = minX == x1 ? x2 : x1;
        int maxY = minY == y1 ? y2 : y1;
        int maxZ = minZ == z1 ? z2 : z1;

        min = new Vector(minX, minY, minZ);
        max = new Vector(maxX, maxY, maxZ);
    }

    @Override
    public boolean contains(Vector pt) {
        return containsWithin(pt, 0);
    }

    @Override
    public boolean containsWithin(Vector pt, int blocksAway) {
        int x = pt.getBlockX();
        int y = pt.getBlockY();
        int z = pt.getBlockZ();

        return x >= min.getBlockX() - blocksAway && x <= max.getBlockX() + blocksAway
                && y >= min.getBlockY() - blocksAway && y <= max.getBlockY() + blocksAway
                && z >= min.getBlockZ() - blocksAway && z <= max.getBlockZ() + blocksAway;
    }

    @Override
    public boolean handler(TriPredicate<Integer, Integer, Integer> action) {
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    if (!action.test(x, y, z)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void setMin(Vector min) {
        this.min = min;
    }

    public void setMax(Vector max) {
        this.max = max;
    }
}
