package me.colingrimes.skymines.command.skymine.settings;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.menu.SettingsMenu;

import javax.annotation.Nonnull;

public class SkyMineSettings implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		new SettingsMenu(plugin, sender.player()).open();
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPlayerRequired(true);
	}
}
