package com.github.colingrime.storage;

import com.github.colingrime.SkyMines;
import com.github.colingrime.storage.sql.SqlStorage;
import com.github.colingrime.storage.sql.connection.file.SqliteConnectionProvider;
import com.github.colingrime.storage.sql.connection.hikari.MySqlConnectionProvider;
import com.github.colingrime.storage.yaml.YamlStorage;

public record StorageFactory(SkyMines plugin) {

    public Storage createStorage() {
        StorageType storageType = plugin.getSettings().getStorageType();
        StorageCredentials credentials = plugin.getSettings().getCredentials();

        return switch (storageType) {
            case MYSQL -> new SqlStorage(plugin, new MySqlConnectionProvider(credentials));
            case SQLITE -> new SqlStorage(plugin, new SqliteConnectionProvider(plugin));
            case YAML -> new YamlStorage(plugin);
        };
    }
}
