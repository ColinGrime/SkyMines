package me.colingrimes.skymines.skymine.manager;

import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.midnight.cache.Cooldown;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.time.Duration;

public class CooldownManager {

	private final Cooldown<SkyMine> skyMineCooldown = Cooldown.create(Duration.ofSeconds(0));
	private final Cooldown<Player> pickupCooldown = Cooldown.create(Duration.ofSeconds(300));
	private final Cooldown<Player> placementCooldown = Cooldown.create(Duration.ofSeconds(10));
	private final Cooldown<Player> throttle = Cooldown.create(Duration.ofMillis(100));

	@Nonnull
	public Cooldown<SkyMine> getSkyMineCooldown() {
		return skyMineCooldown;
	}

	@Nonnull
	public Cooldown<Player> getPickupCooldown() {
		return pickupCooldown;
	}

	@Nonnull
	public Cooldown<Player> getPlacementCooldown() {
		return placementCooldown;
	}

	@Nonnull
	public Cooldown<Player> getThrottle() {
		return throttle;
	}
}
