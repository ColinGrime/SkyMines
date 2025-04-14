package me.colingrimes.skymines.listener;

import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.skymines.api.SkyMineCooldownFinishEvent;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

public class SkyMineListeners implements Listener {

	@EventHandler
	public void onCooldownFinish(@Nonnull SkyMineCooldownFinishEvent event) {
		if (Settings.OPTIONS_NOTIFY_ON_RESET_COOLDOWN_FINISH.get()) {
			Players.get(event.getSkyMine().getOwner()).ifPresent(player -> {
				Messages.GENERAL_COOLDOWN_RESET_FINISH.replace("{id}", event.getSkyMine().getIndex()).send(player);
			});
		}
	}
}
