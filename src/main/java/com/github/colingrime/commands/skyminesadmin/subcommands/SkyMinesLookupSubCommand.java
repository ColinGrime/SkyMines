package com.github.colingrime.commands.skyminesadmin.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.utils.UUIDFinder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public class SkyMinesLookupSubCommand implements SubCommand {

	private final SkyMines plugin;

	public SkyMinesLookupSubCommand(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		UUID uuid = UUIDFinder.fromName(args[0]);
		if (uuid == null) {
			sender.sendMessage("nope from lookup");
			return;
		}

		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(uuid);
		if (skyMines.size() == 0) {
			Messages.FAILURE_NO_SKYMINES.sendTo(sender);
			return;
		}

		Messages.LIST_SKYMINES_TOP_MESSAGE.sendTo(sender);
		for (int i=1; i<=skyMines.size(); i++) {
			Location loc = skyMines.get(i - 1).getHome();

			Replacer replacer = new Replacer("%id%", i);
			replacer.add("%world%", loc.getWorld().getName());
			replacer.add("%x%", loc.getBlockX());
			replacer.add("%y%", loc.getBlockY());
			replacer.add("%z%", loc.getBlockZ());

			Messages.LIST_SKYMINES_REPEATING_MESSAGE.sendTo(sender, replacer);
		}
	}

	@Override
	public String getName() {
		return "lookup";
	}

	@Override
	public Messages getUsage() {
		return null;
	}
}
