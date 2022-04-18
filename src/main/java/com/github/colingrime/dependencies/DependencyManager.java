package com.github.colingrime.dependencies;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.structure.behavior.BuildBehavior;
import com.github.colingrime.utils.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;

public class DependencyManager {

    private final SkyMines plugin;

    private Economy econ;
    private BuildBehavior buildbehavior;

    public DependencyManager(SkyMines plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers all the depenencies.
     */
    public void registerDependencies() throws DependencyFailureException {
        for (Dependency dependency : Dependency.values()) {
            registerDependency(dependency);
        }
    }

    /**
     * Registers a dependency.
     *
     * @param dependency any dependency
     * @throws DependencyFailureException dependency failed to load
     */
    private void registerDependency(Dependency dependency) throws DependencyFailureException {
        Plugin plugin = this.plugin.getServer().getPluginManager().getPlugin(dependency.getName());

        // check if the dependency is not active
        if (plugin == null || !plugin.isEnabled()) {
            if (dependency.isRequired()) {
                throw new DependencyFailureException(dependency.getName());
            }
        } else {
            Logger.log(dependency.getName() + " dependency has been found. Registering now...");
        }

        dependency.getRegistry().apply(this, plugin);
    }

    public Economy getEcon() {
        return econ;
    }

    protected void setEconomy(Economy econ) {
        this.econ = econ;
    }

    public BuildBehavior getBuildbehavior() {
        return buildbehavior;
    }

    protected void setBuildBehavior(BuildBehavior buildBehavior) {
        this.buildbehavior = buildBehavior;
    }
}
