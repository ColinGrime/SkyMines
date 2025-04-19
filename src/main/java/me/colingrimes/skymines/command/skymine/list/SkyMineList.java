package me.colingrimes.skymines.command.skymine.list;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.util.text.Markdown;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.List;

public class SkyMineList implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		List<SkyMine> skyMines = plugin.getSkyMineManager().getSkyMines(sender.player());
		if (skyMines.isEmpty()) {
			Messages.FAILURE_SKYMINE_NONE_OWNED.send(sender);
			return;
		}

		if (!Messages.GENERAL_SKYMINE_LIST_TOP.toText().isEmpty()) {
			Messages.GENERAL_SKYMINE_LIST_TOP.send(sender);
		}
		for (int i=1; i<=skyMines.size(); i++) {
			Location loc = skyMines.get(i - 1).getHome().toLocation();
			String message = Messages.GENERAL_SKYMINE_LIST_REPEATING
					.replace("{id}", i)
					.replace("{x}", loc.getBlockX())
					.replace("{y}", loc.getBlockY())
					.replace("{z}", loc.getBlockZ())
					.toText();
			Markdown.of("[" + message + "](/skymines_home_" + i + " &cGo Home!)").send(sender);
		}
		if (!Messages.GENERAL_SKYMINE_LIST_BOTTOM.toText().isEmpty()) {
			Messages.GENERAL_SKYMINE_LIST_BOTTOM.send(sender);
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPlayerRequired(true);
	}
}
