package com.github.colingrime.cache;

import com.github.colingrime.api.SkyMineCooldownFinishEvent;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public class SkyMineCooldownCache extends CooldownCache<SkyMine> {

	public SkyMineCooldownCache(SkyMine skyMine, double duration, TimeUnit unit) {
		super(skyMine, duration, unit);
	}

	@Override
	protected void completionAction(SkyMine skyMine) {
		Bukkit.getPluginManager().callEvent(new SkyMineCooldownFinishEvent(skyMine));
	}
}
