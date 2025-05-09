package me.colingrimes.skymines.command.skymineadmin.debug;

import com.google.gson.JsonObject;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.structure.SkyMineStructure;
import me.colingrimes.skymines.util.MineUtils;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;

public class SkyMineAdminDebug implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Location eye = sender.player().getEyeLocation();
		RayTraceResult result = sender.world().rayTrace(eye, eye.getDirection(), 100, FluidCollisionMode.NEVER, true, 0, e -> !e.equals(sender.player()));
		if (result == null || result.getHitBlock() == null) {
			Messages.ADMIN_FAILURE_SKYMINE_DEBUG.send(sender);
			return;
		}

		Position position = Position.of(result.getHitBlock().getLocation());
		for (SkyMine skyMine : plugin.getSkyMineManager().getSkyMines()) {
			SkyMineStructure structure = skyMine.getStructure();
			if (structure.contains(position)) {
				JsonObject upgradeJson = (JsonObject) skyMine.getUpgrades().serialize();
				upgradeJson.remove("identifier");
				MineUtils.placeholders(Messages.ADMIN_GENERAL_SKYMINE_DEBUG.replace("{enabled}", skyMine.isEnabled() ? "&2Enabled" : "&4Disabled"), skyMine)
						.replace("{type}", Text.format(skyMine.getStructure().getBorderType().name()))
						.replace("{size}", Json.toString(skyMine.getStructure().getMineSize().serialize()))
						.replace("{upgrades}", Json.toString(upgradeJson))
						.replace("{uuid}", skyMine.getUUID().toString())
						.send(sender);
				return;
			}
		}

		// No mine was found.
		Messages.ADMIN_FAILURE_SKYMINE_DEBUG.send(sender);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPermission("skymines.admin.debug");
		properties.setPlayerRequired(true);
	}
}
