package com.github.scilldev.commands.skymines.subcommands;

import com.github.scilldev.SkyMines;
import com.github.scilldev.locale.Messages;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.panel.UpgradePanel;
import com.github.scilldev.skymines.SkyMine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SkyMinesPanelSubCommand implements SubCommand {

	private final SkyMines plugin;

	public SkyMinesPanelSubCommand(SkyMines plugin) {
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
			new UpgradePanel(plugin, player, skyMine.get()).openInventory(player);
		} else {
			player.sendMessage("You don't have a skymine with the ID: " + args[0]);
		}
	}

	@Override
	public String getName() {
		return "panel";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_PANEL;
	}
}
