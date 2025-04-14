package me.colingrimes.skymines.menu;

import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Menus;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.midnight.config.util.ConfigurableInventory;
import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.bukkit.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nonnull;
import java.time.Duration;

public class MainMenu extends Gui {

	private final SkyMines plugin;
	private final SkyMine skyMine;

	public MainMenu(@Nonnull SkyMines plugin, @Nonnull Player player, @Nonnull SkyMine skyMine) {
		super(player, Menus.MAIN_MENU.get().getTitle(), Menus.MAIN_MENU.get().getRows());
		this.plugin = plugin;
		this.skyMine = skyMine;
	}

	@Override
	public void draw() {
		ConfigurableInventory panel = Menus.MAIN_MENU.get();
		panel.getItems().forEach((i, item) -> {
			Duration duration = plugin.getCooldownManager().getSkyMineCooldown().getTimeLeft(skyMine);
			getSlot(i).setItem(Items.of(item.clone()).placeholder("{time}", Text.format(duration)).build());

			String command = panel.getCommand(i);
			if (command == null || command.isEmpty()) {
				return;
			}

			getSlot(i).bind(ClickType.LEFT, e -> {
				close();
				if (getPlayer().getUniqueId().equals(skyMine.getOwner())) {
					performPlayerAction(command);
				} else if (getPlayer().hasPermission("skymines.admin.panel")) {
					performAdminAction(command);
				}
			});
		});
	}

	/**
	 * Performs one of the available actions on the menu.
	 *
	 * @param action the action to perform
	 */
	private void performPlayerAction(@Nonnull String action) {
		switch (action) {
			case "HOME" -> Players.command(getPlayer(), "/skymine home " + skyMine.getIndex());
			case "RESET" -> Players.command(getPlayer(), "/skymine reset " + skyMine.getIndex());
			case "UPGRADES" -> Players.command(getPlayer(), "/skymine upgrades " + skyMine.getIndex());
			case "PICKUP" -> Players.command(getPlayer(), "/skymine pickup " + skyMine.getIndex());
		}
	}

	/**
	 * Performs one of the available actions on the menu.
	 * <p>
	 * This is used for Admins who have another player's mine menu open.
	 *
	 * @param action the action to perform
	 */
	private void performAdminAction(@Nonnull String action) {
		switch (action) {
			case "HOME" -> {
				getPlayer().teleport(skyMine.getHome().toLocation());
				Messages.ADMIN_SUCCESS_HOME.replace("{player}", Players.getName(skyMine.getOwner())).send(getPlayer());
			}
			case "RESET" -> {
				if (!skyMine.isEnabled()) {
					Messages.FAILURE_SKYMINE_INVALID_IDENTIFIER.replace("{id}", skyMine.getIdentifier()).send(getPlayer());
					return;
				}
				if (!skyMine.reset(false)) {
					Duration time = plugin.getCooldownManager().getSkyMineCooldown().getTimeLeft(skyMine);
					Messages.FAILURE_COOLDOWN_RESET.replace("{time}", Text.format(time)).send(getPlayer());
					return;
				}

				Messages.ADMIN_SUCCESS_RESET.replace("{player}", Players.getName(skyMine.getOwner())).send(getPlayer());
				if (Settings.OPTIONS_TELEPORT_HOME_ON_RESET.get()) {
					getPlayer().teleport(skyMine.getHome().toLocation());
				}
			}
			case "UPGRADES" -> {
				if (!skyMine.isEnabled()) {
					Messages.FAILURE_SKYMINE_INVALID_IDENTIFIER.replace("{id}", skyMine.getIdentifier()).send(getPlayer());
					return;
				}
				new UpgradeMenu(getPlayer(), skyMine).open();
			}
			case "PICKUP" -> {
				if (skyMine.pickup(getPlayer())) {
					Messages.ADMIN_SUCCESS_PICKUP.replace("{player}", Players.getName(skyMine.getOwner())).send(getPlayer());
				} else {
					Messages.FAILURE_TOKEN_NO_INVENTORY_SPACE.send(getPlayer());
				}
			}
		}
	}
}
