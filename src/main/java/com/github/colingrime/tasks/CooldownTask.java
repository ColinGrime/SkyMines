package com.github.colingrime.tasks;

import com.github.colingrime.cache.Cooldown;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CooldownTask extends BukkitRunnable {

	private final List<Cooldown> cooldowns = new ArrayList<>();

	@Override
	public void run() {
		for (Iterator<Cooldown> i = cooldowns.iterator(); i.hasNext();) {
			Cooldown cooldown = i.next();

			// check for cooldown completion
			if (cooldown.getCooldownLeft() <= 0) {
				i.remove();

				// only returns true if cooldown was finished normally
				if (cooldown.isCooldownFinished()) {
					cooldown.completionAction();
				}
			}
		}
	}

	public List<Cooldown> getCooldowns() {
		return cooldowns;
	}
}
