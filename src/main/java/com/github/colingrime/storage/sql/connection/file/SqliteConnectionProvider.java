package com.github.colingrime.storage.sql.connection.file;

import com.github.colingrime.SkyMines;
import com.github.colingrime.storage.sql.connection.ConnectionProvider;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Function;

public class SqliteConnectionProvider implements ConnectionProvider {

	private final SkyMines plugin;
	private File file;

	public SqliteConnectionProvider(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "SQLite";
	}

	@Override
	public void init() {
		file = new File(plugin.getDataFolder(), "mines.db");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void shutdown() {}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + file.toString());
	}

	@Override
	public Function<String, String> getStatementProcessor() {
		return s -> s.replace('\'', '`');
	}
}
