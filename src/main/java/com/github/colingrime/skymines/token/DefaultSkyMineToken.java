package com.github.colingrime.skymines.token;

import com.github.colingrime.SkyMines;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

public class DefaultSkyMineToken implements SkyMineToken {

    private final SkyMines plugin;

    public DefaultSkyMineToken(SkyMines plugin) {
        this.plugin = plugin;
    }

    @Override
    public ItemStack getToken() {
        return getToken(new MineSize(10, 10, 10), Material.BEDROCK);
    }

    @Override
    public ItemStack getToken(MineSize size, Material borderType) {
        return getToken(size, new SkyMineUpgrades(plugin), borderType);
    }

    @Override
    public ItemStack getToken(MineSize size, SkyMineUpgrades upgrades, Material borderType) {
        ItemStack item = new ItemStack(plugin.getSettings().getTokenType());
        ItemMeta meta = item.getItemMeta();
        Replacer replacer = getReplacer(size);

        meta.setDisplayName(replacer.replace(plugin.getSettings().getTokenName()));
        meta.setLore(replacer.replace(plugin.getSettings().getTokenLore()));
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean("skymine", true);
        nbtItem.setObject("skymine-size", size);
        nbtItem.setInteger("skymine-blockvariety", upgrades.getBlockVarietyUpgrade().getLevel());
        nbtItem.setInteger("skymine-resetcooldown", upgrades.getResetCooldownUpgrade().getLevel());
        nbtItem.setObject("skymine-bordertype", borderType);
        return nbtItem.getItem();
    }

    private Replacer getReplacer(MineSize size) {
        return new Replacer("%length%", size.getLength())
                .add("%height%", size.getHeight())
                .add("%width%", size.getWidth());
    }

    @Override
    public boolean isToken(ItemStack item) {
        if (isInvalidItem(item)) {
            return false;
        }

        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getBoolean("skymine");
    }

    @Override
    public Optional<MineSize> getMineSize(ItemStack item) {
        if (isInvalidItem(item)) {
            return Optional.empty();
        }

        NBTItem nbtItem = new NBTItem(item);
        return Optional.ofNullable(nbtItem.getObject("skymine-size", MineSize.class));
    }

    @Override
    public Optional<SkyMineUpgrades> getUpgrades(ItemStack item) {
        if (isInvalidItem(item)) {
            return Optional.empty();
        }

        NBTItem nbtItem = new NBTItem(item);
        int blockVarietyLevel = nbtItem.getInteger("skymine-blockvariety");
        int resetCooldown = nbtItem.getInteger("skymine-resetcooldown");
        return Optional.of(new SkyMineUpgrades(plugin, blockVarietyLevel, resetCooldown));
    }

    @Override
    public Optional<Material> getBorderType(ItemStack item) {
        if (isInvalidItem(item)) {
            return Optional.empty();
        }

        NBTItem nbtItem = new NBTItem(item);
        return Optional.ofNullable(nbtItem.getObject("skymine-bordertype", Material.class));
    }

    private boolean isInvalidItem(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
}