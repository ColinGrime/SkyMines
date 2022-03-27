package com.github.scilldev.commands.skymines;

import com.github.scilldev.commands.BaseCommand;
import com.github.scilldev.locale.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyMinesBaseCommand extends BaseCommand {

	public SkyMinesBaseCommand(JavaPlugin plugin) {
		super(plugin, "skymines");
	}

	@Override
	public Messages getUsage() {
		return null;
	}
}
