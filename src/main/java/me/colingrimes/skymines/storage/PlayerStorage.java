package me.colingrimes.skymines.storage;

import me.colingrimes.midnight.storage.database.DatabaseUtils;
import me.colingrimes.midnight.storage.sql.SqlStorage;
import me.colingrimes.midnight.storage.sql.connection.ConnectionProvider;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.player.PlayerSettings;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.UUID;

public class PlayerStorage extends SqlStorage<PlayerSettings> {

	private static final String PLAYERS_SELECT = "SELECT * FROM 'skymines_players' WHERE uuid=? LIMIT 1";
	private static final String PLAYERS_EXIST  = "SELECT 1 FROM 'skymines_players' WHERE uuid=? LIMIT 1";
	private static final String PLAYERS_INSERT = "INSERT INTO 'skymines_players' (uuid, notifications_enabled, auto_reset_enabled) VALUES (?, ?, ?)";
	private static final String PLAYERS_UPDATE = "UPDATE 'skymines_players' SET notifications_enabled=?, auto_reset_enabled=? WHERE uuid=?";
	private final SkyMines plugin;

	public PlayerStorage(@Nonnull SkyMines plugin, @Nonnull ConnectionProvider connectionProvider) {
		super(plugin, connectionProvider);
		this.plugin = plugin;
	}

	/**
	 * Loads in the player's settings to memory.
	 *
	 * @param player the player to load in
	 */
	public void loadPlayer(@Nonnull Player player) throws SQLException {
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = prepare(c, PLAYERS_SELECT)) {
				DatabaseUtils.setUUID(type, ps, 1, player.getUniqueId());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					UUID uuid = DatabaseUtils.getUUID(type, rs, "uuid");
					boolean notify = rs.getBoolean("notifications_enabled");
					boolean autoReset = rs.getBoolean("auto_reset_enabled");
					if (uuid != null) {
						plugin.getPlayerManager().setSettings(uuid, new PlayerSettings(plugin, uuid, notify, autoReset));
					}
				}
			}
		}
	}

	@Override
	public void save(@Nonnull PlayerSettings settings) throws SQLException {
		boolean update;
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = prepare(c, PLAYERS_EXIST)) {
				DatabaseUtils.setUUID(type, ps, 1, settings.getUUID());
				update = ps.executeQuery().next();
			}
		}

		if (update) {
			updateSettings(settings);
		} else {
			insertSettings(settings);
		}
	}

	private void updateSettings(@Nonnull PlayerSettings settings) throws SQLException {
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = prepare(c, PLAYERS_UPDATE)) {
				ps.setBoolean(1, settings.shouldNotify());
				ps.setBoolean(2, settings.shouldAutoReset());
				DatabaseUtils.setUUID(type, ps, 3, settings.getUUID());
				ps.executeUpdate();
			}
		}
	}

	private void insertSettings(@Nonnull PlayerSettings settings) throws SQLException {
		try (Connection c = provider.getConnection()) {
			try (PreparedStatement ps = prepare(c, PLAYERS_INSERT)) {
				DatabaseUtils.setUUID(type, ps, 1, settings.getUUID());
				ps.setBoolean(2, settings.shouldNotify());
				ps.setBoolean(3, settings.shouldAutoReset());
				ps.executeUpdate();
			}
		}
	}

	@Override
	public void delete(@Nonnull PlayerSettings settings) {
		// no-op
	}
}
