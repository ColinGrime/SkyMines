package com.github.colingrime.skymines;

import com.github.colingrime.SkyMines;
import com.github.colingrime.api.SkyMineCooldownFinishEvent;
import com.github.colingrime.cache.Cooldown;
import com.github.colingrime.cache.CooldownCache;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.skymines.structure.MineStructure;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DefaultSkyMine implements SkyMine {

	private final SkyMines plugin;
	private final UUID uuid;
	private final UUID owner;
	private final MineStructure structure;
	private final SkyMineUpgrades upgrades;
	private Location home;
	private Cooldown cooldown = new CooldownCache<>(this, 0, TimeUnit.SECONDS);

	public DefaultSkyMine(SkyMines plugin, UUID owner, MineStructure structure, Location home, SkyMineUpgrades upgrades) {
		this(plugin, UUID.randomUUID(), owner, structure, home, upgrades);
	}

	public DefaultSkyMine(SkyMines plugin, UUID uuid, UUID owner, MineStructure structure, Location home, SkyMineUpgrades upgrades) {
		this.plugin = plugin;
		this.uuid = uuid;
		this.owner = owner;
		this.structure = structure;
		this.home = home;
		this.upgrades = upgrades;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public int getId() {
		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(owner);
		for (int i=0; i<skyMines.size(); i++) {
			if (skyMines.get(i).equals(this)) {
				return i + 1;
			}
		}

		return -1;
	}

	@Override
	public MineStructure getStructure() {
		return structure;
	}

	@Override
	public Location getHome() {
		return home;
	}

	@Override
	public void setHome(Location home) {
		this.home = home;
		save();
	}

	@Override
	public SkyMineUpgrades getUpgrades() {
		return upgrades;
	}

	@Override
	public boolean reset(boolean ignoreCooldown) {
		if (!ignoreCooldown) {
			if (!cooldown.isCooldownFinished()) {
				return false;
			}

			resetCooldown();
		}

		structure.buildInside(upgrades.getBlockVarietyUpgrade().getBlockVariety());
		return true;
	}

	private void resetCooldown() {
		Consumer<SkyMine> action = skyMine -> Bukkit.getPluginManager().callEvent(new SkyMineCooldownFinishEvent(skyMine));
		cooldown = new CooldownCache<>(this, getUpgrades().getResetCooldownUpgrade().getResetCooldown(), TimeUnit.SECONDS, action);
		plugin.getCooldownManager().addCooldown(cooldown);
	}

	@Override
	public Cooldown getCooldown() {
		return cooldown;
	}

	@Override
	public boolean pickup(Player player) {
		if (!owner.equals(player.getUniqueId()) && !player.hasPermission("skymines.admin.pickup")) {
			return false;
		}

		ItemStack token = plugin.getSkyMineManager().getToken().getToken(structure.getSize(), upgrades);
		if (!player.getInventory().addItem(token).isEmpty()) {
			return false;
		}

		// add cooldown
		plugin.getCooldownManager().addPlayerCooldown(player, plugin.getSettings().getPickupCooldown(), p -> {
			if (plugin.getSettings().shouldNotifyOnPickupCooldownFinish()) {
				Messages.PICKUP_COOLDOWN_FINISH.sendTo(player);
			}
		}, Messages.FAILURE_ON_PICKUP_COOLDOWN);

		remove();
		return true;
	}

	@Override
	public void remove() {
		structure.destroy();
		plugin.getSkyMineManager().removeSkyMine(owner, this);
	}


	@Override
	public void save() {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			try {
				plugin.getStorage().saveMine(this);
			} catch (Exception e) {
				Logger.severe("SkyMine has failed to save. Please report this to the developer.");
				e.printStackTrace();
			}
		});
	}
}
