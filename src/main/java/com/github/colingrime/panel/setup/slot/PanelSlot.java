package com.github.colingrime.panel.setup.slot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class PanelSlot {

    public ItemStack getItem() {
        ItemStack item = new ItemStack(getType());
        if (item.getType() == Material.AIR || item.getItemMeta() == null) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getName());
        meta.setLore(getLore());

        item.setItemMeta(meta);
        return item;
    }

    public abstract Material getType();

    public abstract String getName();

    public abstract List<String> getLore();

    public abstract String getCommand();
}
