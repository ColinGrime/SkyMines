package me.colingrimes.skymines.listener;

import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.skymines.api.SkyMineCooldownFinishEvent;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.option.ResetOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.Optional;

public class SkyMineListeners implements Listener {

	@EventHandler
	public void onCooldownFinish(@Nonnull SkyMineCooldownFinishEvent event) {
		SkyMine skyMine = event.getSkyMine();
		Optional<Player> player = Players.get(skyMine.getOwner());
		if (player.isEmpty()) {
			return;
		}

		// Automatic reset.
		if (Settings.OPTION_RESET_AUTOMATIC.get() && player.get().hasPermission("skymines.reset.automatic")) {
			if (skyMine.reset(ResetOptions.create().cooldowns(true).build()) > 0 && Settings.OPTION_RESET_AUTOMATIC_NOTIFY.get()) {
				Messages.SUCCESS_RESET_AUTOMATIC.send(player.get());
			}
		}

		// Notify on reset finish (will not be sent if automatic reset is enabled).
		else if (Settings.OPTION_COOLDOWN_NOTIFY_ON_RESET_FINISH.get()) {
			Messages.GENERAL_COOLDOWN_RESET_FINISH.replace("{id}", skyMine.getIndex()).send(player.get());
		}
	}
}
