package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.skymines.SkyMinesSubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.utils.Utils;
import org.bukkit.command.CommandSender;

public class SkyMinesResetSubCommand extends SkyMinesSubCommand {

	public SkyMinesResetSubCommand(SkyMines plugin) {
		super(plugin);
	}

	@Override
	public void onCommand(CommandSender sender, String[] args, SkyMine skyMine) {
		if (skyMine.reset()) {
			Messages.SUCCESS_RESET.sendTo(sender);
		} else {
			Replacer replacer = new Replacer("%time%", Utils.formatTime(skyMine.getCooldownTime()));
			Messages.FAILURE_INCOMPLETE_COOLDOWN.sendTo(sender, replacer);
		}
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
