package me.colingrimes.skymines.skymine;

import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.api.SkyMineCooldownFinishEvent;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.manager.CooldownManager;
import me.colingrimes.skymines.skymine.manager.SkyMineManager;
import me.colingrimes.skymines.skymine.structure.MineStructure;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import me.colingrimes.midnight.util.io.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
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
	private final MineStructure structure;
	private final SkyMineUpgrades upgrades;
	private Location home;

	// This is used for new skymines -- will generate random UUIDs for them.
	public DefaultSkyMine(@Nonnull SkyMines plugin, @Nonnull UUID owner, @Nonnull MineStructure structure, @Nonnull SkyMineUpgrades upgrades, @Nonnull Location home) {
		this(plugin, UUID.randomUUID(), owner, structure, upgrades, home);
	}

	// This is used when deserializing the skymine data back into skymines.
	public DefaultSkyMine(@Nonnull SkyMines plugin, @Nonnull UUID uuid, @Nonnull UUID owner, @Nonnull MineStructure structure, @Nonnull SkyMineUpgrades upgrades, @Nonnull Location home) {
		this.plugin = plugin;
		this.manager = plugin.getSkyMineManager();
		this.cooldowns = plugin.getCooldownManager();
		this.uuid = uuid;
		this.owner = owner;
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

	@Override
	public int getId() {
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
	public MineStructure getStructure() {
		return structure;
	}

	@Override
	@Nonnull
	public SkyMineUpgrades getUpgrades() {
		return upgrades;
	}

	@Override
	@Nonnull
	public Location getHome() {
		return home;
	}

	@Override
	public void setHome(@Nonnull Location home) {
		this.home = home;
		save();
	}

	@Override
	public boolean reset(boolean ignoreCooldown) {
		if (!ignoreCooldown) {
			if (cooldowns.getSkyMineCooldown().onCooldown(this)) {
				return false;
			}
			Consumer<SkyMine> action = skyMine -> Common.call(new SkyMineCooldownFinishEvent(skyMine));
			cooldowns.getSkyMineCooldown().add(this, getUpgrades().getResetCooldownUpgrade().getResetCooldown(), action);
		}

		structure.buildInside(upgrades.getBlockVarietyUpgrade().getBlockVariety());
		return true;
	}

	@Override
	public boolean pickup(@Nonnull Player player) {
		if (!owner.equals(player.getUniqueId()) && !player.hasPermission("skymines.admin.pickup")) {
			return false;
		}

		ItemStack token = manager.getToken().getToken(structure.getMineSize(), structure.getBorderType(), upgrades);
		if (!player.getInventory().addItem(token).isEmpty()) {
			return false;
		}

		// add cooldown
		int time = Settings.OPTIONS_PICKUP_COOLDOWN.get();
		cooldowns.getPickupCooldown().add(player, Duration.ofSeconds(time), p -> {
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
		Scheduler.async().run(() -> {
			try {
				plugin.getStorage().save(this);
			} catch (Exception e) {
				Logger.severe("SkyMine has failed to save. Please report this to the developer:", e);
			}
		});
	}
}
