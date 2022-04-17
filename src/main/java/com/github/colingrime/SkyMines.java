package com.github.colingrime;

import com.github.colingrime.commands.skymines.SkyMinesBaseCommand;
import com.github.colingrime.commands.skyminesadmin.SkyMinesAdminBaseCommand;
import com.github.colingrime.config.Settings;
import com.github.colingrime.dependencies.DependencyFailureException;
import com.github.colingrime.dependencies.DependencyManager;
import com.github.colingrime.listeners.PanelListeners;
import com.github.colingrime.listeners.PlayerListeners;
import com.github.colingrime.listeners.SkyMineListeners;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.metrics.Metrics;
import com.github.colingrime.panel.setup.PanelSettings;
import com.github.colingrime.skymines.factory.DefaultSkyMineFactory;
import com.github.colingrime.skymines.manager.CooldownManager;
import com.github.colingrime.skymines.manager.SkyMineManager;
import com.github.colingrime.skymines.token.DefaultSkyMineToken;
import com.github.colingrime.storage.Storage;
import com.github.colingrime.storage.StorageFactory;
import com.github.colingrime.updater.SpigotUpdater;
import com.github.colingrime.utils.Logger;
import com.github.colingrime.utils.Timer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyMines extends JavaPlugin {

	private static SkyMines instance;

	private DependencyManager dependencyManager;
	private SkyMineManager skyMineManager;
	private CooldownManager cooldownManager;
	private Settings settings;
	private PanelSettings panelSettings;
	private Storage storage;

	@Override
	public void onEnable() {
		try {
			dependencyManager = new DependencyManager(this);
			dependencyManager.registerDependencies();
		} catch (DependencyFailureException ex) {
			Logger.severe(ex.getMessage());
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		instance = this;
		skyMineManager = new SkyMineManager(this, new DefaultSkyMineFactory(this), new DefaultSkyMineToken(this));
		cooldownManager = new CooldownManager(this);

		loadData();
		Bukkit.getScheduler().runTaskAsynchronously(this, this::loadStorage);

		registerCommands();
		registerListeners();

		// check for metrics
		if (settings.isMetricsEnabled()) {
			new Metrics(this, 14946);
		}

		// check for updates
		SpigotUpdater updater = new SpigotUpdater(this);
		Bukkit.getScheduler().runTaskAsynchronously(this, updater::checkForUpdate);
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		if (storage != null) {
			storage.shutdown();
		}
	}

	/**
	 * Initializes the yaml data.
	 */
	private void loadData() {
		settings = new Settings(this);
		panelSettings = new PanelSettings(this);
		Messages.init(this);
		reload();
	}

	/**
	 * Reloads the yaml data.
	 */
	public void reload() {
		settings.reload();
		panelSettings.reload();
		Messages.reload();
	}

	/**
	 * Initializes the storage system.
	 */
	private void loadStorage() {
		try {
			storage = new StorageFactory(this).createStorage();
			storage.init();
			Timer.time(() -> storage.loadMines(), "Mines have been loaded in %s ms");
		} catch (Exception ex) {
			Logger.severe("Storage has failed to load. Plugin has been disabled.");
			ex.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	private void registerCommands() {
		new SkyMinesBaseCommand(this);
		new SkyMinesAdminBaseCommand(this);
	}

	private void registerListeners() {
		new PlayerListeners(this);
		new PanelListeners(this);
		new SkyMineListeners(this);
	}

	public static SkyMines getInstance() {
		return instance;
	}

	public DependencyManager getDependencyManager() {
		return dependencyManager;
	}

	public SkyMineManager getSkyMineManager() {
		return skyMineManager;
	}

	public CooldownManager getCooldownManager() {
		return cooldownManager;
	}

	public Settings getSettings() {
		return settings;
	}

	public PanelSettings getPanelSettings() {
		return panelSettings;
	}

	public Storage getStorage() {
		return storage;
	}
}
