package com.github.scilldev;

import com.github.scilldev.commands.skymines.SkyMinesBaseCommand;
import com.github.scilldev.commands.skymines.subcommands.*;
import com.github.scilldev.listeners.PlayerListeners;
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
	private DataSourceProvider sourceProvider;
	private Database database;
	private Economy econ = null;
	private boolean isDatabaseEnabled = true;

	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			Logger.severe("No Vault dependency found. Plugin has been disabled.");
			getServer().getPluginManager().disablePlugin(this);
		}

		skyMineManager = new SkyMineManager();
		loadData();

		try {
			// initialize data provider and test connection
			sourceProvider = new MySqlProvider(settings);
			sourceProvider.testConection();
		} catch (SQLException ex) {
			Logger.severe("Could not establish database connection. Defaulting to YAML (database is recommended).");
			isDatabaseEnabled = false;
		}

		if (isDatabaseEnabled) {
			// set up the database (build needed tables / perform updates)
			Timer.time("Setting up database...", () -> database = new MySqlDatabase(this, sourceProvider.getSource()), "Database set up in %s ms");

			// load mine data and starts the timers
			Timer.time("Loading in user data...", () -> database.getMineData().loadMines(), "Users loaded in %s ms");
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
		saveDefaultConfig();
		settings = new Settings(this);
		reload();
	}

	/**
	 * Reloads the yaml data.
	 */
	public void reload() {
		reloadConfig();
		settings.reload();
		Messages.init(this);
	}

	private void registerCommands() {
		SkyMinesBaseCommand skyMines = new SkyMinesBaseCommand(this);
		skyMines.registerSubCommand(new SkyMinesListSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesHomeSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesPanelSubCommand());
		skyMines.registerSubCommand(new SkyMinesResetSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesGiveSubCommand(this));
	}

	private void registerListeners() {
		new PlayerListeners(this);
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
