package com.github.colingrime.storage.yaml;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.DefaultSkyMine;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.structure.MineStructure;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.storage.Storage;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class YamlStorage implements Storage {

    private final SkyMines plugin;
    private File file;
    private FileConfiguration config;

    public YamlStorage(SkyMines plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() throws Exception {
        file = new File(plugin.getDataFolder(), "mines.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void loadMines() {
        ConfigurationSection sec = config.getConfigurationSection("");
        for (String uuidString : Objects.requireNonNull(sec).getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            UUID owner = UUID.fromString(Objects.requireNonNull(sec.getString(uuidString + ".owner")));
            MineStructure structure = MineStructure.deserialize(Objects.requireNonNull(sec.getString(uuidString + ".structure")));
            Location home = sec.getObject(uuidString + ".home", Location.class);
            SkyMineUpgrades upgrades = SkyMineUpgrades.parse(Objects.requireNonNull(sec.getString(uuidString + ".upgrades")));

            if (structure != null && home != null) {
                SkyMine skyMine = new DefaultSkyMine(plugin, uuid, owner, structure, home, upgrades);
                plugin.getSkyMineManager().addSkyMine(owner, skyMine);
            }
        }
    }

    @Override
    public void saveMine(SkyMine skyMine) {
        String uuid = skyMine.getUUID().toString();
        config.set(uuid + ".owner", skyMine.getOwner().toString());
        config.set(uuid + ".structure", MineStructure.serialize(skyMine.getStructure()));
        config.set(uuid + ".home", skyMine.getHome());
        config.set(uuid + ".upgrades", SkyMineUpgrades.parse(skyMine.getUpgrades()));
        save();
    }

    @Override
    public void deleteMine(SkyMine skyMine) {
        config.set(skyMine.getUUID().toString(), null);
        save();
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}