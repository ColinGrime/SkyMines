package com.github.colingrime.commands.skyminesadmin;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.BaseCommand;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.commands.skyminesadmin.subcommands.SkyMinesGiveSubCommand;
import com.github.colingrime.commands.skyminesadmin.subcommands.SkyMinesLookupSubCommand;
import com.github.colingrime.commands.skyminesadmin.subcommands.SkyMinesReloadSubCommand;
import com.github.colingrime.commands.skyminesadmin.subcommands.SkyMinesRemoveSubCommand;
import com.github.colingrime.locale.Messages;

import java.util.List;

public class SkyMinesAdminBaseCommand extends BaseCommand {

	public SkyMinesAdminBaseCommand(SkyMines plugin) {
		super(plugin, "skyminesadmin");
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_COMMAND;
	}

	@Override
	public void registerSubCommands(List<SubCommand> subCommands, SkyMines plugin) {
		subCommands.add(new SkyMinesGiveSubCommand(plugin));
		subCommands.add(new SkyMinesLookupSubCommand(plugin));
		subCommands.add(new SkyMinesRemoveSubCommand(plugin));
		subCommands.add(new SkyMinesReloadSubCommand(plugin));
	}
}
