package me.colingrimes.skymines;

import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.storage.sql.connection.ConnectionFactory;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.midnight.update.UpdateCheckerSpigot;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.listener.ParameterListeners;
import me.colingrimes.skymines.listener.PlayerListeners;
import me.colingrimes.skymines.listener.SkyMineListeners;
import me.colingrimes.skymines.skymine.factory.DefaultSkyMineFactory;
import me.colingrimes.skymines.skymine.manager.CooldownManager;
import me.colingrimes.skymines.skymine.manager.SkyMineManager;
import me.colingrimes.skymines.skymine.structure.behavior.BuildBehavior;
import me.colingrimes.skymines.skymine.structure.behavior.DefaultBuildBehavior;
import me.colingrimes.skymines.skymine.token.DefaultSkyMineToken;
import me.colingrimes.skymines.storage.SkyMineStorage;
import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.libs.bstats.bukkit.Metrics;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.io.Logger;
import me.colingrimes.midnight.util.misc.Timer;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.List;

public class SkyMines extends Midnight {

	private static SkyMines instance;
	private SkyMineManager skyMineManager;
	private CooldownManager cooldownManager;
	private BuildBehavior buildBehavior;
	private SkyMineStorage storage;

	@Override
	public void enable() {
		if (!Common.hasEconomy()) {
			Logger.severe(this, "No Vault dependency found. Disabling plugin...");
			Common.disable(this);
			return;
		}

		instance = this;
		skyMineManager = new SkyMineManager(this, new DefaultSkyMineFactory(this), new DefaultSkyMineToken());
		cooldownManager = new CooldownManager();
		buildBehavior =  new DefaultBuildBehavior();

		// Load the database storage.
		Scheduler.async().run(this::loadStorage);

		// Check for Metrics.
		if (Settings.MISC_ENABLE_METRICS.get()) {
			new Metrics(this, 14946);
		}

		// Check for updates.
		new UpdateCheckerSpigot(this, 101373);
	}

	@Override
	public void disable() {
		Bukkit.getScheduler().cancelTasks(this);
		if (storage != null) {
			storage.shutdown();
		}
	}

	@Override
	protected void registerListeners(@Nonnull List<Listener> listeners) {
		listeners.add(new PlayerListeners(this));
		listeners.add(new SkyMineListeners());
		listeners.add(new ParameterListeners(this));
	}

	/**
	 * Initializes the storage system.
	 */
	private void loadStorage() {
		try {
			ConnectionProvider connectionProvider = new ConnectionFactory(this).createConnection(Settings.DATABASE_CREDENTIALS.get());
			storage = new SkyMineStorage(this, connectionProvider);
			storage.init();
			if (skyMineManager.getSkyMines().isEmpty()) {
				Timer.time(this, "Mines have been loaded", () -> storage.loadMines());
			}
		} catch (Exception e) {
			Logger.severe(this, "Storage has failed to initialize. Please report this to the developer:", e);
			Common.disable(this);
		}
	}

	@Nonnull
	public static SkyMines getInstance() {
		return instance;
	}

	@Nonnull
	public SkyMineManager getSkyMineManager() {
		return skyMineManager;
	}

	@Nonnull
	public CooldownManager getCooldownManager() {
		return cooldownManager;
	}

	@Nonnull
	public BuildBehavior getBuildbehavior() {
		return buildBehavior;
	}

	@Nonnull
	public SkyMineStorage getStorage() {
		return storage;
	}
}
