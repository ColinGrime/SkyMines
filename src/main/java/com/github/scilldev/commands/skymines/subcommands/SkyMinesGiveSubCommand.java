package com.github.scilldev.commands.skymines.subcommands;

import com.github.scilldev.locale.Messages;
import com.github.scilldev.SkyMines;
import com.github.scilldev.commands.SubCommand;
import com.github.scilldev.skymines.structure.MineSize;
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
			getUsage().sendTo(sender);
			return;
		}

		MineSize size = new MineSize(10, 10, 10);
		if (args.length >= 3) {
			String sizeString = args[2];
			String[] sizeStringArray = sizeString.split("x");
			if (!isSizeValid(sizeStringArray)) {
				getUsage().sendTo(sender);
				return;
			}

			int length = Integer.parseInt(sizeStringArray[0]);
			int height = Integer.parseInt(sizeStringArray[1]);
			int width = Integer.parseInt(sizeStringArray[2]);
			if (length <= 1 || height <= 1 || width <= 1) {
				sender.sendMessage("WAY TOO SMALL");
				return;
			}

			size = new MineSize(length, height, width);
		}

		int amount = 1;
		if (args.length > 1 && isInt(args[1])) {
			amount = Integer.parseInt(args[1]);
		}

		ItemStack item = plugin.getSkyMineManager().getToken().getToken(size);
		for (int i=0; i<amount; i++) {
			receiver.getInventory().addItem(item);
		}

		receiver.sendMessage("HERE'S THE " + amount + " OF TOKENS");
	}

	private boolean isSizeValid(String[] sizeStringArray) {
		if (sizeStringArray.length != 3) {
			return false;
		}
		return isInt(sizeStringArray[0]) && isInt(sizeStringArray[1]) && isInt(sizeStringArray[2]);
	}

	private boolean isInt(String string) {
		return string.matches("\\d+");
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
