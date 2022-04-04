package com.github.colingrime.commands.skymines;

import com.github.colingrime.commands.BaseCommand;
import com.github.colingrime.locale.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyMinesBaseCommand extends BaseCommand {

	public SkyMinesBaseCommand(JavaPlugin plugin) {
		super(plugin, "skymines");
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_COMMAND;
	}
}
