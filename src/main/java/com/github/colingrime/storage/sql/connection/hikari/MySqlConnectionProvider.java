package com.github.colingrime.storage.sql.connection.hikari;

import com.github.colingrime.storage.StorageCredentials;
import com.zaxxer.hikari.HikariConfig;

import java.util.Map;
import java.util.function.Function;

public class MySqlConnectionProvider extends HikariConnectionProvider {

	public MySqlConnectionProvider(StorageCredentials credentials) {
		super(credentials);
	}

	@Override
	public String getName() {
		return "MySQL";
	}

	@Override
	public Function<String, String> getStatementProcessor() {
		return s -> s.replace('\'', '`');
	}

	@Override
	protected void configureDatabase(HikariConfig config, StorageCredentials credentials) {
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.setJdbcUrl("jdbc:mysql://" + credentials.host() + ":" + credentials.port() + "/" + credentials.database());
		config.setUsername(credentials.username());
		config.setPassword(credentials.password());
	}

	@Override
	protected void overrideProperties(Map<String, String> properties) {
		// https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
		properties.putIfAbsent("cachePrepStmts", "true");
		properties.putIfAbsent("prepStmtCacheSize", "250");
		properties.putIfAbsent("prepStmtCacheSqlLimit", "2048");
		properties.putIfAbsent("useServerPrepStmts", "true");
		properties.putIfAbsent("useLocalSessionState", "true");
		properties.putIfAbsent("rewriteBatchedStatements", "true");
		properties.putIfAbsent("cacheResultSetMetadata", "true");
		properties.putIfAbsent("cacheServerConfiguration", "true");
		properties.putIfAbsent("elideSetAutoCommits", "true");
		properties.putIfAbsent("maintainTimeStats", "false");

		super.overrideProperties(properties);
	}
}
