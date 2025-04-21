package me.colingrimes.skymines.skymine;

import me.colingrimes.midnight.geometry.Pose;
import me.colingrimes.skymines.config.Mines;
import me.colingrimes.skymines.skymine.structure.SkyMineStructure;
import me.colingrimes.skymines.skymine.upgrade.SkyMineUpgrades;
import me.colingrimes.skymines.skymine.upgrade.type.CompositionUpgrade;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface SkyMine {

	/**
	 * Gets whether the skymine is enabled.
	 * If the skymine doesn't have valid {@link Mines.Mine} configuration data, it will return {@code false}.
	 *
	 * @return true if the skymine is enabled
	 */
	boolean isEnabled();

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
	 * Gets the identifier of the skymine.
	 * This is used to get the {@link Mines.Mine} configuration data of the mine.
	 *
	 * @return identifier of the skymine
	 */
	@Nonnull
	String getIdentifier();

	/**
	 * Gets the current index of the skymine.
	 * This index is subject to change (e.g. removing a skymine can change the IDs of all other skymines).
	 * <p>
	 * This is used to target specific skymines for skymine-related commands.
	 * It will always start at ID=1 and increment by 1 for each skymine a player owns.
	 *
	 * @return the index of the skymine
	 */
	int getIndex();

	/**
	 * Gets the current name of the skymine.
	 * The owner of the skymine is allowed to change this if they have access to the '/skymine name' command.
	 *
	 * @return name of the skymine
	 */
	@Nullable
	String getName();

	/**
	 * Sets the name of the skymine.
	 * This can be referenced in place of the index.
	 *
	 * @param name skymine name
	 */
	void setName(@Nonnull String name);

	/**
	 * Gets the structure of the skymine.
	 * <p>
	 * This tells you information about the skymine's regions, size, border material, and more.
	 *
	 * @return structure of the skymine
	 */
	@Nonnull
	SkyMineStructure getStructure();

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
	Pose getHome();

	/**
	 * Sets the home of the skymine.
	 * The home must be within 5 blocks of the skymine.
	 *
	 * @param home pose to set the home
	 * @return true if the home was successfully set
	 */
	boolean setHome(@Nonnull Pose home);

	/**
	 * Resets the skymine depending on the {@link CompositionUpgrade#getLevel()}.
	 * This will fill up the inside of the skymine with random blocks.
	 * <p>
	 * If the owner is currently on cooldown, and {@code force} is false, then nothing will happen.
	 *
	 * @param force whether the cooldown should be ignored
	 * @return true if the skymine was successfully reset
	 */
	boolean reset(boolean force);

	/**
	 * Picks the skymine up. This only works if it's the owner who is requesting pickup.
	 * However, this requirement can be bypassed if the player has the "skymines.admin.pickup" permission.
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
