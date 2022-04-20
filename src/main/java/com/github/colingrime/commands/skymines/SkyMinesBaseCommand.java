package com.github.colingrime.commands.skymines;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.BaseCommand;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.commands.skymines.subcommands.*;
import com.github.colingrime.locale.Messages;

import java.util.List;

public class SkyMinesBaseCommand extends BaseCommand {

    public SkyMinesBaseCommand(SkyMines plugin) {
        super(plugin, "skymines");
    }

    @Override
    public Messages getUsage() {
        return Messages.USAGE_SKYMINES_COMMAND;
    }

    @Override
    public void registerSubCommands(List<SubCommand> subCommands, SkyMines plugin) {
        subCommands.add(new ListSubCommand(plugin));
        subCommands.add(new PanelSubCommand(plugin));
        subCommands.add(new HomeSubCommand(plugin));
        subCommands.add(new SetHomeSubCommand(plugin));
        subCommands.add(new ResetSubCommand(plugin));
        subCommands.add(new UpgradeSubCommand(plugin));
        subCommands.add(new PickupSubCommand(plugin));
    }
}
