package me.colingrimes.skymines.command.skymineadmin.lookup;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.util.misc.UUIDs;
import me.colingrimes.midnight.util.text.Markdown;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SkyMineAdminLookup implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<UUID> uuid = UUIDs.fromName(args.get(0));
		if (uuid.isEmpty()) {
			Messages.FAILURE_NO_PLAYER_FOUND.replace("{player}", args.get(0)).send(sender);
			return;
		}

		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(uuid.get());
		if (skyMines.isEmpty()) {
			Messages.FAILURE_NO_SKYMINES_FOUND.replace("{player}", args.get(0)).send(sender);
			return;
		}

		Messages.LOOKUP_SKYMINES_TOP_MESSAGE.replace("{player}", args.get(0)).send(sender);
		for (int i=1; i<=skyMines.size(); i++) {
			Location loc = skyMines.get(i - 1).getHome();
			String message = Messages.LIST_SKYMINES_REPEATING_MESSAGE
					.replace("{id}", i)
					.replace("{x}", loc.getBlockX())
					.replace("{y}", loc.getBlockY())
					.replace("{z}", loc.getBlockZ())
					.toText();
			Markdown.of("[" + message + "](/skymines_home_" + i + " &cGo Home!)").send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINES_LOOKUP);
		properties.setPermission("skymines.admin.lookup");
		properties.setArgumentsRequired(1);
	}
}
