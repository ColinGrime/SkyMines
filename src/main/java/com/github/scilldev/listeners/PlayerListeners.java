package com.github.scilldev.listeners;

import com.github.scilldev.SkyMines;
import com.github.scilldev.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListeners implements Listener {

	private final SkyMines plugin;

	public PlayerListeners(SkyMines plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getClickedBlock() == null) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();

		if (plugin.getSkyMineManager().getToken().isToken(item)) {
			plugin.getSkyMineManager().createSkyMine(player, event.getClickedBlock().getLocation());
			Utils.removeOneItemFromHand(player);
		}
	}
}
