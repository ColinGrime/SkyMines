package com.github.colingrime.cache;

import com.github.colingrime.SkyMines;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.utils.Utils;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CooldownCache<T> implements Cooldown {

	private final T value;
	private final Consumer<T> completionAction;
	private final Messages denyMessage;

	private long expiration;

	public CooldownCache(T value, double duration, TimeUnit unit) {
		this(value, duration, unit, t -> {});
	}

	public CooldownCache(T value, double duration, TimeUnit unit, Consumer<T> completionAction) {
		this(value, duration, unit, completionAction, null);
	}

	public CooldownCache(T value, double duration, TimeUnit unit, Consumer<T> completionAction, Messages denyMessage) {
		this.value = value;
		this.expiration = System.currentTimeMillis() + unit.toMillis((long) duration);
		this.completionAction = completionAction;
		this.denyMessage = denyMessage;
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
		Bukkit.getScheduler().runTask(SkyMines.getInstance(), () -> completionAction.accept(value));
	}

	@Override
	public String getDenyMessage() {
		Replacer replacer = new Replacer("%time%", Utils.formatTime(getCooldownLeft()));
		return replacer.replace(denyMessage.toString());
	}

	@Override
	public void invalidate() {
		expiration = -1;
	}
}
