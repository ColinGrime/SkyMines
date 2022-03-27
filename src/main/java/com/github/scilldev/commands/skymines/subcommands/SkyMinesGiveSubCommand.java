package com.github.scilldev.commands.skymines.subcommands;

import com.github.scilldev.locale.Messages;
import com.github.scilldev.SkyMines;
import com.github.scilldev.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkyMinesGiveSubCommand implements SubCommand {

	private final SkyMines plugin;

	public SkyMinesGiveSubCommand(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		Player receiver = Bukkit.getPlayer(args[0]);
		if (receiver == null) {
			return;
		}

		int amount = 1;
		if (args.length > 1 && args[1].matches("\\d+")) {
			amount = Integer.parseInt(args[1]);
		}

		ItemStack item = plugin.getSkyMineManager().getToken().getToken();
		for (int i=0; i<amount; i++) {
			receiver.getInventory().addItem(item);
		}

		receiver.sendMessage("HERE'S THE " + amount + " OF TOKENS");
	}

	@Override
	public String getName() {
		return "give";
	}

	@Override
	public Messages getUsage() {
		return null;
	}

	@Override
	public String getPermission() {
		return "skymines.give";
	}

	@Override
	public boolean requireArguments() {
		return true;
	}
}
