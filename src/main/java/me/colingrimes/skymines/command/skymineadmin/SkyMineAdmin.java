package me.colingrimes.skymines.command.skymineadmin;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;

import javax.annotation.Nonnull;

public class SkyMineAdmin implements Command<SkyMines> {

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.ADMIN_USAGE_SKYMINE);
		properties.setPermission("skymines.admin");
		properties.setAliases("skyminesadmin", "smadmin", "sma");
	}
}
