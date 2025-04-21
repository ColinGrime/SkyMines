package me.colingrimes.skymines.menu;

import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Menus;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.upgrade.SkyMineUpgrades;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;
import me.colingrimes.skymines.skymine.upgrade.type.SkyMineUpgrade;
import me.colingrimes.midnight.menu.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nonnull;

public class UpgradeMenu extends Gui {

	private final SkyMine skyMine;
	private final SkyMineUpgrades upgrades;

	public UpgradeMenu(@Nonnull Player player, @Nonnull SkyMine skyMine) {
		super(player, Menus.UPGRADE_MENU.get().getTitle(), Menus.UPGRADE_MENU.get().getRows());
		this.skyMine = skyMine;
		this.upgrades = skyMine.getUpgrades();
	}

	@Override
	public void draw() {
		// Setup initial menu.
		Menus.UPGRADE_MENU.get().getItems().forEach((i, item) -> getSlot(i).setItem(item));

		// Setup upgrade slots.
		for (var upgradeSlot : Menus.UPGRADE_MENU_SLOTS.get().entrySet()) {
			getSlot(upgradeSlot.getKey()).setItem(Menus.getMenuData(upgradeSlot.getValue()).getMenuItem(skyMine)).bind(ClickType.LEFT, e -> {
				attemptUpgrade(upgradeSlot.getValue());
				close();
			});
		}
	}

	/**
	 * Attempts to upgrade the specified upgrade type.
	 *
	 * @param type the ugprade type
	 */
	private void attemptUpgrade(@Nonnull UpgradeType type) {
		Player player = getPlayer();
		SkyMineUpgrade upgrade = upgrades.getUpgrade(type);
		if (!upgrade.canBeUpgraded()) {
			Messages.FAILURE_SKYMINE_UPGRADE_MAXED.send(player);
		} else if (!upgrade.hasPermission(player)) {
			Messages.FAILURE_MISC_NO_PERMISSION.send(player);
		} else if (upgrade.levelUp(player)) {
			Messages.SUCCESS_UPGRADE
					.replace("{upgrade}", upgrade.getType().getName())
					.replace("{level}", upgrade.getLevel())
					.send(player);
			skyMine.save();

			// reset the mine on upgrade
			if (Settings.OPTION_RESET_ON_UPGRADE.get()) {
				skyMine.reset();
			}
		} else {
			Messages.FAILURE_SKYMINE_UPGRADE_FUNDS.send(player);
		}
	}
}
