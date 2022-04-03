package com.github.scilldev.listeners;

import com.github.scilldev.SkyMines;
import com.github.scilldev.panel.Panel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class PanelListeners implements Listener {

    public PanelListeners(SkyMines plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!Panel.getPanelViewers().containsKey(player)) {
            return;
        }

        // get panel and any click actions
        Panel panel = Panel.getPanelViewers().get(player);
        Panel.MenuAction action = panel.getActions().get(event.getRawSlot());
        if (action != null) {
            action.click(player, event.getClick());
        }

        // cancel click so player doesn't interfere with menu
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Panel.getPanelViewers().remove((Player) event.getPlayer());
    }
}