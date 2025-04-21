package me.colingrimes.skymines.command.skymine.reset;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.command.skymine.SkyMineCommand;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.SkyMine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SkyMineReset implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		SkyMine skyMine = SkyMineCommand.forceSkyMine(plugin, sender, args, Messages.USAGE_SKYMINE_RESET);
		if (skyMine == null) {
			return;
		}

		// Check if the mine is disabled.
		if (!skyMine.isEnabled()) {
			Messages.FAILURE_SKYMINE_INVALID_IDENTIFIER.replace("{id}", skyMine.getIdentifier()).send(sender);
			return;
		}

		// Check if SkyMine failed to reset.
		if (!skyMine.reset(false)) {
			Duration time = plugin.getCooldownManager().getSkyMineCooldown().getTimeLeft(skyMine);
			Messages.FAILURE_COOLDOWN_RESET.replace("{time}", Text.format(time)).send(sender);
			return;
		}

		Messages.SUCCESS_RESET.send(sender);
		if (Settings.OPTION_RESET_TELEPORT_HOME.get()) {
			skyMine.getStructure().getPlayers().forEach(p -> p.teleport(skyMine.getHome().toLocation()));
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
		properties.setUsage(Messages.USAGE_SKYMINE_RESET);
		properties.setPlayerRequired(true);
	}
}
