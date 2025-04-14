package me.colingrimes.skymines.command.skymineadmin.debug;

import com.google.gson.JsonObject;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.structure.SkyMineStructure;
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
			Messages.FAILURE_DEBUG_NO_MINE.send(sender);
			return;
		}

		Position position = Position.of(result.getHitBlock().getLocation());
		for (SkyMine skyMine : plugin.getSkyMineManager().getSkyMines()) {
			SkyMineStructure structure = skyMine.getStructure();
			if (structure.getParameter().contains(position) || structure.getInside().contains(position)) {
				boolean enabled = skyMine.isEnabled();
				JsonObject upgradeJson = (JsonObject) skyMine.getUpgrades().serialize();
				upgradeJson.remove("identifier");
				Messages.DEBUG_SKYMINE
						.replace("{owner}", Players.getName(skyMine.getOwner()))
						.replace("{index}", skyMine.getIndex())
						.replace("{identifier}", (enabled ? "&a" : "&c") + skyMine.getIdentifier())
						.replace("{enabled}", enabled ? "&2Enabled" : "&4Disabled")
						.replace("{type}", Text.format(skyMine.getStructure().getBorderType().name()))
						.replace("{size}", Json.toString(skyMine.getStructure().getMineSize().serialize()))
						.replace("{upgrades}", Json.toString(upgradeJson))
						.replace("{uuid}", skyMine.getUUID().toString())
						.send(sender);
				return;
			}
		}

		// No mine was found.
		Messages.FAILURE_DEBUG_NO_MINE.send(sender);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPermission("skymines.admin.debug");
		properties.setPlayerRequired(true);
	}
}
