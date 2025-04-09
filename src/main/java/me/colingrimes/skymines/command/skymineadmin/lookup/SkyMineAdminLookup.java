package me.colingrimes.skymines.command.skymineadmin.lookup;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.util.misc.UUIDs;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SkyMineAdminLookup implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<UUID> uuid = UUIDs.fromName(args.getFirst());
		if (uuid.isEmpty()) {
			Messages.FAILURE_NO_PLAYER_FOUND.replace("{player}", args.getFirst()).send(sender);
			return;
		}

		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(uuid.get());
		if (skyMines.isEmpty()) {
			Messages.FAILURE_NO_SKYMINES_FOUND.replace("{player}", args.getFirst()).send(sender);
			return;
		}

		if (!Messages.LOOKUP_SKYMINES_TOP_MESSAGE.toText().isEmpty()) {
			Messages.LOOKUP_SKYMINES_TOP_MESSAGE.replace("{player}", args.getFirst()).send(sender);
		}
		for (int i=1; i<=skyMines.size(); i++) {
			Location location = skyMines.get(i - 1).getHome();
			Messages.LOOKUP_SKYMINES_REPEATING_MESSAGE
					.replace("{id}", i)
					.replace("{world}", Objects.requireNonNull(location.getWorld()).getName())
					.replace("{x}", location.getBlockX())
					.replace("{y}", location.getBlockY())
					.replace("{z}", location.getBlockZ())
					.send(sender);
		}
		if (!Messages.LOOKUP_SKYMINES_BOTTOM_MESSAGE.toText().isEmpty()) {
			Messages.LOOKUP_SKYMINES_BOTTOM_MESSAGE.replace("{player}", args.getFirst()).send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINES_LOOKUP);
		properties.setPermission("skymines.admin.lookup");
		properties.setArgumentsRequired(1);
	}
}
