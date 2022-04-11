package com.github.colingrime.storage;

import com.github.colingrime.SkyMines;
import com.github.colingrime.storage.sql.SqlStorage;
import com.github.colingrime.storage.sql.connection.hikari.MySqlConnectionProvider;

public class StorageFactory {

	private final SkyMines plugin;

	public StorageFactory(SkyMines plugin) {
		this.plugin = plugin;
	}

	public Storage createStorage() {
		StorageType storageType = plugin.getSettings().getStorageType();
		StorageCredentials credentials = plugin.getSettings().getCredentials();

		switch (storageType) {
			case MYSQL:
				return new SqlStorage(plugin, new MySqlConnectionProvider(credentials));
			// TODO add more
		}

		return null;
	}
}
