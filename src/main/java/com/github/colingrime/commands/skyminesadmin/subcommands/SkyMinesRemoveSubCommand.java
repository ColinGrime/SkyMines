package com.github.colingrime.commands.skyminesadmin.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.utils.UUIDFinder;
import org.bukkit.command.CommandSender;

import java.util.Optional;
import java.util.UUID;

public class SkyMinesRemoveSubCommand implements SubCommand {

	private final SkyMines plugin;

	public SkyMinesRemoveSubCommand(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		if (args.length < 2) {
			sender.sendMessage("not enough args");
			return;
		}

		UUID uuid = UUIDFinder.fromName(args[0]);
		if (uuid == null) {
			sender.sendMessage("nope from remove");
			return;
		}

		Optional<SkyMine> skyMine = plugin.getSkyMineManager().getSkyMine(uuid, args[1]);
		if (skyMine.isPresent()) {
			skyMine.get().remove();
			sender.sendMessage("Successfully removed!");
		} else {
			sender.sendMessage("nope, not found");
		}
	}

	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public Messages getUsage() {
		return null;
	}
}
