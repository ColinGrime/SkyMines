package me.colingrimes.skymines.command.skymine.name;

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

public class SkyMineName implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		SkyMine skyMine = SkyMineCommand.getSkyMine(plugin, sender, args, Messages.USAGE_SKYMINE_NAME);
		if (skyMine != null) {
			skyMine.setName(args.get(1));
			MineUtils.placeholders(Messages.SUCCESS_NAME, skyMine).send(sender);
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		return SkyMineCommand.getSkyMineTabCompletion(plugin, sender, args);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINE_NAME);
		properties.setPermission("skymines.name");
		properties.setArgumentsRequired(2);
		properties.setPlayerRequired(true);
	}
}
