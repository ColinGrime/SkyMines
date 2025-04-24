package me.colingrimes.skymines.skymine;

import me.colingrimes.midnight.geometry.Pose;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.api.SkyMineCooldownFinishEvent;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Mines;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.manager.CooldownManager;
import me.colingrimes.skymines.manager.SkyMineManager;
import me.colingrimes.skymines.skymine.option.ResetOptions;
import me.colingrimes.skymines.skymine.structure.SkyMineStructure;
import me.colingrimes.skymines.skymine.upgrade.SkyMineUpgrades;
import me.colingrimes.skymines.util.MineUtils;
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
	private String name;

	/**
	 * Constructs a new {@link SkyMine} with a random UUID.
	 * This is used to create skymines from its token form.
	 *
	 * @param plugin the plugin
	 * @param owner the UUID of the owner
	 * @param identifier the identifier of the mine data
	 * @param structure the structure of the mine
	 * @param upgrades the mine's current upgrades
	 * @param home the home of the mine
	 */
	public DefaultSkyMine(@Nonnull SkyMines plugin, @Nonnull UUID owner, @Nonnull String identifier, @Nonnull SkyMineStructure structure, @Nonnull SkyMineUpgrades upgrades, @Nonnull Pose home) {
		this(plugin, UUID.randomUUID(), owner, identifier, structure, upgrades, home, null);
		this.reset();
	}

	/**
	 * Constructs a {@link SkyMine} that has an existing UUID.
	 * This is used to load skymines from storage.
	 *
	 * @param plugin the plugin
	 * @param uuid the UUID of the mine
	 * @param owner the UUID of the owner
	 * @param identifier the identifier of the mine data
	 * @param structure the structure of the mine
	 * @param upgrades the mine's current upgrades
	 * @param home the home of the mine
	 * @param name the name of the mine
	 */
	public DefaultSkyMine(@Nonnull SkyMines plugin, @Nonnull UUID uuid, @Nonnull UUID owner, @Nonnull String identifier, @Nonnull SkyMineStructure structure, @Nonnull SkyMineUpgrades upgrades, @Nonnull Pose home, @Nullable String name) {
		this.plugin = plugin;
		this.manager = plugin.getSkyMineManager();
		this.cooldowns = plugin.getCooldownManager();
		this.uuid = uuid;
		this.owner = owner;
		this.identifier = identifier;
		this.structure = structure;
		this.upgrades = upgrades;
		this.home = home;
		this.name = name;
		this.plugin.getHologramManager().addHologram(this);
		this.applyCooldown();
	}

	@Override
	public boolean isEnabled() {
		return Mines.MINES.get().containsKey(identifier);
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

	@Nullable
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(@Nonnull String name) {
		this.name = name;
		save();
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
		if (!structure.containsWithin(home.getPosition(), Settings.OPTION_SKYMINE_SETHOME_DISTANCE.get())) {
			return false;
		}

		this.home = home;
		save();
		return true;
	}

	@Override
	public int reset() {
		return reset(ResetOptions.defaults());
	}

	@Override
	public int reset(@Nonnull ResetOptions options) {
		// Checks if the mine is disabled.
		if (!isEnabled()) {
			if (options.shouldNotify()) {
				Messages.FAILURE_SKYMINE_INVALID_IDENTIFIER.replace("{id}", identifier).send(options.getPlayer());
			}
			return 0;
		}

		// If this is an automatic reset, make sure the player has it enabled.
		if (options.isAutomatic() && !plugin.getPlayerManager().getSettings(options.getPlayer()).shouldAutoReset()) {
			return 0;
		}

		// Applies cooldown logic if necessary.
		if (options.applyCooldowns()) {
			Duration timeLeft = cooldowns.getSkyMineCooldown().getTimeLeft(this);
			if (timeLeft.isPositive()) {
				if (options.shouldNotify()) {
					MineUtils.placeholders(Messages.FAILURE_COOLDOWN_RESET, this).replace("{time}", Text.format(timeLeft)).send(options.getPlayer());
				}
				return 0;
			}
			applyCooldown();
		}

		// Sends different success message depending on if the player is the owner of the mine.
		if (options.shouldNotify()) {
			if (options.getPlayer().getUniqueId().equals(owner)) {
				MineUtils.placeholders(Messages.SUCCESS_RESET, this).send(options.getPlayer());
			} else {
				MineUtils.placeholders(Messages.ADMIN_SUCCESS_RESET, this).send(options.getPlayer());
			}
		}

		// Teleports all players to the mine's home.
		if (options.shouldTeleport()) {
			structure.getPlayers().forEach(p -> p.teleport(home.toLocation()));
		}

		int count = structure.build(upgrades.getComposition().getComposition());

		// Sends the automatic reset message if applicable.
		if (options.isAutomatic() && plugin.getPlayerManager().getSettings(options.getPlayer()).shouldNotify() && count > 0) {
			MineUtils.placeholders(Messages.GENERAL_COOLDOWN_RESET_AUTOMATIC, this).send(options.getPlayer());
		}

		return count;
	}

	@Override
	public boolean pickup(@Nonnull Player player) {
		boolean isOwner = owner.equals(player.getUniqueId());
		if (!isOwner && !player.hasPermission("skymines.admin.pickup")) {
			return false;
		}

		ItemStack token = manager.getToken().getToken(identifier, structure.getMineSize(), upgrades);
		if (!player.getInventory().addItem(token).isEmpty()) {
			Messages.FAILURE_TOKEN_NO_INVENTORY_SPACE.send(player);
			return false;
		}

		// Applies pickup cooldown if it's the owner picking it up.
		if (isOwner) {
			int pickupCooldown;
			if (Types.isInteger(Settings.OPTION_COOLDOWN_PICKUP_COOLDOWN.get())) {
				pickupCooldown = Integer.parseInt(Settings.OPTION_COOLDOWN_PICKUP_COOLDOWN.get());
			} else {
				pickupCooldown = (int) cooldowns.getSkyMineCooldown().getTimeLeft(this).getSeconds();
			}

			// Ensure if there's already an existing pickup cooldown, the maximum cooldown is applied.
			if (cooldowns.getPickupCooldown().onCooldown(player)) {
				pickupCooldown = Math.max(pickupCooldown, (int) cooldowns.getPickupCooldown().getTimeLeft(player).getSeconds());
			}

			// Add the pickup cooldown to the player.
			cooldowns.getPickupCooldown().add(player, Duration.ofSeconds(pickupCooldown), p -> {
				if (plugin.getPlayerManager().getSettings(p).shouldNotify()) {
					Messages.GENERAL_COOLDOWN_PICKUP_FINISH.send(player);
				}
			});
		}

		// Sends different success message depending on if the player is the owner of the mine.
		if (isOwner) {
			MineUtils.placeholders(Messages.SUCCESS_PICKUP, this).send(player);
		} else {
			MineUtils.placeholders(Messages.ADMIN_SUCCESS_PICKUP, this).send(player);
		}

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

	/**
	 * Applies the reset cooldown to the mine if it is enabled.
	 */
	private void applyCooldown() {
		if (!isEnabled()) {
			return;
		}

		Consumer<SkyMine> action = (skyMine) -> Common.call(new SkyMineCooldownFinishEvent(skyMine));
		cooldowns.getSkyMineCooldown().add(this, getUpgrades().getResetCooldown().getResetCooldown(), action);
	}
}
