package me.colingrimes.skymines.command.skymine.pickup;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.command.skymine.SkyMineCommand;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.util.MineUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SkyMinePickup implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		SkyMine skyMine = SkyMineCommand.getSkyMine(plugin, sender, args, Messages.USAGE_SKYMINE_PICKUP);
		if (skyMine == null) {
			return;
		}

		// Attempt to pickup SkyMine.
		if (skyMine.pickup(sender.player())) {
			MineUtils.placeholders(Messages.SUCCESS_PICKUP, skyMine).send(sender);
		} else {
			Messages.FAILURE_TOKEN_NO_INVENTORY_SPACE.send(sender);
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		return SkyMineCommand.getSkyMineTabCompletion(plugin, sender, args);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINE_PICKUP);
		properties.setPlayerRequired(true);
	}
}
