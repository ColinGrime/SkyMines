package com.github.colingrime.storage.sql.connection.hikari;

import com.github.colingrime.storage.StorageCredentials;
import com.github.colingrime.storage.sql.connection.ConnectionProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class HikariConnectionProvider implements ConnectionProvider {

    private final StorageCredentials credentials;
    private HikariDataSource hikari;

    public HikariConnectionProvider(StorageCredentials credentials) {
        this.credentials = credentials;
    }

    protected abstract void configureDatabase(HikariConfig config, StorageCredentials storage);

    protected void overrideProperties(Map<String, String> properties) {
        // https://github.com/brettwooldridge/HikariCP/wiki/Rapid-Recovery
        properties.putIfAbsent("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));
    }

    protected void setProperties(HikariConfig config, Map<String, String> properties) {
        for (Map.Entry<String, String> property : properties.entrySet()) {
            config.addDataSourceProperty(property.getKey(), property.getValue());
        }
    }

    @Override
    public void init() {
        HikariConfig config = new HikariConfig();
        configureDatabase(config, credentials);

        Map<String, String> properties = new HashMap<>();
        overrideProperties(properties);
        setProperties(config, properties);

        // TODO learn how to configure connection pool

        hikari = new HikariDataSource(config);
    }

    @Override
    public void shutdown() {
        if (hikari != null) {
            hikari.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (hikari == null) {
            throw new SQLException("Connection has failed, hikari is null.");
        }

        Connection connection = hikari.getConnection();
        if (connection == null) {
            throw new SQLException("Connection has failed, connection is null.");
        }

        return connection;
    }
}
