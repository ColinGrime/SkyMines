package com.github.scilldev.config;

import com.github.scilldev.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {

	private JavaPlugin plugin;
	private FileConfiguration config;

	// mysql stuff
	private String mysqlHost;
	private int mysqlPort;
	private String mysqlDatabase;
	private String mysqlUsername;
	private String mysqlPassword;

	// token stuff
	private Material tokenType;
	private String tokenName;
	private List<String> tokenLore;

	// upgrade stuff
	private Map<Integer, BlockVariety> upgradesBlockVariety;
	private Map<Integer, Double> upgradesResetCooldown;
	private Map<Integer, Integer> upgradesIslandLimit;

	public Settings(JavaPlugin plugin) {
		this.plugin = plugin;
		this.plugin.saveDefaultConfig();
	}

	public void reload() {
		plugin.reloadConfig();
		config = plugin.getConfig();

		// mysql
		mysqlHost = _getMysqlHost();
		mysqlPort = _getMysqlPort();
		mysqlDatabase = _getMysqlDatabase();
		mysqlUsername = _getMysqlUsername();
		mysqlPassword = _getMysqlPassword();

		// token stuff
		tokenType = _getTokenType();
		tokenName = _getTokenName();
		tokenLore = _getTokenLore();

		// upgrade stuff
		upgradesBlockVariety = _getUpgradesBlockVariety();
		upgradesResetCooldown = _getUpgradesResetCooldown();
		upgradesIslandLimit = _getUpgradesIslandLimit();
	}

	private String _getMysqlHost() {
		return config.getString("mysql.host");
	}

	public String getMysqlHost() {
		return mysqlHost;
	}

	private int _getMysqlPort() {
		return config.getInt("mysql.port");
	}

	public int getMysqlPort() {
		return mysqlPort;
	}

	private String _getMysqlDatabase() {
		return config.getString("mysql.database");
	}

	public String getMysqlDatabase() {
		return mysqlDatabase;
	}

	private String _getMysqlUsername() {
		return config.getString("mysql.username");
	}

	public String getMysqlUsername() {
		return mysqlUsername;
	}

	private String _getMysqlPassword() {
		return config.getString("mysql.password");
	}

	public String getMysqlPassword() {
		return mysqlPassword;
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

	private Map<Integer, Integer> _getUpgradesIslandLimit() {
		Map<Integer, Integer> upgradesIslandLimit = new HashMap<>();
		ConfigurationSection section = config.getConfigurationSection("upgrades.amount-per-island");

		// goes over each level in the island limit section
		for (String level : section.getKeys(false)) {
			if (!level.matches("\\d+")) {
				continue;
			}

			// checks to make sure limit is valid
			String limit = section.getString(level);
			if (!limit.matches("\\d+")) {
				continue;
			}

			upgradesIslandLimit.put(Integer.parseInt(level), Integer.parseInt(limit));
		}

		return upgradesIslandLimit;
	}

	public Map<Integer, Integer> getUpgradesIslandLimit() {
		return upgradesIslandLimit;
	}
}