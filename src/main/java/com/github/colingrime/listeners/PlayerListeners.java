package com.github.colingrime.listeners;

import com.github.colingrime.SkyMines;
import com.github.colingrime.cache.Cooldown;
import com.github.colingrime.cache.PlayerCooldownCache;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.panel.MainPanel;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.manager.SkyMineManager;
import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.skymines.token.SkyMineToken;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlayerListeners implements Listener {

	private final SkyMines plugin;
	private final Map<Player, Cooldown> cooldowns = new HashMap<>();

	public PlayerListeners(SkyMines plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.OFF_HAND || !event.getAction().name().contains("RIGHT_CLICK")) {
			return;
		}

		SkyMineManager manager = plugin.getSkyMineManager();
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();

		if (block != null) {
			for (SkyMine skyMine : manager.getSkyMines(player)) {
				if (skyMine.getStructure().getParameter().contains(block.getLocation().toVector())) {
					new MainPanel(plugin, player, skyMine).openInventory(player);
					return;
				}
			}
		}

		ItemStack item = player.getInventory().getItemInMainHand();
		SkyMineToken token = manager.getToken();

		if (token.isToken(item)) {
			if (manager.getSkyMines(player).size() >= plugin.getSettings().getMaxPerPlayer()) {
				Messages.FAILURE_MAX_AMOUNT.sendTo(player);
				return;
			}

			// check for cooldown
			Cooldown cooldown = cooldowns.get(player);
			if (cooldown != null && !cooldowns.get(player).isCooldownFinished()) {
				Replacer replacer = new Replacer("%time%", Utils.formatTime(cooldown.getCooldownLeft()));
				Messages.FAILURE_ON_PLACEMENT_COOLDOWN.sendTo(player, replacer);
				return;
			}

			MineSize size = token.getMineSize(item).orElseGet(() -> new MineSize(10, 10, 10));
			SkyMineUpgrades upgrades = token.getUpgrades(item).orElseGet(() -> new SkyMineUpgrades(plugin));

			if (manager.createSkyMine(player, player.getLocation().subtract(0, 1, 0), size, upgrades)) {
				Utils.removeOneItemFromHand(player);
				Messages.SUCCESS_PLACE.sendTo(player);
			} else {
				Messages.FAILURE_NO_SPACE.sendTo(player);
			}

			cooldown = new PlayerCooldownCache(player, plugin.getSettings().getPlacementCooldown(), TimeUnit.SECONDS);
			cooldowns.put(player, cooldown);
			plugin.addCooldown(cooldown);
		}
	}

	@EventHandler
	public void onPlayerBlockPlace(BlockPlaceEvent event) {
		// prevents tokens from being placed down
		if (plugin.getSkyMineManager().getToken().isToken(event.getItemInHand())) {
			Messages.FAILURE_INVALID_PLACEMENT.sendTo(event.getPlayer());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (plugin.getSkyMineManager().getToken().isToken(event.getItemDrop().getItemStack())) {
			if (plugin.getSettings().getPreventTokenDrop()) {
				Messages.FAILURE_NO_DROP.sendTo(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			for (SkyMine skyMine : plugin.getSkyMineManager().getSkyMines(event.getPlayer())) {
				skyMine.getStructure().setup();
			}
		});
	}
}
