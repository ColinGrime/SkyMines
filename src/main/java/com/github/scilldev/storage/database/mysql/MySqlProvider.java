package com.github.scilldev.storage.database.mysql;

import com.github.scilldev.storage.database.DataSourceProvider;
import com.github.scilldev.config.Settings;
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
				+ settings.getMysqlHost() + ":"
				+ settings.getMysqlPort() + "/"
				+ settings.getMysqlDatabase());
		source.setUsername(settings.getMysqlUsername());
		source.setPassword(settings.getMysqlPassword());
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
