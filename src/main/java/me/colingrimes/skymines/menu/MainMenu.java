package me.colingrimes.skymines.menu;

import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Menus;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.midnight.config.util.ConfigurableInventory;
import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.skymines.skymine.option.ResetOptions;
import me.colingrimes.skymines.util.MineUtils;
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
				if (player.getUniqueId().equals(skyMine.getOwner())) {
					performPlayerAction(command);
				} else if (player.hasPermission("skymines.admin.panel")) {
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
			case "HOME" -> Players.command(player, "/skymine home " + skyMine.getIndex());
			case "RESET" -> Players.command(player, "/skymine reset " + skyMine.getIndex());
			case "UPGRADES" -> Players.command(player, "/skymine upgrades " + skyMine.getIndex());
			case "PICKUP" -> Players.command(player, "/skymine pickup " + skyMine.getIndex());
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
				player.teleport(skyMine.getHome().toLocation());
				MineUtils.placeholders(Messages.ADMIN_SUCCESS_HOME, skyMine).send(player);
			}
			case "RESET" -> skyMine.reset(ResetOptions.standard(player));
			case "UPGRADES" -> {
				if (skyMine.isEnabled()) {
					new UpgradeMenu(player, skyMine).open();
				} else {
					MineUtils.placeholders(Messages.FAILURE_SKYMINE_INVALID_IDENTIFIER, skyMine).send(player);
				}
			}
			case "PICKUP" -> skyMine.pickup(player);
		}
	}
}
