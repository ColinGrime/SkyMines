package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.skymines.SkyMinesSubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkyMinesPickupSubCommand extends SkyMinesSubCommand {

	public SkyMinesPickupSubCommand(SkyMines plugin) {
		super(plugin);
	}

	@Override
	public void onCommand(CommandSender sender, String[] args, SkyMine skyMine) {
		skyMine.pickup((Player) sender);
	}

	@Override
	public boolean requireSkyMine() {
		return true;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Messages getUsage() {
		return null;
	}
}
