package com.github.colingrime.skymines;

import com.github.colingrime.cache.Cooldown;
import com.github.colingrime.skymines.structure.MineStructure;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.skymines.upgrades.types.BlockVarietyUpgrade;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface SkyMine {

    /**
     * @return UUID of mine
     */
    UUID getUUID();

    /**
     * @return UUID of the owner
     */
    UUID getOwner();

    /**
     * @return gets the ID of the skymine
     */
    int getId();

    /**
     * @return structure of the skymine
     */
    MineStructure getStructure();

    /**
     * @return home of the skymine
     */
    Location getHome();

    /**
     * Sets the home of the skymine.
     *
     * @param home any location
     */
    void setHome(Location home);

    /**
     * @return upgrades of the sky mine
     */
    SkyMineUpgrades getUpgrades();

    /**
     * Resets the skymine depending on the {@link BlockVarietyUpgrade#getLevel()}.
     *
     * @param ignoreCooldown whether the cooldown should be ignored
     * @return true if there is no cooldown and mine was successfully reset
     */
    boolean reset(boolean ignoreCooldown);

    /**
     * @return cooldown cache for skymines
     */
    Cooldown getCooldown();

    /**
     * Picks the skymine up. Only works if it's the owner who is requesting pickup.
     * It will also return false if the owner's inventory is full.
     *
     * @param player any player
     * @return true if pickup was successful
     */
    boolean pickup(Player player);

    /**
     * Removes the skymine and gives no skymine token.
     */
    void remove();

    /**
     * Asynchronously saves the skymine's current state.
     */
    void save();
}
