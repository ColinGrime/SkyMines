package me.colingrimes.skymines.skymine;

import me.colingrimes.skymines.skymine.structure.MineStructure;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import me.colingrimes.skymines.skymine.upgrades.types.BlockVarietyUpgrade;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface SkyMine {

	/**
	 * @return UUID of mine
	 */
	@Nonnull
	UUID getUUID();

	/**
	 * @return UUID of the owner
	 */
	@Nonnull
	UUID getOwner();

	/**
	 * @return gets the ID of the skymine
	 */
	int getId();

	/**
	 * @return structure of the skymine
	 */
	@Nonnull
	MineStructure getStructure();

	/**
	 * @return upgrades of the sky mine
	 */
	@Nonnull
	SkyMineUpgrades getUpgrades();

	/**
	 * @return home of the skymine
	 */
	@Nonnull
	Location getHome();

	/**
	 * Sets the home of the skymine.
	 * @param home any location
	 */
	void setHome(@Nonnull Location home);

	/**
	 * Resets the skymine depending on the {@link BlockVarietyUpgrade#getLevel()}.
	 * @param ignoreCooldown whether the cooldown should be ignored
	 * @return true if there is no cooldown and mine was successfully reset
	 */
	boolean reset(boolean ignoreCooldown);

	/**
	 * Picks the skymine up. Only works if it's the owner who is requesting pickup.
	 * It will also return false if the owner's inventory is full.
	 *
	 * @param player any player
	 * @return true if pickup was successful
	 */
	boolean pickup(@Nonnull Player player);

	/**
	 * Removes the skymine and gives no skymine token.
	 */
	void remove();

	/**
	 * Asynchronously saves the skymine's current state.
	 */
	void save();
}
