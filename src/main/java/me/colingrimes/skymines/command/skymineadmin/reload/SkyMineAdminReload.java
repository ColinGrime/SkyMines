package me.colingrimes.skymines.command.skymineadmin.reload;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;

import javax.annotation.Nonnull;

public class SkyMineAdminReload implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		plugin.getConfigurationManager().reload();
		Messages.SUCCESS_RELOADED.send(sender);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.SUCCESS_RELOADED);
		properties.setPermission("skymines.admin.reload");
	}
}
