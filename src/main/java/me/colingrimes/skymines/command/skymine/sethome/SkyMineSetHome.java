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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SkyMineSetHome implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		SkyMine skyMine = SkyMineCommand.forceSkyMine(plugin, sender, args, Messages.USAGE_SKYMINE_SETHOME);
		if (skyMine == null) {
			return;
		}

		if (skyMine.setHome(Pose.of(sender.location()))) {
			Messages.SUCCESS_SETHOME.send(sender);
		} else {
			Messages.FAILURE_TOO_FAR_AWAY.send(sender);
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(sender.player());
		return skyMines.isEmpty() ? null : IntStream.rangeClosed(1, skyMines.size()).mapToObj(Integer::toString).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINE_SETHOME);
		properties.setPermission("skymines.sethome");
		properties.setPlayerRequired(true);
	}
}
