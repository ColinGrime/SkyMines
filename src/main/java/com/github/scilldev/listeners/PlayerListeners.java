package com.github.scilldev.listeners;

import com.github.scilldev.SkyMines;
import com.github.scilldev.locale.Messages;
import com.github.scilldev.skymines.structure.MineSize;
import com.github.scilldev.skymines.token.SkyMineToken;
import com.github.scilldev.skymines.upgrades.StandardUpgrades;
import com.github.scilldev.skymines.upgrades.Upgrades;
import com.github.scilldev.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
		if (event.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		SkyMineToken token = plugin.getSkyMineManager().getToken();

		if (token.isToken(item)) {
			MineSize size = token.getMineSize(item).orElseGet(() -> new MineSize(10, 10, 10));
			Upgrades upgrades = token.getUpgrades(item).orElseGet(StandardUpgrades::new);

			if (plugin.getSkyMineManager().createSkyMine(player, player.getLocation(), size, upgrades)) {
				Utils.removeOneItemFromHand(player);
			} else {
				Messages.FAILURE_NO_SPACE.sendTo(player);
			}
		}
	}
}
