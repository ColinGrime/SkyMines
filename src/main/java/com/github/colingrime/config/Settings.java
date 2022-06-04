package com.github.colingrime.config;

import com.github.colingrime.storage.StorageCredentials;
import com.github.colingrime.storage.StorageType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Settings {

	private final JavaPlugin plugin;
	private FileConfiguration config;

	// database
	private StorageType storageType;
	private StorageCredentials credentials;

	// skymine options
	private int maxPerPlayer;
	private boolean replaceBlocks;
	private boolean teleportHomeOnReset;
	private boolean resetOnUpgrade;
	private boolean notifyOnResetCooldownFinish;
	private boolean notifyOnPickupCooldownFinish;
	private int pickupCooldown;
	private int placementCooldown;
	private boolean preventTokenDrop;
	private boolean overrideTransparentBlocks;

	// misc settings
	private boolean enableMetrics;

	public Settings(JavaPlugin plugin) {
		this.plugin = plugin;
		this.plugin.saveDefaultConfig();
	}

	public void reload() {
		plugin.reloadConfig();
		config = plugin.getConfig();

		// database
		storageType = _getStorageType();
		credentials = _getCredentials();

		// skymine options
		maxPerPlayer = _getMaxPerPlayer();
		replaceBlocks = _shouldReplaceBlocks();
		teleportHomeOnReset = _shouldTeleportHomeOnReset();
		resetOnUpgrade = _shouldResetOnUpgrade();
		notifyOnResetCooldownFinish = _shouldNotifyOnResetCooldownFinish();
		notifyOnPickupCooldownFinish = _shouldNotifyOnPickupCooldownFinish();
		placementCooldown = _getPlacementCooldown();
		pickupCooldown = _getPickupCooldown();
		preventTokenDrop = _getPreventTokenDrop();
		overrideTransparentBlocks = _getOverrideTransparentBlocks();

		// misc settings
		enableMetrics = _isMetricsEnabled();
	}

	private StorageType _getStorageType() {
		return StorageType.parse(config.getString("save.storage-type"));
	}

	public StorageType getStorageType() {
		return storageType;
	}

	private StorageCredentials _getCredentials() {
		String host = config.getString("save.host");
		int port = config.getInt("save.port");
		String database = config.getString("save.database");
		String username = config.getString("save.username");
		String password = config.getString("save.password");

		return new StorageCredentials(host, port, database, username, password);
	}

	public StorageCredentials getCredentials() {
		return credentials;
	}

	private int _getMaxPerPlayer() {
		return config.getInt("options.max-per-player");
	}

	public int getMaxPerPlayer() {
		return maxPerPlayer;
	}

	private boolean _shouldReplaceBlocks() {
		return config.getBoolean("options.replace-blocks");
	}

	public boolean shouldReplaceBlocks() {
		return replaceBlocks;
	}

	private boolean _shouldTeleportHomeOnReset() {
		return config.getBoolean("options.teleport-home-on-reset");
	}

	public boolean shouldTeleportHomeOnReset() {
		return teleportHomeOnReset;
	}

	private boolean _shouldResetOnUpgrade() {
		return config.getBoolean("options.reset-on-upgrade");
	}

	public boolean shouldResetOnUpgrade() {
		return resetOnUpgrade;
	}

	private boolean _shouldNotifyOnResetCooldownFinish() {
		return config.getBoolean("options.notify-on-reset-cooldown-finish");
	}

	public boolean shouldNotifyOnResetCooldownFinish() {
		return notifyOnResetCooldownFinish;
	}

	private boolean _shouldNotifyOnPickupCooldownFinish() {
		return config.getBoolean("options.notify-on-pickup-cooldown-finish");
	}

	public boolean shouldNotifyOnPickupCooldownFinish() {
		return notifyOnPickupCooldownFinish;
	}

	private int _getPickupCooldown() {
		return config.getInt("options.pickup-cooldown");
	}

	public int getPickupCooldown() {
		return pickupCooldown;
	}

	private int _getPlacementCooldown() {
		return config.getInt("options.placement-cooldown");
	}

	public int getPlacementCooldown() {
		return placementCooldown;
	}

	private boolean _getPreventTokenDrop() {
		return config.getBoolean("options.prevent-token-drop");
	}

	public boolean getPreventTokenDrop() {
		return preventTokenDrop;
	}

	private boolean _getOverrideTransparentBlocks() {
		return config.getBoolean("options.override-transparent-blocks", true);
	}

	public boolean getOverrideTransparentBlocks() {
		return overrideTransparentBlocks;
	}

	private boolean _isMetricsEnabled() {
		return config.getBoolean("misc.enable-metrics", true);
	}

	public boolean isMetricsEnabled() {
		return enableMetrics;
	}
}