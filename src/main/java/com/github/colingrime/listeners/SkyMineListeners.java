package com.github.colingrime.listeners;

import com.github.colingrime.SkyMines;
import com.github.colingrime.api.SkyMineCooldownFinishEvent;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SkyMineListeners extends AbstractListener {

	public SkyMineListeners(SkyMines plugin) {
		super(plugin);
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
