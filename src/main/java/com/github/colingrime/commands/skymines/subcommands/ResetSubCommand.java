package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.skymines.SkyMinesSubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetSubCommand extends SkyMinesSubCommand {

	private final SkyMines plugin;

	public ResetSubCommand(SkyMines plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String[] args, SkyMine skyMine) {
		if (!skyMine.reset(false)) {
			Replacer replacer = new Replacer("%time%", Utils.formatTime(skyMine.getCooldown().getCooldownLeft()));
			Messages.FAILURE_ON_RESET_COOLDOWN.sendTo(sender, replacer);
			return;
		}

		Messages.SUCCESS_RESET.sendTo(sender);
		if (plugin.getSettings().shouldTeleportHomeOnReset()) {
			((Player) sender).teleport(skyMine.getHome());
		}
	}

	@Override
	public boolean requireSkyMine() {
		return true;
	}

	@Override
	public String getName() {
		return "reset";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_RESET;
	}
}
