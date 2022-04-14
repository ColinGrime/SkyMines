package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.skymines.SkyMinesSubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.command.CommandSender;

public class SkyMinesReloadSubCommand extends SkyMinesSubCommand {

	private final SkyMines plugin;

	public SkyMinesReloadSubCommand(SkyMines plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String[] args, SkyMine skyMine) {
		plugin.reload();
		Messages.SUCCESS_RELOADED.sendTo(sender);
	}

	@Override
	public boolean requireSkyMine() {
		return false;
	}

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public Messages getUsage() {
		return Messages.SUCCESS_RELOADED;
	}

	@Override
	public String getPermission() {
		return "skymines.reload";
	}
}
