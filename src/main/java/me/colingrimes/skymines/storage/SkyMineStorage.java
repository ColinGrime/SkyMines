package me.colingrimes.skymines.storage;

import me.colingrimes.midnight.storage.sql.SqlStorage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.midnight.util.io.DatabaseUtils;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.skymine.DefaultSkyMine;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.structure.MineStructure;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import me.colingrimes.skymines.util.Utils;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.UUID;

public class SkyMineStorage extends SqlStorage<SkyMine> {

	private static final String MINES_SELECT_ALL = "SELECT * FROM 'skymines_mines'";
	private static final String MINES_EXIST = "SELECT 1 FROM 'skymines_mines' WHERE uuid=? LIMIT 1";
	private static final String MINES_INSERT = "INSERT INTO 'skymines_mines' (uuid, owner, structure, upgrades, home) VALUES(?, ?, ?, ?, ?)";
	private static final String MINES_UPDATE = "UPDATE 'skymines_mines' SET upgrades=?, home=? WHERE uuid=?";
	private static final String MINES_DELETE = "DELETE FROM 'skymines_mines' WHERE uuid=?";

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
			try (PreparedStatement ps = c.prepareStatement(processor.apply(MINES_SELECT_ALL))) {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					UUID uuid = DatabaseUtils.getUUID(rs, "uuid", database);
					UUID owner = DatabaseUtils.getUUID(rs, "owner", database);
					MineStructure structure = MineStructure.deserialize(rs.getString("structure"));
					SkyMineUpgrades upgrades = SkyMineUpgrades.parse(rs.getString("upgrades"));
					Location home = Utils.parseLocation(rs.getString("home"));
					if (uuid != null && owner != null && structure != null && home != null) {
						SkyMine skyMine = new DefaultSkyMine(plugin, uuid, owner, structure, upgrades, home);
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
			try (PreparedStatement ps = c.prepareStatement(processor.apply(MINES_EXIST))) {
				DatabaseUtils.setUUID(ps, 1, skyMine.getUUID(), database);
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
			try (PreparedStatement ps = c.prepareStatement(processor.apply(MINES_UPDATE))) {
				ps.setString(1, SkyMineUpgrades.parse(skyMine.getUpgrades()));
				ps.setString(2, Utils.parseLocation(skyMine.getHome()));
				DatabaseUtils.setUUID(ps, 3, skyMine.getUUID(), database);
				ps.executeUpdate();
			}
		}
	}

	private void insertMine(SkyMine skyMine) throws SQLException {
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(processor.apply(MINES_INSERT))) {
				DatabaseUtils.setUUID(ps, 1, skyMine.getUUID(), database);
				DatabaseUtils.setUUID(ps, 2, skyMine.getOwner(), database);
				ps.setString(3, skyMine.getStructure().serialize());
				ps.setString(4, SkyMineUpgrades.parse(skyMine.getUpgrades()));
				ps.setString(5, Utils.parseLocation(skyMine.getHome()));
				ps.executeUpdate();
			}
		}
	}

	@Override
	public void delete(@Nonnull SkyMine skyMine) throws SQLException {
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = c.prepareStatement(processor.apply(MINES_DELETE))) {
				DatabaseUtils.setUUID(ps, 1, skyMine.getUUID(), database);
				ps.executeUpdate();
			}
		}
	}

	/**
	 * Gets whether the storage has finished loading.
	 * <p>
	 * This is important because we do not want to allow any new SkyMines from being
	 * made before all the current ones are loaded.
	 * Otherwise, duplicate SkyMines could be made.
	 *
	 * @return true if the storage is finished loading
	 */
	public boolean isLoaded() {
		return loaded;
	}
}
