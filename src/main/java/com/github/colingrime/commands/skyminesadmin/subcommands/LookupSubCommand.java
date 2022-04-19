package com.github.colingrime.commands.skyminesadmin.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.utils.UUIDFinder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public record LookupSubCommand(SkyMines plugin) implements SubCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        UUID uuid = UUIDFinder.fromName(args[0]);
        if (uuid == null) {
            Messages.FAILURE_NO_PLAYER_FOUND.sendTo(sender, new Replacer("%player%", args[0]));
            return;
        }

        List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(uuid);
        if (skyMines.size() == 0) {
            Messages.FAILURE_NO_SKYMINES_FOUND.sendTo(sender, new Replacer("%player%", args[0]));
            return;
        }

        Messages.LOOKUP_SKYMINES_TOP_MESSAGE.sendTo(sender, new Replacer("%player%", args[0]));
        for (int i = 1; i <= skyMines.size(); i++) {
            Location loc = skyMines.get(i - 1).getHome();

            Replacer replacer = new Replacer("%id%", i);
            replacer.add("%world%", loc.getWorld().getName());
            replacer.add("%x%", loc.getBlockX());
            replacer.add("%y%", loc.getBlockY());
            replacer.add("%z%", loc.getBlockZ());

            Messages.LOOKUP_SKYMINES_REPEATING_MESSAGE.sendTo(sender, replacer);
        }
    }

    @Override
    public String getName() {
        return "lookup";
    }

    @Override
    public Messages getUsage() {
        return Messages.USAGE_SKYMINES_LOOKUP;
    }

    @Override
    public String getPermission() {
        return "skymines.admin.lookup";
    }

    @Override
    public int getArgumentsRequired() {
        return 1;
    }
}
