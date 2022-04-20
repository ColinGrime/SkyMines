package com.github.colingrime.skymines.manager;

import com.github.colingrime.SkyMines;
import com.github.colingrime.cache.Cooldown;
import com.github.colingrime.cache.CooldownCache;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.tasks.CooldownTask;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CooldownManager {

    private final CooldownTask cooldownTask = new CooldownTask();
    private final Map<Player, Cooldown> playerCooldowns = new HashMap<>();

    public CooldownManager(SkyMines plugin) {
        cooldownTask.runTaskTimerAsynchronously(plugin, 0, 20L);
    }

    public void addCooldown(Cooldown cooldown) {
        cooldownTask.getCooldownList().add(cooldown);
    }

    public Optional<Cooldown> getPlayerCooldown(Player player) {
        return Optional.ofNullable(playerCooldowns.get(player));
    }

    public void addPlayerCooldown(Player player, int seconds, Consumer<Player> completionAction, Messages denyMessage) {
        Cooldown cooldown = new CooldownCache<>(player, seconds, TimeUnit.SECONDS, completionAction, denyMessage);
        playerCooldowns.put(player, cooldown);
        addCooldown(cooldown);
    }
}
