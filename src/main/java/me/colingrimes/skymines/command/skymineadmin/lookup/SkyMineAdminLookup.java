package me.colingrimes.skymines.command.skymineadmin.lookup;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.midnight.util.misc.UUIDs;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.util.MineUtils;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SkyMineAdminLookup implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<UUID> uuid = UUIDs.fromName(args.getFirst());
		if (uuid.isEmpty()) {
			Messages.ADMIN_FAILURE_MISC_NO_PLAYER_FOUND.replace("{player}", args.getFirst()).send(sender);
			return;
		}

		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(uuid.get());
		if (skyMines.isEmpty()) {
			Messages.ADMIN_FAILURE_SKYMINE_NONE_OWNED.replace("{player}", args.getFirst()).send(sender);
			return;
		}

		if (!Messages.ADMIN_GENERAL_SKYMINE_LOOKUP_TOP.toText().isEmpty()) {
			Messages.ADMIN_GENERAL_SKYMINE_LOOKUP_TOP.replace("{player}", args.getFirst()).send(sender);
		}

		skyMines.forEach(mine -> MineUtils.placeholders(Messages.ADMIN_GENERAL_SKYMINE_LOOKUP_REPEATING, mine).send(sender));

		if (!Messages.ADMIN_GENERAL_SKYMINE_LOOKUP_BOTTOM.toText().isEmpty()) {
			Messages.ADMIN_GENERAL_SKYMINE_LOOKUP_BOTTOM.replace("{player}", args.getFirst()).send(sender);
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Players.filter(p -> p.getName().startsWith(args.getFirst())).map(Player::getName).toList();
		}
		return null;
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.ADMIN_USAGE_SKYMINE_LOOKUP);
		properties.setPermission("skymines.admin.lookup");
		properties.setArgumentsRequired(1);
	}
}
