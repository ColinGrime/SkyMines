package me.colingrimes.skymines.command.skymineadmin;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.midnight.util.misc.UUIDs;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SkyMineAdmin implements Command<SkyMines> {

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.ADMIN_USAGE_SKYMINE);
		properties.setPermission("skymines.admin");
		properties.setAliases("skyminesadmin", "smadmin", "sma");
	}

	/**
	 * Gets the {@link SkyMine} based on the first two arguments of the command.
	 * The first argument is the username of any player.
	 * The second argument can be either a skymine name or index.
	 * <p>
	 * Failure messages will be sent to the sender if either argument is invalid.
	 *
	 * @param plugin the plugin
	 * @param sender the sender of the command
	 * @param args the argument list
	 * @return a valid skymine if one was entered, null otherwise
	 */
	@Nullable
	public static SkyMine getSkyMine(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<UUID> uuid = UUIDs.fromName(args.getFirst());
		if (uuid.isEmpty()) {
			Messages.ADMIN_FAILURE_MISC_NO_PLAYER_FOUND.replace("{player}", args.getFirst()).send(sender);
			return null;
		}

		Optional<SkyMine> skyMine = plugin.getSkyMineManager().getSkyMine(uuid.get(), args.get(1));
		if (skyMine.isPresent()) {
			return skyMine.get();
		}

		// Send a failure message depending on if they attempted to send an ID or name.
		if (Types.isInteger(args.get(1))) {
			Messages.ADMIN_FAILURE_SKYMINE_INVALID_INDEX
					.replace("{player}", args.get(0))
					.replace("{id}", args.get(1))
					.send(sender);
		} else {
			Messages.ADMIN_FAILURE_SKYMINE_INVALID_NAME
					.replace("{player}", args.get(0))
					.replace("{name}", args.get(1))
					.send(sender);
		}

		return null;
	}

	/**
	 * Gets the skymine tab completion list based on the UUID of the first argument.
	 *
	 * @param plugin the plugin
	 * @param args the argument list
	 * @return the skymine tab completion list
	 */
	@Nullable
	public static List<String> getSkyMineTabCompletion(@Nonnull SkyMines plugin, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Players.filter(p -> p.getName().startsWith(args.getFirst())).map(Player::getName).toList();
		} else if (args.size() != 2) {
			return null;
		}

		Optional<UUID> uuid = UUIDs.fromName(args.getFirst());
		if (uuid.isEmpty()) {
			return null;
		}

		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(uuid.get());
		if (skyMines.isEmpty()) {
			return null;
		}

		List<String> tabCompletion = new ArrayList<>();
		for (int i=0; i<skyMines.size(); i++) {
			SkyMine skyMine = skyMines.get(i);
			if (skyMine.getName() != null) {
				tabCompletion.add(skyMine.getName());
			} else {
				tabCompletion.add(String.valueOf(i + 1));
			}
		}

		return tabCompletion;
	}
}
