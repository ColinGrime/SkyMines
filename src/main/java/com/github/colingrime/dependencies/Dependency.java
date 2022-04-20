package com.github.colingrime.dependencies;

import com.github.colingrime.skymines.structure.behavior.BuildBehavior;
import com.github.colingrime.skymines.structure.behavior.DefaultBuildBehavior;
import com.github.colingrime.skymines.structure.behavior.FastBuildBehavior;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public enum Dependency {

    VAULT("Vault", true, (manager, plugin) -> {
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            throw new DependencyFailureException(plugin.getName());
        }
        manager.setEconomy(rsp.getProvider());
    }),

    FAWE("FastAsyncWorldEdit", false, (manager, plugin) -> {
        BuildBehavior behavior = plugin == null ? new DefaultBuildBehavior() : new FastBuildBehavior();
        manager.setBuildBehavior(behavior);
    });

    private final String name;
    private final boolean isRequired;
    private final Registry<DependencyManager, Plugin> register;

    Dependency(String name, boolean isRequired, Registry<DependencyManager, Plugin> register) {
        this.name = name;
        this.isRequired = isRequired;
        this.register = register;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public Registry<DependencyManager, Plugin> getRegistry() {
        return register;
    }

    @FunctionalInterface
    public interface Registry<A, B> {
        void apply(A a, B b) throws DependencyFailureException;
    }
}
