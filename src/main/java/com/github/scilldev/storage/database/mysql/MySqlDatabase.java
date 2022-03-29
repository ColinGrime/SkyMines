package com.github.scilldev.storage.database.mysql;

import com.github.scilldev.SkyMines;
import com.github.scilldev.storage.database.Database;
import com.github.scilldev.storage.MineData;
import com.github.scilldev.utils.Logger;
import com.github.scilldev.utils.Timer;
import org.bukkit.Bukkit;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class MySqlDatabase implements Database {

	private final String GET_UPDATES = "SELECT * FROM skymines_version;";

	private final int latestVersion = 1;
	private final int latestPatch = 0;

	private final SkyMines plugin;
	private final DataSource source;
	private final MineData mineData;

	private int version = 1, patch = 0;

	public MySqlDatabase(SkyMines plugin, DataSource source) {
		this.plugin = plugin;
		this.source = source;
		this.mineData = new MySqlMineData(plugin, source);

		try {
			setupVersionTable();
			if (checkForUpdates()) {
				performUpdates();
			} else {
				Logger.log("Database is up-to-date (version " + version + "." + patch + ")");
			}

			init();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setupVersionTable() throws SQLException {
		runQueries("versioning.sql");
	}

	@Override
	public boolean checkForUpdates() throws SQLException {
		try (Connection c = source.getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(GET_UPDATES)) {
				ResultSet resultSet = ps.executeQuery();
				if (resultSet.next()) {
					version = resultSet.getInt("version");
					patch = resultSet.getInt("patch");
				}
			}
		}

		return !(version == latestVersion && patch == latestPatch);
	}

	@Override
	public void performUpdates() {}

	@Override
	public void init() throws SQLException {
		runQueries("version_" + version + "/setup.sql");
	}

	/**
	 * Runs the queries located in the file ptah.
	 * @param path path of specific query file
	 * @throws SQLException DB failed to load
	 */
	private void runQueries(String path) throws SQLException {
		for (String query : getQueries(path)) {
			if (query.isEmpty()) {
				continue;
			}

			try (Connection c = source.getConnection()) {
				try (PreparedStatement ps = c.prepareStatement(query)) {
					ps.execute();
				}
			}
		}
	}

	/**
	 * Gets database queries from resources package.
	 * @param path path of specific query file
	 * @return array of queries
	 */
	private String[] getQueries(String path) {
		try (InputStream in = getClass().getResourceAsStream("/database/" + path)) {
			String query = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
			return query.split(";");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new String[]{};
	}

	@Override
	public MineData getMineData() {
		return mineData;
	}

	@Override
	public void startTimers() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
			Timer.time(mineData::saveMines, "Mines saved in %s ms");
		}, 2 * 60 * 20L, 2 * 60 * 20L);
	}
}
