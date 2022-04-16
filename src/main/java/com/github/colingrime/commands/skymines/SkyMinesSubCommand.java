package com.github.colingrime.commands.skymines;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class SkyMinesSubCommand implements SubCommand {

	private final SkyMines plugin;

	public SkyMinesSubCommand(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String[] args) {
		// skymine isn't required, continue as normal
		if (!requireSkyMine()) {
			onCommand(sender, args, null);
		}

		// only players can own skymines
		else if (!(sender instanceof Player)) {
			Messages.FAILURE_INVALID_SENDER.sendTo(sender);
		}

		// prompt list of mines if player didn't specify id
		else if (args.length == 0) {
			((Player) sender).performCommand("skymines list");
		}

		// check for skymine id
		else {
			Player player = (Player) sender;
			Optional<SkyMine> skyMine = plugin.getSkyMineManager().getSkyMine(player, args[0]);

			if (skyMine.isPresent()) {
				onCommand(player, args, skyMine.get());
			} else {
				Messages.FAILURE_NO_SKYMINE.sendTo(player, new Replacer("%id%", args[0]));
			}
		}
	}

	@Override
	public ArrayList<String> onTabComplete(CommandSender sender, String[] args) {
		if (!requireSkyMine() || !(sender instanceof Player player)) {
			return null;
		}

		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(player);
		if (skyMines.size() == 0) {
			return null;
		}

		return IntStream.rangeClosed(1, skyMines.size()).mapToObj(Integer::toString).collect(Collectors.toCollection(ArrayList::new));
	}

	public abstract void onCommand(CommandSender sender, String[] args, SkyMine skyMine);

	public abstract boolean requireSkyMine();
}
