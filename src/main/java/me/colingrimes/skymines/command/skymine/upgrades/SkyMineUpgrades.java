package me.colingrimes.skymines.command.skymine.upgrades;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.command.skymine.SkyMineCommand;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.menu.UpgradeMenu;
import me.colingrimes.skymines.skymine.SkyMine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SkyMineUpgrades implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		SkyMine skyMine = SkyMineCommand.getSkyMine(plugin, sender, args);
		if (skyMine == null) {
			return;
		}

		// Check if the mine is disabled.
		if (!skyMine.isEnabled()) {
			Messages.FAILURE_SKYMINE_INVALID_IDENTIFIER.replace("{id}", skyMine.getIdentifier()).send(sender);
			return;
		}

		new UpgradeMenu(sender.player(), skyMine).open();
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		return SkyMineCommand.getSkyMineTabCompletion(plugin, sender, args);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINE_UPGRADES);
		properties.setArgumentsRequired(1);
		properties.setPlayerRequired(true);
	}
}
