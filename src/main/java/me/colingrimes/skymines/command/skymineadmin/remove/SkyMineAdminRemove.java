package me.colingrimes.skymines.command.skymineadmin.remove;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.command.skymineadmin.SkyMineAdmin;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.util.MineUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SkyMineAdminRemove implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		SkyMine skyMine = SkyMineAdmin.getSkyMine(plugin, sender, args);
		if (skyMine != null) {
			MineUtils.placeholders(Messages.ADMIN_SUCCESS_REMOVE, skyMine).send(sender);
			skyMine.remove();
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		return SkyMineAdmin.getSkyMineTabCompletion(plugin, args);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.ADMIN_USAGE_SKYMINE_REMOVE);
		properties.setPermission("skymines.admin.remove");
		properties.setArgumentsRequired(2);
	}
}
