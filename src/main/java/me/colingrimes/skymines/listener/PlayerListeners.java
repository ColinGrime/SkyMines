package me.colingrimes.skymines.listener;

import me.colingrimes.midnight.event.PlayerInteractBlockEvent;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.bukkit.Inventories;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.api.SkyMineBlockBreakEvent;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.menu.MainMenu;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.manager.SkyMineManager;
import me.colingrimes.skymines.skymine.token.SkyMineToken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.time.Duration;

public class PlayerListeners implements Listener {

	private final SkyMines plugin;

	public PlayerListeners(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.OFF_HAND || !event.getAction().name().contains("RIGHT_CLICK")) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		SkyMineManager manager = plugin.getSkyMineManager();
		SkyMineToken token = manager.getToken();
		if (!token.isToken(item)) {
			return;
		}

		// Check if the token is valid.
		if (!token.isValidToken(item)) {
			Messages.FAILURE_INVALID_MINE.replace("{id}", token.getMineIdentifier(item)).send(player);
			return;
		}

		// Add throttle to ensure lag/placement spam is accounted for.
		if (plugin.getCooldownManager().getThrottle().onCooldown(player)) {
			return;
		} else {
			plugin.getCooldownManager().getThrottle().add(player);
		}

		// Make sure all SkyMines are loaded.
		if (!plugin.getStorage().isLoaded()) {
			Messages.FAILURE_NOT_LOADED.send(player);
			return;
		}

		// Check: max skymines.
		if (manager.getSkyMines(player).size() >= Settings.OPTIONS_MAX_PER_PLAYER.get()) {
			Messages.FAILURE_MAX_AMOUNT.send(player);
			return;
		}

		// Check: cooldown when you pick up a skymine.
		if (plugin.getCooldownManager().getPickupCooldown().onCooldown(player)) {
			Messages.FAILURE_ON_PICKUP_COOLDOWN
					.replace("{time}", Text.format(plugin.getCooldownManager().getPickupCooldown().getTimeLeft(player)))
					.send(player);
			return;
		}

		// Check: cooldown when you place down a skymine.
		//
		// TODO this was originally made to prevent lag from spamming skymine placement,
		//  but it's only per-player and who's really going to have that many large skymines at once?
		//  Build task needs to be updated anyways to evenly spread out build over ticks,
		//  once that's complete + profiled over many large structures, consider deprecating this.
		//
		if (plugin.getCooldownManager().getPlacementCooldown().onCooldown(player)) {
			Messages.FAILURE_ON_PLACEMENT_COOLDOWN
						.replace("{time}", Text.format(plugin.getCooldownManager().getPlacementCooldown().getTimeLeft(player)))
					.send(player);
			return;
		}

		if (manager.createSkyMine(player, item)) {
			plugin.getCooldownManager().getPlacementCooldown().add(player, Duration.ofSeconds(Settings.OPTIONS_PLACEMENT_COOLDOWN.get()));
			Inventories.removeSingle(player.getInventory(), item);
			Messages.SUCCESS_PLACE.send(player);
			event.setCancelled(true);
		} else {
			Messages.FAILURE_NO_SPACE.send(player);
		}
	}

	@EventHandler
	public void onPlayerInteractBlock(@Nonnull PlayerInteractBlockEvent event) {
		SkyMineManager manager = plugin.getSkyMineManager();
		Player player = event.getPlayer();

		// Check specific player's mine.
		for (SkyMine skyMine : manager.getSkyMines(player)) {
			if (!skyMine.getStructure().getParameter().contains(Position.of(event.getLocation()))) {
				continue;
			} else if (event.isRightClick()) {
				new MainMenu(plugin, player, skyMine).open();
				event.setCancelled(true);
			} else if (player.isSneaking() && Settings.OPTIONS_FAST_HOME.get()) {
				player.teleport(skyMine.getHome().toLocation());
				Messages.SUCCESS_HOME.send(player);
				event.setCancelled(true);
			}
			return;
		}

		if (!player.hasPermission("skymines.admin.panel")) {
			return;
		}

		// Admins Only -- access to all mines.
		for (SkyMine skyMine : manager.getSkyMines()) {
			if (!skyMine.getStructure().getParameter().contains(Position.of(event.getLocation()))) {
				continue;
			} else if (event.isRightClick()) {
				new MainMenu(plugin, player, skyMine).open();
				Messages.ADMIN_SUCCESS_PANEL.replace("{player}", Players.getName(skyMine.getOwner())).send(player);
				event.setCancelled(true);
			} else if (player.isSneaking() && Settings.OPTIONS_FAST_HOME.get()) {
				player.teleport(skyMine.getHome().toLocation());
				Messages.SUCCESS_HOME.send(player);
				event.setCancelled(true);
			}
			return;
		}
	}

	@EventHandler
	public void onPlayerBlockPlace(@Nonnull BlockPlaceEvent event) {
		// prevents tokens from being placed down
		if (plugin.getSkyMineManager().getToken().isToken(event.getItemInHand())) {
			Messages.FAILURE_INVALID_PLACEMENT.send(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerBlockBreak(@Nonnull BlockBreakEvent event) {
		for (SkyMine skyMine : plugin.getSkyMineManager().getSkyMines()) {
			if (skyMine.getStructure().getInside().contains(Position.of(event.getBlock().getLocation()))) {
				Common.call(new SkyMineBlockBreakEvent(event, skyMine));
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		if (!Settings.OPTIONS_PREVENT_TOKEN_DROP.get()) {
			return;
		}

		// Checks if the dropped item was a skymine token.
		if (plugin.getSkyMineManager().getToken().isToken(event.getItemDrop().getItemStack())) {
			Messages.FAILURE_NO_DROP.send(event.getPlayer());
			event.setCancelled(true);
		}
	}
}
