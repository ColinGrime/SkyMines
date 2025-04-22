package me.colingrimes.skymines.listener;

import me.colingrimes.midnight.event.PlayerInteractBlockEvent;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.scheduler.Scheduler;
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
import me.colingrimes.skymines.manager.SkyMineManager;
import me.colingrimes.skymines.skymine.token.SkyMineToken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class PlayerListeners implements Listener {

	private final SkyMines plugin;

	public PlayerListeners(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.OFF_HAND || event.getAction() != Action.RIGHT_CLICK_AIR) {
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
			Messages.FAILURE_SKYMINE_INVALID_IDENTIFIER.replace("{id}", token.getMineIdentifier(item)).send(player);
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
			Messages.FAILURE_SKYMINE_NOT_LOADED.send(player);
			return;
		}

		// Check: max skymines.
		if (manager.getSkyMines(player).size() >= Settings.OPTION_SKYMINE_MAX_PER_PLAYER.get()) {
			Messages.FAILURE_SKYMINE_MAX_OWNED.send(player);
			return;
		}

		// Check: cooldown when you pick up a skymine.
		if (plugin.getCooldownManager().getPickupCooldown().onCooldown(player)) {
			Messages.FAILURE_COOLDOWN_PICKUP
					.replace("{time}", Text.format(plugin.getCooldownManager().getPickupCooldown().getTimeLeft(player)))
					.send(player);
			return;
		}

		if (manager.createSkyMine(player, item)) {
			Inventories.removeSingle(player.getInventory(), item);
			Messages.SUCCESS_PLACE.send(player);
			event.setCancelled(true);
		} else {
			Messages.FAILURE_SKYMINE_NO_SPACE.send(player);
		}
	}

	@EventHandler
	public void onPlayerInteractBlock(@Nonnull PlayerInteractBlockEvent event) {
		// Ignore SHIFT-RIGHT-CLICK (they can still place blocks on the mine border).
		// Ignore regular LEFT-CLICK (quick action is SHIFT-LEFT-CLICK only).
		if (event.isShiftRightClick() || (event.isLeftClick() && !event.isShiftLeftClick())) {
			return;
		}

		SkyMineManager manager = plugin.getSkyMineManager();
		Player player = event.getPlayer();

		// Check specific player's mine.
		for (SkyMine skyMine : manager.getSkyMines(player)) {
			if (!skyMine.getStructure().getBorderRegion().contains(Position.of(event.getLocation()))) {
				continue;
			} else if (event.isRightClick()) {
				new MainMenu(plugin, player, skyMine).open();
				event.setCancelled(true);
			} else if (player.isSneaking() && Settings.OPTION_SKYMINE_FAST_HOME.get()) {
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
			if (!skyMine.getStructure().getBorderRegion().contains(Position.of(event.getLocation()))) {
				continue;
			} else if (event.isRightClick()) {
				new MainMenu(plugin, player, skyMine).open();
				Messages.ADMIN_SUCCESS_PANEL.replace("{player}", Players.getName(skyMine.getOwner())).send(player);
				event.setCancelled(true);
			} else if (player.isSneaking() && Settings.OPTION_SKYMINE_FAST_HOME.get()) {
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
			Messages.FAILURE_TOKEN_NO_PLACE.send(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerBlockBreak(@Nonnull BlockBreakEvent event) {
		for (SkyMine skyMine : plugin.getSkyMineManager().getSkyMines()) {
			if (skyMine.getStructure().getInnerRegion().contains(Position.of(event.getBlock().getLocation()))) {
				Common.call(new SkyMineBlockBreakEvent(event, skyMine));
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		if (!Settings.OPTION_TOKEN_PREVENT_DROP.get()) {
			return;
		}

		// Checks if the dropped item was a skymine token.
		if (plugin.getSkyMineManager().getToken().isToken(event.getItemDrop().getItemStack())) {
			Messages.FAILURE_TOKEN_NO_DROP.send(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!plugin.getPlayerManager().contains(player.getUniqueId())) {
			Scheduler.async().run(() -> plugin.getPlayerStorage().loadPlayer(player), "PlayerSettings have failed to load. Please report this to the developer:");
		}
	}
}
