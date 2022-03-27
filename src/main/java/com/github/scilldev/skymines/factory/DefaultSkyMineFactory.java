package com.github.scilldev.skymines.factory;

import com.github.scilldev.skymines.DefaultSkyMine;
import com.github.scilldev.skymines.structure.MineStructure;
import com.github.scilldev.skymines.structure.MineSize;
import com.github.scilldev.skymines.SkyMine;
import com.github.scilldev.skymines.upgrades.StandardUpgrades;
import com.github.scilldev.skymines.upgrades.Upgrades;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DefaultSkyMineFactory implements SkyMineFactory {

	private final Upgrades upgradeOptions;

	public DefaultSkyMineFactory() {
		this.upgradeOptions = new StandardUpgrades();
	}

	public DefaultSkyMineFactory(Upgrades upgradeOptions) {
		this.upgradeOptions = upgradeOptions;
	}

	@Override
	public SkyMine createSkyMine(Player owner, Location location, int length, int width, int height) {
		float yaw = owner.getLocation().getYaw();
		if (yaw < 0) {
			yaw += 360;
		}

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
		MineStructure structure = new MineStructure(location, endLoc, new MineSize(length, width, height));
		structure.buildParameter();
		structure.buildInside();

		// creates the home of the mine
		Location home = location.clone().add(0, 1, 0);
		home.setYaw(yaw);

		// creates new sky mine
		return new DefaultSkyMine(owner.getUniqueId(), structure, home, upgradeOptions);
	}
}
