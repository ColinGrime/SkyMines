package com.github.scilldev.commands.skymines.subcommands;

import com.github.scilldev.Messages;
import com.github.scilldev.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class SkyMinesPanelSubCommand implements SubCommand {

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {

	}

	@Override
	public String getName() {
		return "panel";
	}

	@Override
	public Messages getUsage() {
		return null;
	}
}
