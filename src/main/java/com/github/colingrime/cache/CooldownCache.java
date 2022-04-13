package com.github.colingrime.cache;

import com.github.colingrime.SkyMines;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;

public abstract class CooldownCache<T> implements Cooldown {

	private final T value;
	private long expiration;

	public CooldownCache(T value, double duration, TimeUnit unit) {
		this.value = value;
		this.expiration = System.currentTimeMillis() + unit.toMillis((long) duration);
	}

	@Override
	public long getCooldownLeft() {
		// this will only occur if the cooldown was invalidated
		if (expiration == -1) {
			return -1;
		}

		return Math.max(0, TimeUnit.MILLISECONDS.toSeconds(expiration - System.currentTimeMillis()));
	}

	@Override
	public void completionAction() {
		Bukkit.getScheduler().runTask(SkyMines.getInstance(), () -> completionAction(value));
	}

	protected abstract void completionAction(T value);

	@Override
	public void invalidate() {
		expiration = -1;
	}
}
