package me.colingrimes.skymines.command.skymine.sethome;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.geometry.Pose;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.command.skymine.SkyMineCommand;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.util.MineUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SkyMineSetHome implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		SkyMine skyMine = SkyMineCommand.getSkyMine(plugin, sender, args);
		if (skyMine == null) {
			return;
		}

		if (skyMine.setHome(Pose.of(sender.location()))) {
			MineUtils.placeholders(Messages.SUCCESS_SETHOME, skyMine).send(sender);
		} else {
			MineUtils.placeholders(Messages.FAILURE_SKYMINE_SETHOME_DISTANCE, skyMine).send(sender);
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		return SkyMineCommand.getSkyMineTabCompletion(plugin, sender, args);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINE_SETHOME);
		properties.setPermission("skymines.sethome");
		properties.setArgumentsRequired(1);
		properties.setPlayerRequired(true);
	}
}
