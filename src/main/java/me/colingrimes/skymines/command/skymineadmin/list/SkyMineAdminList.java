package me.colingrimes.skymines.command.skymineadmin.list;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Mines;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SkyMineAdminList implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		List<String> skyMines = new ArrayList<>();
		for (String identifier : Mines.MINES_ALL.get()) {
			if (Mines.MINES.get().containsKey(identifier)) {
				skyMines.add("&a" + identifier);
			} else {
				skyMines.add("&c" + identifier);
			}
		}

		Messages.ADMIN_GENERAL_SKYMINE_LIST
				.replace("{amount}", Mines.MINES.get().size())
				.replace("{skymines}", String.join("&f, ", skyMines))
				.send(sender);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPermission("skymines.admin.list");
	}
}
