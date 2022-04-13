package com.github.colingrime.skymines.timer;

import com.github.colingrime.SkyMines;
import com.github.colingrime.api.SkyMineCooldownFinishEvent;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SkyMineCooldownTimer extends BukkitRunnable {

	private final List<SkyMine> minesOnCooldown = new ArrayList<>();

	@Override
	public void run() {
		Iterator<SkyMine> skyMines = minesOnCooldown.iterator();
		while (skyMines.hasNext()) {
			SkyMine skyMine = skyMines.next();

			// check if cooldown is complete
			if (skyMine.getCooldownTime() <= 0) {
				Bukkit.getScheduler().runTask(SkyMines.getInstance(), () -> callCooldownFinish(skyMine));
				skyMines.remove();
			}
		}
	}

	public void callCooldownFinish(SkyMine skyMine) {
		Bukkit.getPluginManager().callEvent(new SkyMineCooldownFinishEvent(skyMine));
	}

	public List<SkyMine> getMinesOnCooldown() {
		return minesOnCooldown;
	}
}
