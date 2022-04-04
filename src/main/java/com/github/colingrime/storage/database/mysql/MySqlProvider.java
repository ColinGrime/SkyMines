package com.github.colingrime.storage.database.mysql;

import com.github.colingrime.storage.database.DataSourceProvider;
import com.github.colingrime.config.Settings;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlProvider implements DataSourceProvider {

	private final HikariDataSource source;

	public MySqlProvider(Settings settings) {
		this.source = new HikariDataSource();
		init(settings);
	}

	@Override
	public void init(Settings settings) {
		source.setJdbcUrl("jdbc:mysql://"
				+ settings.getHost() + ":"
				+ settings.getPort() + "/"
				+ settings.getDatabase());
		source.setUsername(settings.getUsername());
		source.setPassword(settings.getPassword());
		source.setMaximumPoolSize(10);
	}

	@Override
	public void testConection() throws SQLException {
		try (Connection conn = source.getConnection()) {
			if (!conn.isValid(5)) {
				throw new SQLException("Could not establish database connection.");
			}
		}
	}

	@Override
	public DataSource getSource() {
		return source;
	}

	@Override
	public void close() {
		source.close();
	}
}
