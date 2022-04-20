package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.skymines.SkyMinesSubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ListSubCommand extends SkyMinesSubCommand {

    private final SkyMines plugin;

    public ListSubCommand(SkyMines plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args, SkyMine skyMine) {
        Player player = (Player) sender;
        List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(player);

        if (skyMines.size() == 0) {
            Messages.FAILURE_NO_SKYMINES.sendTo(player);
            return;
        }

        Messages.LIST_SKYMINES_TOP_MESSAGE.sendTo(player);
        for (int i = 1; i <= skyMines.size(); i++) {
            Location loc = skyMines.get(i - 1).getHome();

            Replacer replacer = new Replacer("%id%", i);
            replacer.add("%x%", loc.getBlockX());
            replacer.add("%y%", loc.getBlockY());
            replacer.add("%z%", loc.getBlockZ());

            String message = replacer.replace(Messages.LIST_SKYMINES_REPEATING_MESSAGE.toString());
            player.spigot().sendMessage(Utils.command(message, "/skymines home " + i));
        }
    }

    @Override
    public boolean requireSkyMine() {
        return false;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public Messages getUsage() {
        return Messages.USAGE_SKYMINES_LIST;
    }

    @Override
    public boolean requirePlayer() {
        return true;
    }
}
