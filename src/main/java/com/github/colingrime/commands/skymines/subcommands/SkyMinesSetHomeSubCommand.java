package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.skymines.SkyMinesSubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkyMinesSetHomeSubCommand extends SkyMinesSubCommand {

	public SkyMinesSetHomeSubCommand(SkyMines plugin) {
		super(plugin);
	}

	@Override
	public void onCommand(CommandSender sender, String[] args, SkyMine skyMine) {
		skyMine.setHome(((Player) sender).getLocation());
		Messages.SUCCESS_SETHOME.sendTo(sender);
	}

	@Override
	public boolean requireSkyMine() {
		return true;
	}

	@Override
	public String getName() {
		return "sethome";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_HOME;
	}

	@Override
	public String getPermission() {
		return "skymines.sethome";
	}
}
