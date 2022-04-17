package com.github.colingrime.commands.skyminesadmin.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.utils.UUIDFinder;
import org.bukkit.command.CommandSender;

import java.util.Optional;
import java.util.UUID;

public class RemoveSubCommand implements SubCommand {

	private final SkyMines plugin;

	public RemoveSubCommand(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		UUID uuid = UUIDFinder.fromName(args[0]);
		if (uuid == null) {
			Messages.FAILURE_NO_PLAYER_FOUND.sendTo(sender);
			return;
		}

		Optional<SkyMine> skyMine = plugin.getSkyMineManager().getSkyMine(uuid, args[1]);
		if (skyMine.isPresent()) {
			skyMine.get().remove();
			Messages.SUCCESS_REMOVE.sendTo(sender, new Replacer("%player%", args[0]));
		} else {
			Messages.FAILURE_NO_SKYMINE_FOUND.sendTo(sender);
		}
	}

	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_REMOVE;
	}

	@Override
	public String getPermission() {
		return "skymines.admin.remove";
	}

	@Override
	public int getArgumentsRequired() {
		return 2;
	}
}
