package me.colingrimes.skymines.manager;

import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.midnight.cache.Cooldown;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.time.Duration;

public class CooldownManager {

	private final Cooldown<SkyMine> skyMineCooldown = Cooldown.create(Duration.ofSeconds(0));
	private final Cooldown<Player> pickupCooldown = Cooldown.create(Duration.ofSeconds(300));
	private final Cooldown<Player> throttle = Cooldown.create(Duration.ofMillis(100));

	/**
	 * Gets the skymine cooldown from resetting the mine.
	 *
	 * @return the skymine cooldown
	 */
	@Nonnull
	public Cooldown<SkyMine> getSkyMineCooldown() {
		return skyMineCooldown;
	}

	/**
	 * Gets the player cooldown from picking up a skymine.
	 *
	 * @return the pickup cooldown
	 */
	@Nonnull
	public Cooldown<Player> getPickupCooldown() {
		return pickupCooldown;
	}

	/**
	 * Gets the player cooldown from spam right-clicking the skymine token.
	 * This is used to prevent potential duplication issues.
	 *
	 * @return the throttle
	 */
	@Nonnull
	public Cooldown<Player> getThrottle() {
		return throttle;
	}
}
