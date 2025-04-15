package me.colingrimes.skymines.command.skymineadmin.reload;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;

import javax.annotation.Nonnull;

public class SkyMineAdminReload implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		plugin.getConfigurationManager().reload();
		Messages.ADMIN_SUCCESS_RELOADED.send(sender);

		// Show the player the list of loaded mines.
		// If it's the console, they already have more info, so it's not necessary.
		if (sender.isPlayer()) {
			Players.command(sender.player(), "/sma list");
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPermission("skymines.admin.reload");
	}
}
