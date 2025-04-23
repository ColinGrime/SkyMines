package me.colingrimes.skymines.command.skymine;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SkyMineCommand implements Command<SkyMines> {

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINE);
		properties.setAliases("skymines", "sm");
		properties.setPlayerRequired(true);
	}

	/**
	 * Gets the {@link SkyMine} based on the first argument of the command.
	 * The argument can be either a skymine name or index.
	 * <p>
	 * If one is not entered, it will send the specified usage message.
	 *
	 * @param plugin the plugin
	 * @param sender the sender of the command
	 * @param args the argument list
	 * @param usageMessage the usage message to send if no skymine is found
	 * @return a valid skymine if one was entered, null otherwise
	 */
	@Nullable
	public static SkyMine getSkyMine(@Nonnull SkyMines plugin, @Nonnull Sender sender, ArgumentList args, @Nonnull Message<?> usageMessage) {
		if (args.isEmpty()) {
			usageMessage.send(sender);
			return null;
		}

		Optional<SkyMine> skyMine = plugin.getSkyMineManager().getSkyMine(sender.player(), args.getFirst());
		if (skyMine.isEmpty()) {
			if (Types.isInteger(args.getFirst())) {
				Messages.FAILURE_SKYMINE_INVALID_INDEX.replace("{id}", args.getFirst()).send(sender);
			} else {
				Messages.FAILURE_SKYMINE_INVALID_NAME.replace("{name}", args.getFirst()).send(sender);
			}
			return null;
		}

		return skyMine.get();
	}

	/**
	 * Gets the skymine tab completion list for the specified sender.
	 *
	 * @param plugin the plugin
	 * @param sender the sender
	 * @param args the argument list
	 * @return the skymine tab completion list
	 */
	@Nullable
	public static List<String> getSkyMineTabCompletion(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() != 1) {
			return null;
		}

		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(sender.player());
		if (skyMines.isEmpty()) {
			return null;
		}

		List<String> tabCompletion = new ArrayList<>();
		for (int i = 0; i < skyMines.size(); i++) {
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
