package me.colingrimes.skymines.skymine.factory;

import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.skymine.DefaultSkyMine;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.structure.MineSize;
import me.colingrimes.skymines.skymine.structure.MineStructure;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Optional;

public class DefaultSkyMineFactory implements SkyMineFactory {

	private final SkyMines plugin;

	public DefaultSkyMineFactory(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	@Nonnull
	public Optional<SkyMine> createSkyMine(@Nonnull Player owner, @Nonnull Location location, @Nonnull MineSize size, @Nonnull Material borderType, @Nonnull SkyMineUpgrades upgrades) {
		float yaw = owner.getLocation().getYaw();
		if (yaw < 0) {
			yaw += 360;
		}

		int length = size.getLength();
		int height = size.getHeight();
		int width = size.getWidth();
		Location endLoc;

		if (yaw >= 0 && yaw <= 35)
			endLoc = location.clone().add(-width, -height, length);
		else if (yaw >= 35 && yaw <= 90)
			endLoc = location.clone().add(-length, -height, width);
		else if (yaw >= 90 && yaw <= 135)
			endLoc = location.clone().add(-length, -height, -width);
		else if (yaw >= 135 && yaw <= 180)
			endLoc = location.clone().add(-width, -height, -length);
		else if (yaw >= 180 && yaw <= 225)
			endLoc = location.clone().add(width, -height, -length);
		else if (yaw >= 225 && yaw <= 270)
			endLoc = location.clone().add(length, -height, -width);
		else if (yaw >= 270 && yaw <= 325)
			endLoc = location.clone().add(length, -height, width);
		else
			endLoc = location.clone().add(width, -height, length);

		// creates and builds structure
		MineStructure structure = new MineStructure(location, endLoc, size, borderType);

		// check for access and blocks in the way
		if (!structure.doBlockCheck(owner)) {
			return Optional.empty();
		}

		// build the mine
		structure.buildParameter();
		structure.buildInside(upgrades.getBlockVarietyUpgrade().getBlockVariety());

		// creates the home of the mine
		Location home = location.clone().add(0, 1, 0);
		home.setYaw(yaw);

		// creates new skymine
		SkyMine skyMine = new DefaultSkyMine(plugin, owner.getUniqueId(), structure, upgrades, home);
		skyMine.reset(true);
		return Optional.of(skyMine);
	}
}
