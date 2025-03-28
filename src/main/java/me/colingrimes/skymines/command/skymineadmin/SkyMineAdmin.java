package me.colingrimes.skymines.command.skymineadmin;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;

import javax.annotation.Nonnull;

public class SkyMineAdmin implements Command<SkyMines> {

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINES_ADMIN_COMMAND);
		properties.setPermission("skymines.admin");
		properties.setAliases("skyminesadmin", "smadmin", "sma");
	}
}
