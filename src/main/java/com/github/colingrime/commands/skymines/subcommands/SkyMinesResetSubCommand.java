package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.skymines.SkyMinesSubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.command.CommandSender;

public class SkyMinesResetSubCommand extends SkyMinesSubCommand {

	public SkyMinesResetSubCommand(SkyMines plugin) {
		super(plugin);
	}

	@Override
	public void onCommand(CommandSender sender, String[] args, SkyMine skyMine) {
		skyMine.reset();
	}

	@Override
	public boolean requireSkyMine() {
		return true;
	}

	@Override
	public String getName() {
		return "reset";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_RESET;
	}

	@Override
	public String getPermission() {
		return "skymines.reset";
	}
}
