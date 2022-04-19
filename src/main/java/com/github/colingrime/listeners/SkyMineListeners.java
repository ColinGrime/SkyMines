package com.github.colingrime.listeners;

import com.github.colingrime.SkyMines;
import com.github.colingrime.api.SkyMineCooldownFinishEvent;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public record SkyMineListeners(SkyMines plugin) implements Listener {

    public SkyMineListeners(SkyMines plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCooldownFinish(SkyMineCooldownFinishEvent event) {
        if (!plugin.getSettings().shouldNotifyOnResetCooldownFinish()) {
            return;
        }

        Player player = Bukkit.getPlayer(event.getSkyMine().getOwner());
        if (player != null) {
            Messages.RESET_COOLDOWN_FINISH.sendTo(player, new Replacer("%id%", event.getSkyMine().getId()));
        }
    }
}
