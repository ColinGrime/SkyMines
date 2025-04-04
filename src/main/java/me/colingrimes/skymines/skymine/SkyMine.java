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
	 * Gets the UUID of the skymine.
	 *
	 * @return UUID of the skymine
	 */
	@Nonnull
	UUID getUUID();

	/**
	 * Gets the UUID of the player who owns the skymine.
	 *
	 * @return UUID of the owner
	 */
	@Nonnull
	UUID getOwner();

	/**
	 * Gets the current ID of the skymine.
	 * This ID is subject to change (e.g. removing a skymine can change the IDs of all other skymines).
	 * <p>
	 * This is used to target specific skymines for skymine-related commands.
	 * It will always start at ID=1 and increment by 1 for each skymine a player owns.
	 *
	 * @return the ID of the skymine
	 */
	int getId();

	/**
	 * Gets the structure of the skymine.
	 * <p>
	 * This tells you information about the skymine's regions, size, border material, and more.
	 *
	 * @return structure of the skymine
	 */
	@Nonnull
	MineStructure getStructure();

	/**
	 * Gets the skymine's current upgrades. Used for everything upgrade-related.
	 *
	 * @return upgrades of the skymine
	 */
	@Nonnull
	SkyMineUpgrades getUpgrades();

	/**
	 * Gets the current home of the skymine.
	 * The owner of the skymine is allowed to change this if they have access to the '/skymine sethome' command.
	 *
	 * @return home of the skymine
	 */
	@Nonnull
	Location getHome();

	/**
	 * Sets the home of the skymine.
	 *
	 * @param home location to set the home
	 */
	void setHome(@Nonnull Location home);

	/**
	 * Resets the skymine depending on the {@link BlockVarietyUpgrade#getLevel()}.
	 * This will fill up the inside of the skymine with random blocks.
	 * <p>
	 * If the owner is currently on cooldown, and {@code ignoreCooldown} is false, then nothing will happen.
	 *
	 * @param ignoreCooldown whether the cooldown should be ignored
	 * @return true if the skymine was successfully reset
	 */
	boolean reset(boolean ignoreCooldown);

	/**
	 * Picks the skymine up. This only works if it's the owner who is requesting pickup.
	 * It will also return false if the owner's inventory is full.
	 *
	 * @param player any player
	 * @return true if pickup was successful
	 */
	boolean pickup(@Nonnull Player player);

	/**
	 * Removes the skymine, destroys the structure, and gives no skymine token.
	 */
	void remove();

	/**
	 * Asynchronously saves the skymine's current state.
	 */
	void save();
}
