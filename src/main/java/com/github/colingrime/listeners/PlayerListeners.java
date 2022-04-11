package com.github.colingrime.listeners;

import com.github.colingrime.SkyMines;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.panel.UpgradePanel;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.skymines.token.SkyMineToken;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.utils.Utils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerListeners implements Listener {

	private final SkyMines plugin;

	public PlayerListeners(SkyMines plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.OFF_HAND || !event.getAction().name().contains("RIGHT_CLICK")) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		SkyMineToken token = plugin.getSkyMineManager().getToken();

		if (token.isToken(item)) {
			MineSize size = token.getMineSize(item).orElseGet(() -> new MineSize(10, 10, 10));
			SkyMineUpgrades upgrades = token.getUpgrades(item).orElseGet(() -> new SkyMineUpgrades(plugin));

			if (plugin.getSkyMineManager().createSkyMine(player, player.getLocation().subtract(0, 1, 0), size, upgrades)) {
				Utils.removeOneItemFromHand(player);
			} else {
				Messages.FAILURE_NO_SPACE.sendTo(player);
			}

			return;
		}

		Block block = event.getClickedBlock();
		if (block != null) {
			for (SkyMine skyMine : plugin.getSkyMineManager().getSkyMines(player)) {
				if (skyMine.getStructure().getParameter().contains(block.getLocation().toVector())) {
					new UpgradePanel(plugin, player, skyMine).openInventory(player);
					return;
				}
			}
		}
	}
}
