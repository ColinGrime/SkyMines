package me.colingrimes.skymines.skymine.option;

import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Options that control how a {@link SkyMine} reset is performed.
 */
public class ResetOptions {

	private final Player player;
	private final boolean notify;
	private final boolean cooldowns;
	private final boolean teleport;
	private final boolean automatic;

	/**
	 * Constructs the default {@link ResetOptions}.
	 *
	 * @return the default result options
	 */
	@Nonnull
	public static ResetOptions defaults() {
		return create().build();
	}

	/**
	 * Constructs the standard {@link ResetOptions} for a regular player.
	 *
	 * @param player the player who initiated the reset
	 * @return the standard reset options
	 */
	@Nonnull
	public static ResetOptions standard(@Nonnull Player player) {
		return create()
				.player(player)
				.notify(true)
				.cooldowns(true)
				.build();
	}

	/**
	 * Constructs the automatic {@link ResetOptions} for a regular player.
	 *
	 * @param player the player who initiated the reset
	 * @return the automatic reset options
	 */
	@Nonnull
	public static ResetOptions automatic(@Nonnull Player player) {
		return create()
				.player(player)
				.cooldowns(true)
				.automatic(true)
				.build();
	}

	/**
	 * Constructs a new {@link ResetOptions.Builder}.
	 *
	 * @return the reset options builder
	 */
	@Nonnull
	public static ResetOptions.Builder create() {
		return new Builder();
	}

	private ResetOptions(Builder builder) {
		this.player = builder.player;
		this.notify = builder.notify;
		this.cooldowns = builder.cooldowns;
		this.teleport = builder.teleport;
		this.automatic = builder.automatic;
	}

	/**
	 * Gets the player who initiated the reset.
	 *
	 * @return the player who reset the mine
	 * @throws NullPointerException if the player is null
	 */
	@Nonnull
	public Player getPlayer() {
		return Objects.requireNonNull(player, "Player is null.");
	}

	/**
	 * Gets the player who initiated the reset.
	 *
	 * @return the player who reset the mine
	 */
	@Nullable
	public Player getPlayerNullable() {
		return player;
	}

	/**
	 * Gets whether status messages should be sent to the player.
	 * <p>
	 * Requires the {@link ResetOptions#getPlayer()} to be non-null and {@link ResetOptions#isAutomatic()} to be false.
	 *
	 * @return true if the player should be notified
	 */
	public boolean shouldNotify() {
		return notify && player != null && !isAutomatic();
	}

	/**
	 * Gets whether cooldowns should be applied.
	 * <p>
	 * This also prevents the mine from resetting if it is currently on cooldown.
	 * 
	 * @return true if cooldowns should be applied
	 */
	public boolean applyCooldowns() {
		return cooldowns;
	}

	/**
	 * Gets whether all players who are inside the mine should be teleported to its home on reset.
	 *
	 * @return true if mine teleportation is enabled
	 */
	public boolean shouldTeleport() {
		return teleport;
	}

	/**
	 * Gets whether this is an automatic reset.
	 * If this is enabled, {@link ResetOptions#shouldNotify()} will always return false.
	 * <p>
	 * Requires the {@link ResetOptions#getPlayer()} to be non-null.
	 *
	 * @return true if it is an automatic reset
	 */
	public boolean isAutomatic() {
		return automatic && player != null;
	}

	public static class Builder {
		private Player player = null;
		private boolean notify = false;
		private boolean cooldowns = false;
		private boolean teleport = Settings.OPTION_RESET_TELEPORT_HOME.get();
		private boolean automatic = false;

		@Nonnull
		public Builder player(@Nonnull Player player) {
			this.player = player;
			return this;
		}

		@Nonnull
		public Builder notify(boolean notify) {
			this.notify = notify;
			return this;
		}

		@Nonnull
		public Builder cooldowns(boolean force) {
			this.cooldowns = force;
			return this;
		}

		@Nonnull
		public Builder teleport(boolean teleport) {
			this.teleport = teleport;
			return this;
		}

		@Nonnull
		public Builder automatic(boolean automatic) {
			this.automatic = automatic;
			return this;
		}

		@Nonnull
		public ResetOptions build() {
			return new ResetOptions(this);
		}
	}
}
