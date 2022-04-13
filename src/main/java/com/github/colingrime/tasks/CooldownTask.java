package com.github.colingrime.tasks;

import com.github.colingrime.cache.Cooldown;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CooldownTask extends BukkitRunnable {

	private final List<Cooldown> cooldownList = new ArrayList<>();

	@Override
	public void run() {
		Iterator<Cooldown> cooldownIterator = cooldownList.iterator();
		while (cooldownIterator.hasNext()) {
			Cooldown cooldown = cooldownIterator.next();

			// check for cooldown completion
			if (cooldown.getCooldownLeft() <= 0) {
				cooldownIterator.remove();

				// only returns true if cooldown was finished normally
				if (cooldown.isCooldownFinished()) {
					cooldown.completionAction();
				}
			}
		}
	}

	public List<Cooldown> getCooldownList() {
		return cooldownList;
	}
}
