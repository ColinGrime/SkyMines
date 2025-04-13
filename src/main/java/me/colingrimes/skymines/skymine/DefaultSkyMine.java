package me.colingrimes.skymines.skymine;

import me.colingrimes.midnight.geometry.Pose;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.api.SkyMineCooldownFinishEvent;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Mines;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.manager.CooldownManager;
import me.colingrimes.skymines.skymine.manager.SkyMineManager;
import me.colingrimes.skymines.skymine.structure.SkyMineStructure;
import me.colingrimes.skymines.skymine.upgrade.SkyMineUpgrades;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class DefaultSkyMine implements SkyMine {

	private final SkyMines plugin;
	private final SkyMineManager manager;
	private final CooldownManager cooldowns;

	private final UUID uuid;
	private final UUID owner;
	private final String identifier;
	private final SkyMineStructure structure;
	private final SkyMineUpgrades upgrades;
	private Pose home;

	// This is used for new skymines -- will generate random UUIDs for them.
	public DefaultSkyMine(@Nonnull SkyMines plugin, @Nonnull UUID owner, @Nonnull String identifier, @Nonnull SkyMineStructure structure, @Nonnull SkyMineUpgrades upgrades, @Nonnull Pose home) {
		this(plugin, UUID.randomUUID(), owner, identifier, structure, upgrades, home);
	}

	// This is used when deserializing the skymine data back into skymines.
	public DefaultSkyMine(@Nonnull SkyMines plugin, @Nonnull UUID uuid, @Nonnull UUID owner, @Nonnull String identifier, @Nonnull SkyMineStructure structure, @Nonnull SkyMineUpgrades upgrades, @Nonnull Pose home) {
		this.plugin = plugin;
		this.manager = plugin.getSkyMineManager();
		this.cooldowns = plugin.getCooldownManager();
		this.uuid = uuid;
		this.owner = owner;
		this.identifier = identifier;
		this.structure = structure;
		this.upgrades = upgrades;
		this.home = home;
	}

	@Override
	@Nonnull
	public UUID getUUID() {
		return uuid;
	}

	@Override
	@Nonnull
	public UUID getOwner() {
		return owner;
	}

	@Nonnull
	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Nullable
	@Override
	public Mines.Mine getMine() {
		return Mines.MINES.get().get(identifier);
	}

	@Override
	public int getIndex() {
		List<SkyMine> skyMines = manager.getSkyMines(owner);
		for (int i=0; i<skyMines.size(); i++) {
			if (skyMines.get(i).equals(this)) {
				return i + 1;
			}
		}
		return -1;
	}

	@Override
	@Nonnull
	public SkyMineStructure getStructure() {
		return structure;
	}

	@Override
	@Nonnull
	public SkyMineUpgrades getUpgrades() {
		return upgrades;
	}

	@Override
	@Nonnull
	public Pose getHome() {
		return home;
	}

	@Override
	public boolean setHome(@Nonnull Pose home) {
		if (!structure.getInside().containsWithin(home.getPosition(), 5)) {
			return false;
		}

		this.home = home;
		save();
		return true;
	}

	@Override
	public boolean reset(boolean force) {
		if (!force) {
			if (cooldowns.getSkyMineCooldown().onCooldown(this)) {
				return false;
			}
			Consumer<SkyMine> action = skyMine -> Common.call(new SkyMineCooldownFinishEvent(skyMine));
			cooldowns.getSkyMineCooldown().add(this, getUpgrades().getResetCooldown().getResetCooldown(), action);
		}

		structure.buildInside(upgrades.getComposition().getComposition());
		return true;
	}

	@Override
	public boolean pickup(@Nonnull Player player) {
		if (!owner.equals(player.getUniqueId()) && !player.hasPermission("skymines.admin.pickup")) {
			return false;
		}

		ItemStack token = manager.getToken().getToken(identifier, structure.getMineSize(), upgrades);
		if (!player.getInventory().addItem(token).isEmpty()) {
			return false;
		}

		// Calculate pickup cooldown.
		int pickupCooldown;
		if (Types.isInteger(Settings.OPTIONS_PICKUP_COOLDOWN.get())) {
			pickupCooldown = Integer.parseInt(Settings.OPTIONS_PICKUP_COOLDOWN.get());
		} else {
			pickupCooldown = (int) cooldowns.getSkyMineCooldown().getTimeLeft(this).getSeconds();
		}

		// Ensure if there's already an existing pickup cooldown, the maximum cooldown is applied.
		if (cooldowns.getPickupCooldown().onCooldown(player)) {
			pickupCooldown = Math.max(pickupCooldown, (int) cooldowns.getPickupCooldown().getTimeLeft(player).getSeconds());
		}

		// Add the pickup cooldown to the player.
		cooldowns.getPickupCooldown().add(player, Duration.ofSeconds(pickupCooldown), p -> {
			if (Settings.OPTIONS_NOTIFY_ON_PICKUP_COOLDOWN_FINISH.get()) {
				Messages.GENERAL_PICKUP_COOLDOWN_FINISH.send(player);
			}
		});

		remove();
		return true;
	}

	@Override
	public void remove() {
		structure.destroy();
		manager.removeSkyMine(owner, this);
	}

	@Override
	public void save() {
		Scheduler.async().run(() -> plugin.getStorage().save(this), "SkyMine has failed to save. Please report this to the developer:");
	}
}
