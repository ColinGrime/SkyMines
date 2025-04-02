package me.colingrimes.skymines.panel;

import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Panels;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.midnight.config.util.ConfigurableInventory;
import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.bukkit.Players;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.annotation.Nonnull;
import java.time.Duration;

public class MainPanel extends Gui {

	private final SkyMines plugin;
	private final SkyMine skyMine;

	public MainPanel(@Nonnull SkyMines plugin, @Nonnull Player player, @Nonnull SkyMine skyMine) {
		super(player, Panels.MAIN_PANEL.get().getTitle(), Panels.MAIN_PANEL.get().getRows());
		this.plugin = plugin;
		this.skyMine = skyMine;
	}

	@Override
	public void draw() {
		ConfigurableInventory panel = Panels.MAIN_PANEL.get();
		panel.getItems().forEach((i, item) -> {
			Duration duration = plugin.getCooldownManager().getSkyMineCooldown().getTimeLeft(skyMine);
			getSlot(i).setItem(Items.of(item.clone()).placeholder("{time}", Text.formatTime(duration)).build());

			String command = panel.getCommand(i);
			if (command == null || command.isEmpty()) {
				return;
			}

			getSlot(i).bind(ClickType.LEFT, e -> {
				close();

				// TODO - I really hate this, but I need to think through a better way.
				// Using commands with placeholders like before wouldn't work for admins accessing player's skymines.
				// Also, the logic is slightly different for admins, so maybe just switch away from the 'commands' property?
				// Doubly also, swap out the standard success messages with admin messages with the owner's name.
				switch (command) {
					case "/skymines home {id}" -> {
						getPlayer().teleport(skyMine.getHome());
						Messages.SUCCESS_HOME.send(getPlayer());
					}
					case "/skymines reset {id}" -> {
						if (!skyMine.reset(false)) {
							Duration time = plugin.getCooldownManager().getSkyMineCooldown().getTimeLeft(skyMine);
							Messages.FAILURE_ON_RESET_COOLDOWN.replace("{time}", Text.formatTime(time)).send(getPlayer());
							return;
						}

						Messages.SUCCESS_RESET.send(getPlayer());
						if (Settings.OPTIONS_TELEPORT_HOME_ON_RESET.get()) {
							getPlayer().teleport(skyMine.getHome());
						}
					}
					case "/skymines upgrades {id}" -> new UpgradePanel(getPlayer(), skyMine).open();
					case "/skymines pickup {id}" -> {
						if (skyMine.pickup(getPlayer())) {
							Messages.SUCCESS_PICKUP.send(getPlayer());
						} else {
							Messages.FAILURE_NO_INVENTORY_SPACE.send(getPlayer());
						}
					}
					// Set the command of the slot if applicable.
					default -> Players.command(getPlayer(), Placeholders.of("{id}", skyMine.getId()).apply(command).toText());
				}
			});
		});
	}
}
