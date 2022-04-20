package com.github.colingrime.storage.sql;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.DefaultSkyMine;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.structure.MineStructure;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.storage.Storage;
import com.github.colingrime.storage.sql.connection.ConnectionProvider;
import com.github.colingrime.utils.Utils;
import org.bukkit.Location;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class SqlStorage implements Storage {

    private static final String MINES_SELECT = "SELECT owner, structure, home, upgrades FROM 'skymines_mines' WHERE uuid=? LIMIT 1";
    private static final String MINES_SELECT_ALL = "SELECT * FROM 'skymines_mines'";
    private static final String MINES_INSERT = "INSERT INTO 'skymines_mines' (uuid, owner, structure, home, upgrades) VALUES(?, ?, ?, ?, ?)";
    private static final String MINES_UPDATE_HOME = "UPDATE 'skymines_mines' SET home=? WHERE uuid=?";
    private static final String MINES_UPDATE_UPGRADES = "UPDATE 'skymines_mines' SET upgrades=? WHERE uuid=?";
    private static final String MINES_DELETE = "DELETE FROM 'skymines_mines' WHERE uuid=?";

    private final SkyMines plugin;
    private final ConnectionProvider connectionProvider;
    private final Function<String, String> statementProcessor;

    public SqlStorage(SkyMines plugin, ConnectionProvider connectionProvider) {
        this.plugin = plugin;
        this.connectionProvider = connectionProvider;
        this.statementProcessor = connectionProvider.getStatementProcessor();
    }

    @Override
    public void init() throws SQLException, IOException {
        connectionProvider.init();

        if (!tableExists("skymines_mines")) {
            applySchema();
        }
    }

    private boolean tableExists(String tableName) throws SQLException {
        try (Connection c = connectionProvider.getConnection()) {
            try (ResultSet rs = c.getMetaData().getTables(c.getCatalog(), null, tableName, null)) {
                while (rs.next()) {
                    if (rs.getString("TABLE_NAME").equalsIgnoreCase(tableName)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private void applySchema() throws IOException, SQLException {
        List<String> queries;
        String path = "/schema/" + connectionProvider.getName().toLowerCase() + ".sql";

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Schema file not found (" + path + ")");
            }
            queries = SchemaReader.getQueries(is);
        }

        try (Connection c = connectionProvider.getConnection()) {
            try (Statement s = c.createStatement()) {
                for (String query : queries) {
                    s.addBatch(query);
                }

                s.executeBatch();
            }
        }
    }

    @Override
    public void shutdown() {
        connectionProvider.shutdown();
    }

    @Override
    public void loadMines() throws SQLException {
        try (Connection c = connectionProvider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(statementProcessor.apply(MINES_SELECT_ALL))) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    UUID uuid = UUID.fromString(rs.getString(1));
                    UUID owner = UUID.fromString(rs.getString(2));
                    MineStructure structure = MineStructure.deserialize(rs.getString(3));
                    Location home = Utils.parseLocation(rs.getString(4));
                    SkyMineUpgrades upgrades = SkyMineUpgrades.parse(rs.getString(5));

                    if (structure != null && home != null) {
                        SkyMine skyMine = new DefaultSkyMine(plugin, uuid, owner, structure, home, upgrades);
                        plugin.getSkyMineManager().addSkyMine(owner, skyMine);
                    }
                }
            }
        }
    }

    @Override
    public void saveMine(SkyMine skyMine) throws SQLException {
        boolean update;
        try (Connection c = connectionProvider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(statementProcessor.apply(MINES_SELECT))) {
                ps.setString(1, skyMine.getUUID().toString());
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
        try (Connection c = connectionProvider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(statementProcessor.apply(MINES_UPDATE_HOME))) {
                ps.setString(1, Utils.parseLocation(skyMine.getHome()));
                ps.setString(2, skyMine.getUUID().toString());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = c.prepareStatement(statementProcessor.apply(MINES_UPDATE_UPGRADES))) {
                ps.setString(1, SkyMineUpgrades.parse(skyMine.getUpgrades()));
                ps.setString(2, skyMine.getUUID().toString());
                ps.executeUpdate();
            }
        }
    }

    private void insertMine(SkyMine skyMine) throws SQLException {
        try (Connection c = connectionProvider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(statementProcessor.apply(MINES_INSERT))) {
                ps.setString(1, skyMine.getUUID().toString());
                ps.setString(2, skyMine.getOwner().toString());
                ps.setString(3, MineStructure.serialize(skyMine.getStructure()));
                ps.setString(4, Utils.parseLocation(skyMine.getHome()));
                ps.setString(5, SkyMineUpgrades.parse(skyMine.getUpgrades()));
                ps.executeUpdate();
            }
        }
    }

    @Override
    public void deleteMine(SkyMine skyMine) throws SQLException {
        try (Connection c = connectionProvider.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(statementProcessor.apply(MINES_DELETE))) {
                ps.setString(1, skyMine.getUUID().toString());
                ps.executeUpdate();
            }
        }
    }
}
