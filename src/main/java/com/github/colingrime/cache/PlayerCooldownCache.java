package com.github.colingrime.cache;

import com.github.colingrime.SkyMines;
import com.github.colingrime.locale.Messages;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PlayerCooldownCache extends CooldownCache<Player> {

	public PlayerCooldownCache(Player player, double duration, TimeUnit unit) {
		super(player, duration, unit);
	}

	@Override
	protected void completionAction(Player player) {
		if (SkyMines.getInstance().getSettings().shouldNotifyOnPlacementCooldownFinish()) {
			Messages.PLACEMENT_COOLDOWN_FINISH.sendTo(player);
		}
	}
}
