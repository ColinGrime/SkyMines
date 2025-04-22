package me.colingrimes.skymines.player;

import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;

import javax.annotation.Nonnull;
import java.util.UUID;

public class PlayerSettings {

	private final SkyMines plugin;
	private final UUID uuid;
	private boolean notify;
	private boolean autoReset;

	public PlayerSettings(@Nonnull SkyMines plugin, @Nonnull UUID uuid) {
		this(plugin, uuid, true, false);
	}

	public PlayerSettings(@Nonnull SkyMines plugin, @Nonnull UUID uuid, boolean notify, boolean autoReset) {
		this.plugin = plugin;
		this.uuid = uuid;
		this.notify = notify;
		this.autoReset = autoReset;
	}

	/**
	 * Gets the player's unique ID.
	 *
	 * @return the player's unique ID
	 */
	@Nonnull
	public UUID getUUID() {
		return uuid;
	}

	/**
	 * Gets whether notification messages should be sent to the player.
	 * This includes all messages that are sent from cooldowns ending:
	 * <ul>
	 *     <li>The {@link Messages#GENERAL_COOLDOWN_RESET_AUTOMATIC} message from the reset cooldown finishing -> resetting the mine.</li>
	 *     <li>The {@link Messages#GENERAL_COOLDOWN_RESET_FINISH} message from the reset cooldown fishing without the automatic reset enabled.</li>
	 *     <li>The {@link Messages#GENERAL_COOLDOWN_PICKUP_FINISH} message from the pickup cooldown finishing.</li>
	 * </ul>
	 *
	 * @return true if the player should be notified
	 */
	public boolean shouldNotify() {
		return notify;
	}

	/**
	 * Sets whether notification messages should be sent to the player.
	 * This includes all messages that are sent from cooldowns ending:
	 * <ul>
	 *     <li>The {@link Messages#GENERAL_COOLDOWN_RESET_AUTOMATIC} message from the reset cooldown finishing -> resetting the mine.</li>
	 *     <li>The {@link Messages#GENERAL_COOLDOWN_RESET_FINISH} message from the reset cooldown fishing without the automatic reset enabled.</li>
	 *     <li>The {@link Messages#GENERAL_COOLDOWN_PICKUP_FINISH} message from the pickup cooldown finishing.</li>
	 * </ul>
	 *
	 * @param notify the notification toggle
	 */
	public void setNotify(boolean notify) {
		this.notify = notify;
		this.save();
	}

	/**
	 * Gets whether the player's mine should automatically be reset.
	 *
	 * @return true if the player's mines should automatically reset
	 */
	public boolean shouldAutoReset() {
		return autoReset;
	}

	/**
	 * Sets whether the player's mine should automatically be reset.
	 *
	 * @param autoReset the auto reset toggle
	 */
	public void setAutoReset(boolean autoReset) {
		this.autoReset = autoReset;
		this.save();
	}

	/**
	 * Asynchronously saves the player's current settings.
	 */
	public void save() {
		Scheduler.async().run(() -> plugin.getPlayerStorage().save(this), "PlayerSettings have failed to save. Please report this to the developer:");
	}
}
