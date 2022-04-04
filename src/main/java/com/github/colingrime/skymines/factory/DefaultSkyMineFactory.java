package com.github.colingrime.skymines.factory;

import com.github.colingrime.SkyMines;
import com.github.colingrime.skymines.DefaultSkyMine;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.skymines.structure.MineStructure;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

public class DefaultSkyMineFactory implements SkyMineFactory {

	private final SkyMines plugin;

	public DefaultSkyMineFactory(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public Optional<SkyMine> createSkyMine(Player owner, Location location, MineSize size, SkyMineUpgrades upgrades) {
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
		MineStructure structure = new MineStructure(location, endLoc, size);
		if (!structure.setup()) {
			return Optional.empty();
		}

		structure.buildParameter();
		structure.buildInside(upgrades.getBlockVarietyUpgrade().getBlockVariety());

		// creates the home of the mine
		Location home = location.clone().add(0, 1, 0);
		home.setYaw(yaw);

		// creates new sky mine
		return Optional.of(new DefaultSkyMine(plugin, owner.getUniqueId(), structure, home, upgrades));
	}
}
