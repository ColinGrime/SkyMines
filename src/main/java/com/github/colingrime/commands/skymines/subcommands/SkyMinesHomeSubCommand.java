package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.locale.Messages;
import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SkyMinesHomeSubCommand implements SubCommand {

	private final SkyMines plugin;

	public SkyMinesHomeSubCommand(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		Player player = (Player) sender;
		if (args.length == 0) {
			player.performCommand("skymines list");
			return;
		}

		Optional<SkyMine> skyMine = plugin.getSkyMineManager().getSkyMine(player, args[0]);
		if (skyMine.isPresent()) {
			player.teleport(skyMine.get().getHome());
		} else {
			player.sendMessage("You don't have a skymine with the ID: " + args[0]);
		}
	}

	@Override
	public ArrayList<String> onTabComplete(CommandSender sender, String subCommand, String[] args) {
		if (!(sender instanceof Player)) {
			return null;
		}

		Player player = (Player) sender;
		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(player);

		if (skyMines.size() == 0) {
			return null;
		}

		return IntStream.rangeClosed(1, skyMines.size()).mapToObj(Integer::toString).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public String getName() {
		return "home";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_HOME;
	}

	@Override
	public String getPermission() {
		return "skymines.home";
	}

	@Override
	public boolean requirePlayer() {
		return true;
	}
}
