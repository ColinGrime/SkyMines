package com.github.scilldev;

import com.github.scilldev.commands.skymines.SkyMinesBaseCommand;
import com.github.scilldev.commands.skymines.subcommands.*;
import com.github.scilldev.config.Settings;
import com.github.scilldev.listeners.PanelListeners;
import com.github.scilldev.listeners.PlayerListeners;
import com.github.scilldev.locale.Messages;
import com.github.scilldev.panel.setup.PanelSettings;
import com.github.scilldev.storage.StorageType;
import com.github.scilldev.storage.database.DataSourceProvider;
import com.github.scilldev.storage.database.Database;
import com.github.scilldev.storage.database.mysql.MySqlDatabase;
import com.github.scilldev.storage.database.mysql.MySqlProvider;
import com.github.scilldev.skymines.manager.SkyMineManager;
import com.github.scilldev.utils.Logger;
import com.github.scilldev.utils.Timer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class SkyMines extends JavaPlugin {

	private SkyMineManager skyMineManager;
	private Settings settings;
	private PanelSettings panelSettings;
	private DataSourceProvider sourceProvider;
	private Database database;
	private Economy econ = null;
	private boolean isDatabaseEnabled = false;

	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			Logger.severe("No Vault dependency found. Plugin has been disabled.");
			getServer().getPluginManager().disablePlugin(this);
		}

		skyMineManager = new SkyMineManager(this);
		loadData();
		reload();

		StorageType type = settings.getStorageType();
		if (type == StorageType.None) {
			Logger.log("Your SkyMines aren't currently being saved. Pick a storage option in the config.");
		}

		// TODO make a database manager class
		if (type == StorageType.MySql) {
			try {
				// initialize data provider and test connection
				sourceProvider = new MySqlProvider(settings);
				sourceProvider.testConection();
				isDatabaseEnabled = true;
			} catch (SQLException ex) {
				Logger.severe("Could not establish database connection. Defaulting to YAML (database is recommended).");
			}
		}

		if (isDatabaseEnabled) {
			// set up the database (build needed tables / perform updates)
			Timer.time(() -> database = new MySqlDatabase(this, sourceProvider.getSource()), "Database set up in %s ms");

			// load mine data and starts the timers
			Timer.time(() -> database.getMineData().loadMines(), "Users loaded in %s ms");
			database.startTimers();
		}

		registerCommands();
		registerListeners();
	}

	@Override
	public void onDisable() {
		if (isDatabaseEnabled) {
			database.getMineData().saveMines();
			sourceProvider.close();
		}
	}

	/**
	 * Initializes the yaml data.
	 */
	private void loadData() {
		settings = new Settings(this);
		panelSettings = new PanelSettings(this);
		Messages.init(this);
	}

	/**
	 * Reloads the yaml data.
	 */
	public void reload() {
		settings.reload();
		panelSettings.reload();
		Messages.reload();
	}

	private void registerCommands() {
		SkyMinesBaseCommand skyMines = new SkyMinesBaseCommand(this);
		skyMines.registerSubCommand(new SkyMinesListSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesHomeSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesPanelSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesResetSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesGiveSubCommand(this));
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
		return econ != null;
	}

	public Settings getSettings() {
		return settings;
	}

	public PanelSettings getPanelSettings() {
		return panelSettings;
	}

	public SkyMineManager getSkyMineManager() {
		return skyMineManager;
	}

	public Database getActiveDatabase() {
		return database;
	}

	public Economy getEconomy() {
		return econ;
	}
}
