package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.skymines.SkyMinesSubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PickupSubCommand extends SkyMinesSubCommand {

	public PickupSubCommand(SkyMines plugin) {
		super(plugin);
	}

	@Override
	public void onCommand(CommandSender sender, String[] args, SkyMine skyMine) {
		if (skyMine.pickup((Player) sender)) {
			Messages.SUCCESS_PICKUP.sendTo(sender);
		} else {
			Messages.FAILURE_NO_INVENTORY_SPACE.sendTo(sender);
		}
	}

	@Override
	public boolean requireSkyMine() {
		return true;
	}

	@Override
	public String getName() {
		return "pickup";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_PICKUP;
	}
}
