package com.github.colingrime.panel;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class Panel implements Listener, InventoryHolder {

    private final static Map<Player, Panel> panelViewers = new HashMap<>();

    private final JavaPlugin plugin;
    private final Inventory inventory;
    private final Player viewer;
    private final Map<Integer, MenuAction> actions = new HashMap<>();

    public Panel(JavaPlugin plugin, Inventory inventory, Player viewer) {
        this.plugin = plugin;
        this.inventory = inventory;
        this.viewer = viewer;
    }

    public static Map<Player, Panel> getPanelViewers() {
        return panelViewers;
    }

    /**
     * Attempts to set up the Inventory.
     *
     * @param args any String arguments you want to pass
     * @return true if the Inventory was properly setup
     */
    protected abstract boolean setupInventory(String[] args);

    /**
     * Sets up the inventory & then display it to player.
     *
     * @param viewer Player opening the inventory
     * @param args   any String arguments you want to pass
     */
    public void openInventory(Player viewer, String... args) {
        if (!setupInventory(args)) {
            return;
        }

        panelViewers.put(viewer, this);
        Bukkit.getScheduler().runTask(plugin, () -> viewer.openInventory(inventory));
    }

    public void closeInventory(Player viewer) {
        // Runs a tick later to avoid possible conflicts with InventoryClickEvent.
        Bukkit.getScheduler().runTask(plugin, viewer::closeInventory);
    }

    public void setItem(int slotNum, Material material) {
        setItem(slotNum, new ItemStack(material));
    }

    public void setItem(int slotNum, ItemStack item) {
        setItem(slotNum, item, null);
    }

    public void setItem(int slotNum, Material material, MenuAction action) {
        setItem(slotNum, new ItemStack(material), action);
    }

    public void setItem(int slotNum, ItemStack item, MenuAction action) {
        inventory.setItem(slotNum, item);
        setAction(slotNum, action);
    }

    public void setAction(int slotNum, MenuAction action) {
        if (action != null) {
            actions.put(slotNum, action);
        }
    }

    public Player getViewer() {
        return viewer;
    }

    public Map<Integer, MenuAction> getActions() {
        return actions;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * Interface for actions ran from inventory clicks.
     */
    @FunctionalInterface
    public interface MenuAction {
        void click(Player player, ClickType clickType);
    }
}