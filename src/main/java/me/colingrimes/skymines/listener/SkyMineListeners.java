package me.colingrimes.skymines.listener;

import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.api.SkyMineCooldownFinishEvent;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.player.PlayerSettings;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.option.ResetOptions;
import me.colingrimes.skymines.util.MineUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.Optional;

public class SkyMineListeners implements Listener {

	private final SkyMines plugin;

	public SkyMineListeners(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onCooldownFinish(@Nonnull SkyMineCooldownFinishEvent event) {
		SkyMine skyMine = event.getSkyMine();
		Optional<Player> player = Players.get(skyMine.getOwner());
		if (player.isEmpty()) {
			return;
		}

		PlayerSettings settings = plugin.getPlayerManager().getSettings(player.get());

		// Automatic reset.
		if (settings.shouldAutoReset()) {
			skyMine.reset(ResetOptions.automatic(player.get()));
		}

		// Notify on reset finish (will not be sent if automatic reset is enabled).
		else if (settings.shouldNotify()) {
			MineUtils.placeholders(Messages.GENERAL_COOLDOWN_RESET_FINISH, skyMine).send(player.get());
		}
	}
}
