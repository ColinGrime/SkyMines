package me.colingrimes.skymines.command.skymineadmin.remove;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.util.misc.UUIDs;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class SkyMineAdminRemove implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<UUID> uuid = UUIDs.fromName(args.get(0));
		if (uuid.isEmpty()) {
			Messages.FAILURE_NO_PLAYER_FOUND.replace("{player}", args.get(0)).send(sender);
			return;
		}

		Optional<SkyMine> skyMine = plugin.getSkyMineManager().getSkyMine(uuid.get(), args.get(1));
		if (skyMine.isPresent()) {
			skyMine.get().remove();
			Messages.SUCCESS_REMOVE.replace("{player}", args.get(0)).send(sender);
		} else {
			Messages.FAILURE_NO_SKYMINE_FOUND
					.replace("{player}", args.get(0))
					.replace("{id}", args.get(1))
					.send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINES_REMOVE);
		properties.setPermission("skymines.admin.remove");
		properties.setArgumentsRequired(2);
	}
}
