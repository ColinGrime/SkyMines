package me.colingrimes.skymines.skymine.factory;

import me.colingrimes.midnight.geometry.Pose;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.geometry.Size;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Mines;
import me.colingrimes.skymines.skymine.DefaultSkyMine;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.structure.SkyMineStructure;
import me.colingrimes.skymines.skymine.token.SkyMineToken;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;

public class DefaultSkyMineFactory implements SkyMineFactory {

	private final SkyMines plugin;

	public DefaultSkyMineFactory(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	@Nonnull
	public Optional<SkyMine> createSkyMine(@Nonnull Player owner, @Nonnull ItemStack token) {
		SkyMineToken tokenProvider = plugin.getSkyMineManager().getToken();
		Mines.Mine mine = tokenProvider.getMine(token);
		if (mine == null) {
			return Optional.empty();
		}

		float yaw = owner.getLocation().getYaw();
		if (yaw < 0) {
			yaw += 360;
		}

		Size size = tokenProvider.getMineSize(token);
		int length = size.getLength() + 1;
		int height = size.getHeight();
		int width = size.getWidth() + 1;

		Position corner1 = Position.of(owner.getLocation().clone().subtract(0, 1, 0));
		Position corner2;
		if (yaw >= 0 && yaw <= 35)
			corner2 = corner1.add(-width, -height, length);
		else if (yaw >= 35 && yaw <= 90)
			corner2 = corner1.add(-length, -height, width);
		else if (yaw >= 90 && yaw <= 135)
			corner2 = corner1.add(-length, -height, -width);
		else if (yaw >= 135 && yaw <= 180)
			corner2 = corner1.add(-width, -height, -length);
		else if (yaw >= 180 && yaw <= 225)
			corner2 = corner1.add(width, -height, -length);
		else if (yaw >= 225 && yaw <= 270)
			corner2 = corner1.add(length, -height, -width);
		else if (yaw >= 270 && yaw <= 325)
			corner2 = corner1.add(length, -height, width);
		else
			corner2 = corner1.add(width, -height, length);

		// check for access and blocks in the way
		SkyMineStructure structure = new SkyMineStructure(corner1, corner2, mine.getBorderType());
		if (!structure.canBuild(owner)) {
			return Optional.empty();
		}

		// creates the home of the mine
		Location home = owner.getLocation().clone().add(0, 1, 0);
		home.setYaw(yaw);

		// creates new skymine and builds the mine
		return Optional.of(new DefaultSkyMine(plugin, owner.getUniqueId(), mine.getIdentifier(), structure, tokenProvider.getUpgrades(token), Pose.of(home)));
	}
}
