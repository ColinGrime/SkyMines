package com.github.colingrime.skymines.token;

import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface SkyMineToken {

    /**
     * @return default skymine token
     */
    ItemStack getToken();

    /**
     * @param size       size of the mine
     * @param borderType border type of the mine
     * @return skymine token with custom size and upgrades
     */
    ItemStack getToken(MineSize size, Material borderType);

    /**
     * @param size       size of the mine
     * @param upgrades   upgrades of the mine
     * @param borderType border type of the mine
     * @return skymine token with custom size, upgrades, and border type
     */
    ItemStack getToken(MineSize size, SkyMineUpgrades upgrades, Material borderType);

    /**
     * @param item any item
     * @return true if the item is a sky mine token
     */
    boolean isToken(ItemStack item);

    /**
     * @param item any item
     * @return size of the mine if available
     */
    Optional<MineSize> getMineSize(ItemStack item);

    /**
     * @param item any item
     * @return upgrades of the mine if available
     */
    Optional<SkyMineUpgrades> getUpgrades(ItemStack item);

    /**
     * @param item any item
     * @return border type of the mine if available
     */
    Optional<Material> getBorderType(ItemStack item);
}