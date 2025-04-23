package me.colingrimes.skymines.command.skymineadmin.pickup;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.misc.UUIDs;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.util.MineUtils;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class SkyMineAdminPickup implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<UUID> uuid = UUIDs.fromName(args.getFirst());
		if (uuid.isEmpty()) {
			Messages.ADMIN_FAILURE_MISC_NO_PLAYER_FOUND.replace("{player}", args.getFirst()).send(sender);
			return;
		}

		Optional<SkyMine> skyMine = plugin.getSkyMineManager().getSkyMine(uuid.get(), args.get(1));
		if (skyMine.isEmpty()) {
			Messages.ADMIN_FAILURE_SKYMINE_INVALID_INDEX.replace("{player}", args.get(0)).replace("{id}", args.get(1)).send(sender);
			return;
		}

		// Attempt to pickup SkyMine.
		Message<?> success = MineUtils.placeholders(Messages.ADMIN_SUCCESS_PICKUP, skyMine.get());
		if (skyMine.get().pickup(sender.player())) {
			success.send(sender);
		} else {
			Messages.FAILURE_TOKEN_NO_INVENTORY_SPACE.send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.ADMIN_USAGE_SKYMINE_PICKUP);
		properties.setPermission("skymines.admin.pickup");
		properties.setArgumentsRequired(2);
	}
}
