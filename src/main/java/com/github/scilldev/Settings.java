package com.github.scilldev;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Settings {

	private JavaPlugin plugin;
	private FileConfiguration config;

	// mysql stuff
	private String mysqlHost;
	private int mysqlPort;
	private String mysqlDatabase;
	private String mysqlUsername;
	private String mysqlPassword;

	// auto pickup stuff
	private boolean autoBlockXp;
	private boolean autoMobXp;
	private List<EntityType> enabledMobs;

	public Settings(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void reload() {
		config = plugin.getConfig();

		// mysql
		mysqlHost = _getMysqlHost();
		mysqlPort = _getMysqlPort();
		mysqlDatabase = _getMysqlDatabase();
		mysqlUsername = _getMysqlUsername();
		mysqlPassword = _getMysqlPassword();

		// auto pickup stuff
		autoBlockXp = _isAutoBlockXpEnabled();
		autoMobXp = _isAutoMobXpEnabled();
		enabledMobs = _getEnabledMobs();
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

	private boolean _isAutoBlockXpEnabled() {
		return config.getBoolean("auto-pickup.block-xp");
	}

	public boolean isAutoBlockXpEnabled() {
		return autoBlockXp;
	}

	private boolean _isAutoMobXpEnabled() {
		return config.getBoolean("auto-pickup.mob-xp");
	}

	public boolean isAutoMobXpEnabled() {
		return autoMobXp;
	}

	private List<EntityType> _getEnabledMobs() {
		List<EntityType> enabledMobs = new ArrayList<>();
		for (String mobName : config.getStringList("auto-pickup.enabled-mobs")) {
			for (EntityType entity : EntityType.values()) {
				if (entity.name().equalsIgnoreCase(mobName)) {
					enabledMobs.add(entity);
				}
			}
		}

		return enabledMobs;
	}

	public List<EntityType> getEnabledMobs() {
		return enabledMobs;
	}
}