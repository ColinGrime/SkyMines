package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SkyMinesListSubCommand implements SubCommand {

	private final SkyMines plugin;

	public SkyMinesListSubCommand(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		Player player = (Player) sender;
		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(player);

		if (skyMines.size() == 0) {
			player.sendMessage("You have no mines!");
			return;
		}

		for (int i=1; i<=skyMines.size(); i++) {
			String message = Messages.SKYMINES_LIST.toString().replaceAll("%id%", String.valueOf(i));
			player.spigot().sendMessage(Utils.command(message, "/skymines home " + i));
		}
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_LIST;
	}

	@Override
	public boolean requirePlayer() {
		return true;
	}
}
