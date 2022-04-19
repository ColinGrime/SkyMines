package com.github.colingrime.skymines.manager;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.factory.SkyMineFactory;
import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.skymines.token.SkyMineToken;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class SkyMineManager {

    private final Map<UUID, List<SkyMine>> skyMines = new HashMap<>();

    private final SkyMines plugin;
    private final SkyMineFactory factory;
    private final SkyMineToken token;

    public SkyMineManager(SkyMines plugin, SkyMineFactory factory, SkyMineToken token) {
        this.plugin = plugin;
        this.factory = factory;
        this.token = token;
    }

    public SkyMineToken getToken() {
        return token;
    }

    /**
     * Creates a skymine if there's space available.
     *
     * @param player   any player
     * @param location location of the skymine
     * @param size     size of the skymine
     * @param upgrades upgrades of the skymine
     * @return true if the skymine was successfully created
     */
    public boolean createSkyMine(Player player, Location location, MineSize size, SkyMineUpgrades upgrades, Material borderType) {
        Optional<SkyMine> skyMine = factory.createSkyMine(player, location, size, upgrades, borderType);
        if (skyMine.isEmpty()) {
            return false;
        }

        addSkyMine(player, skyMine.get());
        skyMine.get().save();
        return true;
    }

    /**
     * @return all skymines
     */
    public List<SkyMine> getSkyMines() {
        List<SkyMine> skyMines = new ArrayList<>();
        for (List<SkyMine> skyMine : this.skyMines.values()) {
            skyMines.addAll(skyMine);
        }

        return skyMines;
    }

    /**
     * @param player any player
     * @return list of skymines the player owns
     */
    public List<SkyMine> getSkyMines(Player player) {
        return getSkyMines(player.getUniqueId());
    }

    /**
     * @param uuid uuid of player
     * @return list of skymines the uuid owns
     */
    public List<SkyMine> getSkyMines(UUID uuid) {
        return skyMines.getOrDefault(uuid, new ArrayList<>());
    }

    /**
     * @param player any player
     * @param id id number of the skymine
     * @return Optional skymine if found, or an empty Optional
     */
    public Optional<SkyMine> getSkyMine(Player player, String id) {
        return getSkyMine(player.getUniqueId(), id);
    }

    /**
     * @param uuid any uuid
     * @param id id number of the skymine
     * @return Optional skymine if found, or an empty Optional
     */
    public Optional<SkyMine> getSkyMine(UUID uuid, String id) {
        if (id.matches("\\d+")) {
            return getSkyMine(uuid, Integer.parseInt(id));
        }
        return Optional.empty();
    }

    /**
     * @param uuid any uuid
     * @param id id number of the skymine
     * @return Optional skymine if found, or an empty Optional
     */
    protected Optional<SkyMine> getSkyMine(UUID uuid, int id) {
        List<SkyMine> skyMines = getSkyMines(uuid);
        for (int i = 0; i < skyMines.size(); i++) {
            if (id == i + 1) {
                return Optional.of(skyMines.get(i));
            }
        }

        return Optional.empty();
    }

    /**
     * @param player any player
     * @param skyMine created skymine
     */
    public void addSkyMine(Player player, SkyMine skyMine) {
        addSkyMine(player.getUniqueId(), skyMine);
    }

    /**
     * @param uuid any uuid of player
     * @param skyMine created skymine
     */
    public void addSkyMine(UUID uuid, SkyMine skyMine) {
        List<SkyMine> skyMines = getSkyMines(uuid);
        skyMines.add(skyMine);
        this.skyMines.put(uuid, skyMines);
    }

    /**
     * Removes the SkyMine and deletes it from storage.
     *
     * @param player any player
     * @param skyMine removed skymine
     */
    public void removeSkyMine(Player player, SkyMine skyMine) {
        removeSkyMine(player.getUniqueId(), skyMine);
    }

    /**
     * @param uuid any uuid of player
     * @param skyMine removed skymine
     */
    public void removeSkyMine(UUID uuid, SkyMine skyMine) {
        List<SkyMine> skyMines = getSkyMines(uuid);
        skyMines.remove(skyMine);
        this.skyMines.put(uuid, skyMines);

        skyMine.getCooldown().invalidate();
        try {
            plugin.getStorage().deleteMine(skyMine);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}