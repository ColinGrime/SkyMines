package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.locale.Messages;
import org.bukkit.command.CommandSender;

public class SkyMinesReloadSubCommand implements SubCommand {

	private final SkyMines plugin;

	public SkyMinesReloadSubCommand(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		plugin.reload();
		Messages.RELOADED.sendTo(sender);
	}

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public Messages getUsage() {
		return Messages.RELOADED;
	}

	@Override
	public String getPermission() {
		return "skymines.reload";
	}
}
