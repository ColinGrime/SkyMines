package com.github.colingrime;

import com.github.colingrime.commands.skymines.SkyMinesBaseCommand;
import com.github.colingrime.commands.skymines.subcommands.*;
import com.github.colingrime.config.Settings;
import com.github.colingrime.listeners.PanelListeners;
import com.github.colingrime.listeners.PlayerListeners;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.panel.setup.PanelSettings;
import com.github.colingrime.skymines.factory.DefaultSkyMineFactory;
import com.github.colingrime.skymines.manager.SkyMineManager;
import com.github.colingrime.skymines.structure.behavior.BuildBehavior;
import com.github.colingrime.skymines.structure.behavior.DefaultBuildBehavior;
import com.github.colingrime.skymines.structure.behavior.FastBuildBehavior;
import com.github.colingrime.skymines.token.DefaultSkyMineToken;
import com.github.colingrime.storage.Storage;
import com.github.colingrime.storage.StorageFactory;
import com.github.colingrime.utils.Logger;
import com.github.colingrime.utils.Timer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyMines extends JavaPlugin {

	private static SkyMines instance;

	private SkyMineManager skyMineManager;
	private Settings settings;
	private PanelSettings panelSettings;
	private Storage storage;
	private Economy econ = null;

	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			Logger.severe("No Vault dependency found. Plugin has been disabled.");
			getServer().getPluginManager().disablePlugin(this);
		}

		BuildBehavior buildBehavior;
		if (getServer().getPluginManager().getPlugin("FastAsyncWorldEdit") == null) {
			buildBehavior = new DefaultBuildBehavior();
		} else {
			buildBehavior = new FastBuildBehavior(this);
		}

		instance = this;
		// TODO clean this up and account for different instances
		skyMineManager = new SkyMineManager(this, new DefaultSkyMineFactory(this), new DefaultSkyMineToken(this), buildBehavior);

		loadData();
		loadStorage();

		registerCommands();
		registerListeners();
	}

	@Override
	public void onDisable() {
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
		storage = new StorageFactory(this).createStorage();

		try {
			storage.init();
		} catch (Exception ex) {
			Logger.severe("The storage has failed to initialize.");
		}

		Timer.time(() -> {
			try {
				storage.loadMines();
			} catch (Exception e) {
				Logger.severe("Mines have failed to load.");
			}
		}, "Mines have been loaded!");
	}

	private void registerCommands() {
		SkyMinesBaseCommand skyMines = new SkyMinesBaseCommand(this);
		skyMines.registerSubCommand(new SkyMinesListSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesHomeSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesPanelSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesResetSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesGiveSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesPickupSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesReloadSubCommand(this));
	}

	private void registerListeners() {
		new PlayerListeners(this);
		new PanelListeners(this);
	}

	/**
	 * @return true if economy was successfully setup
	 */
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}

		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}

		econ = rsp.getProvider();
		return true;
	}

	public static SkyMines getInstance() {
		return instance;
	}

	public SkyMineManager getSkyMineManager() {
		return skyMineManager;
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

	public Economy getEconomy() {
		return econ;
	}
}
