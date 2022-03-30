package com.github.scilldev.commands.skymines.subcommands;

import com.github.scilldev.SkyMines;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.locale.Messages;
import org.bukkit.command.CommandSender;

public class SkyMinesReloadSubCommand implements SubCommand {

	private final SkyMines plugin;

	public SkyMinesReloadSubCommand(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		plugin.reload();
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
