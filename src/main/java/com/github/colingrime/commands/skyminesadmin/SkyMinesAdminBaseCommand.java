package com.github.colingrime.commands.skyminesadmin;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.BaseCommand;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.commands.skyminesadmin.subcommands.*;
import com.github.colingrime.locale.Messages;

import java.util.List;

public class SkyMinesAdminBaseCommand extends BaseCommand {

    public SkyMinesAdminBaseCommand(SkyMines plugin) {
        super(plugin, "skyminesadmin");
    }

    @Override
    public Messages getUsage() {
        return Messages.USAGE_SKYMINES_ADMIN_COMMAND;
    }

    @Override
    public void registerSubCommands(List<SubCommand> subCommands, SkyMines plugin) {
        subCommands.add(new GiveSubCommand(plugin));
        subCommands.add(new LookupSubCommand(plugin));
        subCommands.add(new PickupSubCommand(plugin));
        subCommands.add(new RemoveSubCommand(plugin));
        subCommands.add(new ReloadSubCommand(plugin));
    }
}
