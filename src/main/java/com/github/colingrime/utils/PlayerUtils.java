package com.github.colingrime.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PlayerUtils {

    private PlayerUtils() {
    }

    /**
     * Removes a single item from the player's hand.
     *
     * @param player any player
     */
    public static void removeOneItemFromHand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            return;
        }

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }

    /**
     * Gives the player the item, or drops it on
     * the ground if their inventory is full.
     *
     * @param player any player
     * @param item   any item
     */
    public static void giveItemOrDrop(Player player, ItemStack item) {
        for (ItemStack itemDrop : player.getInventory().addItem(item).values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemDrop);
        }
    }
}
