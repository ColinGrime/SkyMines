package com.github.colingrime;

import com.github.colingrime.commands.skymines.SkyMinesBaseCommand;
import com.github.colingrime.commands.skymines.subcommands.*;
import com.github.colingrime.config.Settings;
import com.github.colingrime.dependencies.DependencyManager;
import com.github.colingrime.listeners.PanelListeners;
import com.github.colingrime.listeners.PlayerListeners;
import com.github.colingrime.listeners.SkyMineListeners;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.panel.setup.PanelSettings;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.factory.DefaultSkyMineFactory;
import com.github.colingrime.skymines.manager.CooldownManager;
import com.github.colingrime.skymines.manager.SkyMineManager;
import com.github.colingrime.skymines.token.DefaultSkyMineToken;
import com.github.colingrime.storage.Storage;
import com.github.colingrime.storage.StorageFactory;
import com.github.colingrime.utils.Logger;
import com.github.colingrime.utils.Timer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
		dependencyManager = new DependencyManager(this);
		dependencyManager.registerDependencies();

		instance = this;
		skyMineManager = new SkyMineManager(this, new DefaultSkyMineFactory(this), new DefaultSkyMineToken(this));
		cooldownManager = new CooldownManager(this);

		loadData();
		registerCommands();
		registerListeners();

		List<UUID> uuidsToLoad = Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
		Bukkit.getScheduler().runTaskAsynchronously(this, () -> loadStorage(uuidsToLoad));
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
	private void loadStorage(List<UUID> uuidsToLoad) {
		try {
			storage = new StorageFactory(this).createStorage();
			storage.init();
			Timer.time(() -> storage.loadMines(), "Mines have been loaded in %s ms");
		} catch (Exception ex) {
			Logger.severe("Storage has failed to load. Plugin has been disabled.");
			ex.printStackTrace();
			getServer().getPluginManager().disablePlugin(this);
		}

		// load initial mines (because some people like to reload the server...)
		for (UUID uuid : uuidsToLoad) {
			for (SkyMine skyMine : skyMineManager.getSkyMines(uuid)) {
				skyMine.getStructure().setup();
			}
		}
	}

	private void registerCommands() {
		SkyMinesBaseCommand skyMines = new SkyMinesBaseCommand(this);
		skyMines.registerSubCommand(new SkyMinesListSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesHomeSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesSetHomeSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesPanelSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesUpgradeSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesResetSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesGiveSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesPickupSubCommand(this));
		skyMines.registerSubCommand(new SkyMinesReloadSubCommand(this));
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
