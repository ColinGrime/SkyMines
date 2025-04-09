package me.colingrimes.skymines.command.skymine;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class SkyMineCommand implements Command<SkyMines> {

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINES_COMMAND);
		properties.setAliases("skymines", "sm");
		properties.setPlayerRequired(true);
	}

	/**
	 * Forces a valid SkyMine ID to be entered.
	 * If one is not entered, it will force the player to run /skymines list.
	 *
	 * @return a valid skymine if one was entered, null otherwise
	 */
	@Nullable
	public static SkyMine forceSkyMine(@Nonnull SkyMines plugin, @Nonnull Sender sender, ArgumentList args, @Nonnull Message<?> usageMessage) {
		if (args.isEmpty()) {
			usageMessage.send(sender);
			return null;
		}

		Optional<SkyMine> skyMine = plugin.getSkyMineManager().getSkyMine(sender.player(), args.getFirst());
		if (skyMine.isEmpty()) {
			Messages.FAILURE_NO_SKYMINE.replace("{id}", args.getFirst()).send(sender);
			return null;
		}

		return skyMine.get();
	}
}
