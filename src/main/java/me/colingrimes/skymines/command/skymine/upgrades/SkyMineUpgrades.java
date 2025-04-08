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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SkyMineUpgrades implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		SkyMine skyMine = SkyMineCommand.forceSkyMine(plugin, sender, args, Messages.USAGE_SKYMINES_UPGRADES);
		if (skyMine != null) {
			new UpgradeMenu(sender.player(), skyMine).open();
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
		properties.setUsage(Messages.USAGE_SKYMINES_UPGRADES);
		properties.setPlayerRequired(true);
	}
}
