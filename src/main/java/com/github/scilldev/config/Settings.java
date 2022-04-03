package com.github.scilldev.config;

import com.github.scilldev.storage.StorageType;
import com.github.scilldev.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {

	private JavaPlugin plugin;
	private FileConfiguration config;

	// database stuff
	private StorageType storageType;
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;

	// token stuff
	private Material tokenType;
	private String tokenName;
	private List<String> tokenLore;

	// upgrade stuff
	private Map<Integer, BlockVariety> upgradesBlockVariety;
	private Map<Integer, Double> upgradesResetCooldown;

	// max levels
	private int maxBlockVariety;
	private int maxResetCooldown;

	public Settings(JavaPlugin plugin) {
		this.plugin = plugin;
		this.plugin.saveDefaultConfig();
	}

	public void reload() {
		plugin.reloadConfig();
		config = plugin.getConfig();

		// database stuff
		storageType = _getStorageType();
		host = _getHost();
		port = _getPort();
		database = _getDatabase();
		username = _getUsername();
		password = _getPassword();

		// token stuff
		tokenType = _getTokenType();
		tokenName = _getTokenName();
		tokenLore = _getTokenLore();

		// upgrade stuff
		upgradesBlockVariety = _getUpgradesBlockVariety();
		upgradesResetCooldown = _getUpgradesResetCooldown();

		// max levels
		maxBlockVariety = Collections.max(upgradesBlockVariety.keySet());
		maxResetCooldown = Collections.max(upgradesResetCooldown.keySet());
	}

	private StorageType _getStorageType() {
		String type = config.getString("skymines-save.storage-type");
		for (StorageType storageType : StorageType.values()) {
			if (storageType.name().equalsIgnoreCase(type)) {
				return storageType;
			}
		}

		return StorageType.None;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	private String _getHost() {
		return config.getString("skymines-save.host");
	}

	public String getHost() {
		return host;
	}

	private int _getPort() {
		return config.getInt("skymines-save.port");
	}

	public int getPort() {
		return port;
	}

	private String _getDatabase() {
		return config.getString("skymines-save.database");
	}

	public String getDatabase() {
		return database;
	}

	private String _getUsername() {
		return config.getString("skymines-save.username");
	}

	public String getUsername() {
		return username;
	}

	private String _getPassword() {
		return config.getString("skymines-save.password");
	}

	public String getPassword() {
		return password;
	}

	private Material _getTokenType() {
		Material material = Material.getMaterial(config.getString("token.type"));
		if (material == null) {
			return Material.TRIPWIRE_HOOK;
		}
		return material;
	}

	public Material getTokenType() {
		return tokenType;
	}

	private String _getTokenName() {
		return Utils.color(config.getString("token.name"));
	}

	public String getTokenName() {
		return tokenName;
	}

	private List<String> _getTokenLore() {
		return Utils.color(config.getStringList("token.lore"));
	}

	public List<String> getTokenLore() {
		return tokenLore;
	}

	private Map<Integer, BlockVariety> _getUpgradesBlockVariety() {
		Map<Integer, BlockVariety> upgradesBlockVariety = new HashMap<>();
		ConfigurationSection section = config.getConfigurationSection("upgrades.block-variety");

		// goes over each level in the block variety section
		for (String level : section.getKeys(false)) {
			if (!level.matches("\\d+")) {
				continue;
			}

			// sets up collection of types with chances
			BlockVariety blockVariety = new BlockVariety();
			for (String types : section.getStringList(level)) {
				String[] type = types.split(" ");
				blockVariety.addType(type[0], type[1]);
			}

			upgradesBlockVariety.put(Integer.parseInt(level), blockVariety);
		}

		return upgradesBlockVariety;
	}

	public Map<Integer, BlockVariety> getUpgradesBlockVariety() {
		return upgradesBlockVariety;
	}

	private Map<Integer, Double> _getUpgradesResetCooldown() {
		Map<Integer, Double> upgradesResetCooldown = new HashMap<>();
		ConfigurationSection section = config.getConfigurationSection("upgrades.reset-cooldown");

		// goes over each level in the reset cooldown section
		for (String level : section.getKeys(false)) {
			if (!level.matches("\\d+")) {
				continue;
			}

			// checks to make sure time is valid
			String[] cooldownString = section.getString(level).split(" ");
			if (!cooldownString[0].matches("\\d+(\\.\\d+)?")) {
				continue;
			}

			// checks for minute units
			double cooldown = Double.parseDouble(cooldownString[0]);
			if (cooldownString.length > 1 && cooldownString[1].contains("minute")) {
				cooldown *= 60;
			}

			upgradesResetCooldown.put(Integer.parseInt(level), cooldown);
		}

		return upgradesResetCooldown;
	}

	public Map<Integer, Double> getUpgradesResetCooldown() {
		return upgradesResetCooldown;
	}

	public int getMaxBlockVariety() {
		return maxBlockVariety;
	}

	public int getMaxResetCooldown() {
		return maxResetCooldown;
	}
}