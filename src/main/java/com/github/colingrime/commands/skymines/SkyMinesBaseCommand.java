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
		subCommands.add(new SkyMinesListSubCommand(plugin));
		subCommands.add(new SkyMinesPanelSubCommand(plugin));
		subCommands.add(new SkyMinesHomeSubCommand(plugin));
		subCommands.add(new SkyMinesSetHomeSubCommand(plugin));
		subCommands.add(new SkyMinesResetSubCommand(plugin));
		subCommands.add(new SkyMinesUpgradeSubCommand(plugin));
		subCommands.add(new SkyMinesPickupSubCommand(plugin));
	}
}
