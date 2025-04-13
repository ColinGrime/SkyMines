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
import java.util.Map;

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
		Menus.UPGRADE_MENU_SLOTS.get().entrySet().stream()
				.filter(e -> e.getValue() == UpgradeType.Composition)
				.map(Map.Entry::getKey)
				.forEach(slot -> getSlot(slot).setItem(Menus.UPGRADE_MENU_COMPOSITION.get().getMenuItem(skyMine)).bind(ClickType.LEFT, e -> {
					attemptUpgrade(getPlayer(), upgrades.getUpgrade(UpgradeType.Composition));
					close();
				}));
		Menus.UPGRADE_MENU_SLOTS.get().entrySet().stream()
				.filter(e -> e.getValue() == UpgradeType.ResetCooldown)
				.map(Map.Entry::getKey)
				.forEach(slot -> getSlot(slot).setItem(Menus.UPGRADE_MENU_RESET_COOLDOWN.get().getMenuItem(skyMine)).bind(ClickType.LEFT, e -> {
					attemptUpgrade(getPlayer(), upgrades.getUpgrade(UpgradeType.ResetCooldown));
					close();
				}));
	}

	/**
	 * Attempts to upgrade the specified skymine upgrade.
	 *
	 * @param player the player
	 * @param upgrade the upgrade
	 */
	private void attemptUpgrade(@Nonnull Player player, @Nonnull SkyMineUpgrade upgrade) {
		if (!upgrade.canBeUpgraded()) {
			Messages.FAILURE_ALREADY_MAXED.send(player);
		} else if (!upgrade.hasPermission(player)) {
			Messages.FAILURE_NO_PERMISSION.send(player);
		} else if (upgrade.levelUp(player)) {
			Messages.SUCCESS_UPGRADE
					.replace("{upgrade}", upgrade.getType().getName())
					.replace("{level}", upgrade.getLevel())
					.send(player);
			skyMine.save();

			// reset the mine on upgrade
			if (Settings.OPTIONS_RESET_ON_UPGRADE.get()) {
				skyMine.reset(true);
			}
		} else {
			Messages.FAILURE_NO_FUNDS.send(player);
		}
	}
}
