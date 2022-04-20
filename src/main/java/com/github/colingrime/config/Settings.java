package com.github.colingrime.config;

import com.github.colingrime.skymines.structure.material.MaterialVariety;
import com.github.colingrime.storage.StorageCredentials;
import com.github.colingrime.storage.StorageType;
import com.github.colingrime.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Settings {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    // database
    private StorageType storageType;
    private StorageCredentials credentials;

    // skymine options
    private int maxPerPlayer;
    private boolean replaceBlocks;
    private boolean teleportHomeOnReset;
    private boolean resetOnUpgrade;
    private boolean notifyOnResetCooldownFinish;
    private boolean notifyOnPickupCooldownFinish;
    private int pickupCooldown;
    private int placementCooldown;
    private boolean preventTokenDrop;
    private boolean overrideTransparentBlocks;

    // token info
    private Material tokenType;
    private String tokenName;
    private List<String> tokenLore;

    // upgrade: material variety
    private Map<Integer, MaterialVariety> upgradesBlockVariety;
    private Map<Integer, Double> costsBlockVariety;
    private Set<Material> allPossibleMaterials;

    // upgrade: reset cooldown
    private Map<Integer, Double> upgradesResetCooldown;
    private Map<Integer, Double> costsResetCooldown;

    // max levels
    private int maxBlockVariety;
    private int maxResetCooldown;

    // misc settings
    private boolean enableMetrics;

    public Settings(JavaPlugin plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();

        // database
        storageType = _getStorageType();
        credentials = _getCredentials();

        // skymine options
        maxPerPlayer = _getMaxPerPlayer();
        replaceBlocks = _shouldReplaceBlocks();
        teleportHomeOnReset = _shouldTeleportHomeOnReset();
        resetOnUpgrade = _shouldResetOnUpgrade();
        notifyOnResetCooldownFinish = _shouldNotifyOnResetCooldownFinish();
        notifyOnPickupCooldownFinish = _shouldNotifyOnPickupCooldownFinish();
        placementCooldown = _getPlacementCooldown();
        pickupCooldown = _getPickupCooldown();
        preventTokenDrop = _getPreventTokenDrop();
        overrideTransparentBlocks = _getOverrideTransparentBlocks();

        // token info
        tokenType = _getTokenType();
        tokenName = _getTokenName();
        tokenLore = _getTokenLore();

        // upgrade: block variety
        upgradesBlockVariety = _getUpgradesBlockVariety();
        costsBlockVariety = _getCostsBlockVariety();
        allPossibleMaterials = _getAllPossibleMaterials();

        // upgrade: reset cooldown
        upgradesResetCooldown = _getUpgradesResetCooldown();
        costsResetCooldown = _getCostsResetCooldown();

        // max levels
        maxBlockVariety = Collections.max(upgradesBlockVariety.keySet());
        maxResetCooldown = Collections.max(upgradesResetCooldown.keySet());

        // misc settings
        enableMetrics = _isMetricsEnabled();
    }

    private StorageType _getStorageType() {
        return StorageType.parse(config.getString("save.storage-type"));
    }

    public StorageType getStorageType() {
        return storageType;
    }

    private StorageCredentials _getCredentials() {
        String host = config.getString("save.host");
        int port = config.getInt("save.port");
        String database = config.getString("save.database");
        String username = config.getString("save.username");
        String password = config.getString("save.password");

        return new StorageCredentials(host, port, database, username, password);
    }

    public StorageCredentials getCredentials() {
        return credentials;
    }

    private int _getMaxPerPlayer() {
        return config.getInt("options.max-per-player");
    }

    public int getMaxPerPlayer() {
        return maxPerPlayer;
    }

    private boolean _shouldReplaceBlocks() {
        return config.getBoolean("options.replace-blocks");
    }

    public boolean shouldReplaceBlocks() {
        return replaceBlocks;
    }

    private boolean _shouldTeleportHomeOnReset() {
        return config.getBoolean("options.teleport-home-on-reset");
    }

    public boolean shouldTeleportHomeOnReset() {
        return teleportHomeOnReset;
    }

    private boolean _shouldResetOnUpgrade() {
        return config.getBoolean("options.reset-on-upgrade");
    }

    public boolean shouldResetOnUpgrade() {
        return resetOnUpgrade;
    }

    private boolean _shouldNotifyOnResetCooldownFinish() {
        return config.getBoolean("options.notify-on-reset-cooldown-finish");
    }

    public boolean shouldNotifyOnResetCooldownFinish() {
        return notifyOnResetCooldownFinish;
    }

    private boolean _shouldNotifyOnPickupCooldownFinish() {
        return config.getBoolean("options.notify-on-pickup-cooldown-finish");
    }

    public boolean shouldNotifyOnPickupCooldownFinish() {
        return notifyOnPickupCooldownFinish;
    }

    private int _getPickupCooldown() {
        return config.getInt("options.pickup-cooldown");
    }

    public int getPickupCooldown() {
        return pickupCooldown;
    }

    private int _getPlacementCooldown() {
        return config.getInt("options.placement-cooldown");
    }

    public int getPlacementCooldown() {
        return placementCooldown;
    }

    private boolean _getPreventTokenDrop() {
        return config.getBoolean("options.prevent-token-drop");
    }

    public boolean getPreventTokenDrop() {
        return preventTokenDrop;
    }

    private boolean _getOverrideTransparentBlocks() {
        return config.getBoolean("options.override-transparent-blocks", true);
    }

    public boolean getOverrideTransparentBlocks() {
        return overrideTransparentBlocks;
    }

    private Material _getTokenType() {
        String materialName = config.getString("token.type");
        if (materialName == null || Material.matchMaterial(materialName) == null) {
            return Material.TRIPWIRE_HOOK;
        }
        return Material.matchMaterial(materialName);
    }

    public Material getTokenType() {
        return tokenType;
    }

    private String _getTokenName() {
        return Utils.color(config.getString("token.name"));
    }

    public String getTokenName() {
        return tokenName;
    }

    private List<String> _getTokenLore() {
        return Utils.color(config.getStringList("token.lore"));
    }

    public List<String> getTokenLore() {
        return tokenLore;
    }

    private Map<Integer, MaterialVariety> _getUpgradesBlockVariety() {
        Map<Integer, MaterialVariety> upgradesBlockVariety = new HashMap<>();
        ConfigurationSection sec = config.getConfigurationSection("upgrades.block-variety");
        if (sec == null) {
            return upgradesBlockVariety;
        }

        // goes over each level in the block variety section
        for (String level : sec.getKeys(false)) {
            if (!level.matches("\\d+")) {
                continue;
            }

            // sets up collection of types with chances
            MaterialVariety blockVariety = new MaterialVariety();
            for (String types : sec.getStringList(level + ".upgrade")) {
                String[] type = types.split(" ");
                blockVariety.addType(type[0], type[1]);
            }

            upgradesBlockVariety.put(Integer.parseInt(level), blockVariety);
        }

        return upgradesBlockVariety;
    }

    public Map<Integer, MaterialVariety> getUpgradesBlockVariety() {
        return upgradesBlockVariety;
    }

    private Map<Integer, Double> _getCostsBlockVariety() {
        Map<Integer, Double> costsBlockVariety = new HashMap<>();
        ConfigurationSection sec = config.getConfigurationSection("upgrades.block-variety");
        if (sec == null) {
            return costsBlockVariety;
        }

        // goes over each level in the block variety section
        for (String level : sec.getKeys(false)) {
            if (level.matches("\\d+")) {
                costsBlockVariety.put(Integer.parseInt(level), sec.getDouble(level + ".cost"));
            }
        }

        return costsBlockVariety;
    }

    public Map<Integer, Double> getCostsBlockVariety() {
        return costsBlockVariety;
    }

    private Set<Material> _getAllPossibleMaterials() {
        Set<Material> allPossibleMaterials = new HashSet<>();
        for (MaterialVariety materialVariety : getUpgradesBlockVariety().values()) {
            allPossibleMaterials.addAll(materialVariety.getMaterials());
        }
        return allPossibleMaterials;
    }

    public Set<Material> getAllPossibleMaterials() {
        return allPossibleMaterials;
    }

    private Map<Integer, Double> _getUpgradesResetCooldown() {
        Map<Integer, Double> upgradesResetCooldown = new HashMap<>();
        ConfigurationSection sec = config.getConfigurationSection("upgrades.reset-cooldown");
        if (sec == null) {
            return upgradesResetCooldown;
        }

        // goes over each level in the reset cooldown section
        for (String level : sec.getKeys(false)) {
            if (!level.matches("\\d+")) {
                continue;
            }

            // checks to make sure time is valid
            String cooldownString = sec.getString(level + ".upgrade");
            if (cooldownString == null || !cooldownString.split(" ")[0].matches("\\d+(\\.\\d+)?")) {
                continue;
            }
            String[] cooldownArray = cooldownString.split(" ");

            // checks for minute units
            double cooldown = Double.parseDouble(cooldownArray[0]);
            if (cooldownArray.length > 1 && cooldownArray[1].contains("minute")) {
                cooldown *= 60;
            }

            upgradesResetCooldown.put(Integer.parseInt(level), cooldown);
        }

        return upgradesResetCooldown;
    }

    public Map<Integer, Double> getUpgradesResetCooldown() {
        return upgradesResetCooldown;
    }

    private Map<Integer, Double> _getCostsResetCooldown() {
        Map<Integer, Double> costsResetCooldown = new HashMap<>();
        ConfigurationSection sec = config.getConfigurationSection("upgrades.reset-cooldown");
        if (sec == null) {
            return costsResetCooldown;
        }

        // goes over each level in the block variety section
        for (String level : sec.getKeys(false)) {
            if (level.matches("\\d+")) {
                costsResetCooldown.put(Integer.parseInt(level), sec.getDouble(level + ".cost"));
            }
        }

        return costsResetCooldown;
    }

    public Map<Integer, Double> getCostsResetCooldown() {
        return costsResetCooldown;
    }

    public int getMaxBlockVariety() {
        return maxBlockVariety;
    }

    public int getMaxResetCooldown() {
        return maxResetCooldown;
    }

    private boolean _isMetricsEnabled() {
        return config.getBoolean("misc.enable-metrics", true);
    }

    public boolean isMetricsEnabled() {
        return enableMetrics;
    }
}