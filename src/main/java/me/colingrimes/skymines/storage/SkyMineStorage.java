package me.colingrimes.skymines.storage;

import me.colingrimes.midnight.geometry.Pose;
import me.colingrimes.midnight.storage.sql.SqlStorage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.midnight.util.io.DatabaseUtils;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.skymine.DefaultSkyMine;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.structure.MineStructure;
import me.colingrimes.skymines.skymine.upgrade.SkyMineUpgrades;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.UUID;

public class SkyMineStorage extends SqlStorage<SkyMine> {

	private static final String MINES_SELECT_ALL = "SELECT * FROM 'skymines_mines'";
	private static final String MINES_EXIST      = "SELECT 1 FROM 'skymines_mines' WHERE uuid=? LIMIT 1";
	private static final String MINES_INSERT     = "INSERT INTO 'skymines_mines' (uuid, owner, identifier, structure, upgrades, home) VALUES(?, ?, ?, ?, ?, ?)";
	private static final String MINES_UPDATE     = "UPDATE 'skymines_mines' SET upgrades=?, home=? WHERE uuid=?";
	private static final String MINES_DELETE     = "DELETE FROM 'skymines_mines' WHERE uuid=?";

	private final SkyMines plugin;
	private boolean loaded = false;

	public SkyMineStorage(@Nonnull SkyMines plugin, @Nonnull ConnectionProvider connectionProvider) {
		super(plugin, connectionProvider);
		this.plugin = plugin;
	}

	public void loadMines() throws SQLException {
		if (loaded) {
			return;
		}
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = prepare(c, MINES_SELECT_ALL)) {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					UUID uuid = DatabaseUtils.getUUID(type, rs, "uuid");
					UUID owner = DatabaseUtils.getUUID(type, rs, "owner");
					String identifier = rs.getString("identifier");
					MineStructure structure = DatabaseUtils.getJson(type, rs, "structure", MineStructure.class);
					SkyMineUpgrades upgrades = DatabaseUtils.getJson(type, rs, "upgrades", SkyMineUpgrades.class);
					Pose home = DatabaseUtils.getJson(type, rs, "home", Pose.class);
					if (uuid != null && owner != null && identifier != null && structure != null && upgrades != null && home != null) {
						SkyMine skyMine = new DefaultSkyMine(plugin, uuid, owner, identifier, structure, upgrades, home);
						plugin.getSkyMineManager().addSkyMine(owner, skyMine);
					}
				}
			}
		}
		loaded = true;
	}

	@Override
	public void save(@Nonnull SkyMine skyMine) throws SQLException {
		boolean update;
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = prepare(c, MINES_EXIST)) {
				DatabaseUtils.setUUID(type, ps, 1, skyMine.getUUID());
				update = ps.executeQuery().next();
			}
		}

		if (update) {
			updateMine(skyMine);
		} else {
			insertMine(skyMine);
		}
	}

	private void updateMine(SkyMine skyMine) throws SQLException {
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = prepare(c, MINES_UPDATE)) {
				DatabaseUtils.setJson(type, ps, 1, skyMine.getUpgrades());
				DatabaseUtils.setJson(type, ps, 2, skyMine.getHome());
				DatabaseUtils.setUUID(type, ps, 3, skyMine.getUUID());
				ps.executeUpdate();
			}
		}
	}

	private void insertMine(SkyMine skyMine) throws SQLException {
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = prepare(c, MINES_INSERT)) {
				DatabaseUtils.setUUID(type, ps, 1, skyMine.getUUID());
				DatabaseUtils.setUUID(type, ps, 2, skyMine.getOwner());
				ps.setString(3, skyMine.getIdentifier());
				DatabaseUtils.setJson(type, ps, 4, skyMine.getStructure());
				DatabaseUtils.setJson(type, ps, 5, skyMine.getUpgrades());
				DatabaseUtils.setJson(type, ps, 6, skyMine.getHome());
				ps.executeUpdate();
			}
		}
	}

	@Override
	public void delete(@Nonnull SkyMine skyMine) throws SQLException {
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = prepare(c, MINES_DELETE)) {
				DatabaseUtils.setUUID(type, ps, 1, skyMine.getUUID());
				ps.executeUpdate();
			}
		}
	}

	/**
	 * Gets whether the storage has finished loading.
	 * <p>
	 * This is important because we do not want to allow any new SkyMines from being made
	 * before all the current ones are loaded. Otherwise, duplicate SkyMines could be made.
	 *
	 * @return true if the storage is finished loading
	 */
	public boolean isLoaded() {
		return loaded;
	}
}
