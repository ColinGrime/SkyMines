package com.github.scilldev.commands.skymines.subcommands;

import com.github.scilldev.SkyMines;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.Messages;
import com.github.scilldev.skymines.SkyMine;
import com.github.scilldev.utils.Utils;
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
		return null;
	}

	@Override
	public boolean requirePlayer() {
		return true;
	}
}
