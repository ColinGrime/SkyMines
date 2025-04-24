package me.colingrimes.skymines.menu;

import me.colingrimes.midnight.config.util.ConfigurableInventory;
import me.colingrimes.midnight.menu.Gui;
import me.colingrimes.midnight.menu.Slot;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Menus;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.player.PlayerSettings;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.option.ResetOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class SettingsMenu extends Gui {

	private final SkyMines plugin;
	private final PlayerSettings settings;

	public SettingsMenu(@Nonnull SkyMines plugin, @Nonnull Player player) {
		super(player, Menus.SETTINGS_MENU.get().getTitle(), Menus.SETTINGS_MENU.get().getRows());
		this.plugin = plugin;
		this.settings = plugin.getPlayerManager().getSettings(player);
	}

	@Override
	public void draw() {
		ConfigurableInventory panel = Menus.SETTINGS_MENU.get();
		panel.getItems().forEach((i, item) -> {
			getSlot(i).setItem(Items.of(item.clone()).build());

			String command = panel.getCommand(i);
			if (command == null || command.isEmpty()) {
				return;
			}

			setItem(getSlot(i), item, command);
			getSlot(i).bind(ClickType.LEFT, e -> {
				boolean isNotify = command.equals("NOTIFY");
				boolean isAutoReset = command.equals("AUTO_RESET");
				if ((isNotify && !player.hasPermission("skymines.settings.notify")) || (isAutoReset && !player.hasPermission("skymines.settings.autoreset"))) {
					Messages.FAILURE_MISC_NO_PERMISSION.send(player);
					close();
					return;
				}

				switch (command) {
					case "NOTIFY" -> settings.setNotify(!settings.shouldNotify());
					case "AUTO_RESET" -> {
						settings.setAutoReset(!settings.shouldAutoReset());
						if (!settings.shouldAutoReset()) {
							break;
						}

						int totalReset = 0;
						for (SkyMine skyMine : plugin.getSkyMineManager().getSkyMines(player)) {
							totalReset += (skyMine.reset(ResetOptions.create().cooldowns(true).build()) > 0 ? 1 : 0);
						}

						// Send just 1 automatic reset message instead of multiple.
						if (totalReset > 0) {
							Messages.GENERAL_COOLDOWN_RESET_AUTOMATIC_MULTIPLE.send(player);
						}
					}
				}
				setItem(getSlot(i), item, command);
			});
		});
	}

	private void setItem(@Nonnull Slot slot, @Nonnull ItemStack item, @Nonnull String command) {
		boolean enabled = switch (command) {
			case "NOTIFY" -> settings.shouldNotify();
			case "AUTO_RESET" -> settings.shouldAutoReset();
			default -> false;
		};
		slot.setItem(Items.of(item.clone()).glow(enabled).placeholder("{enabled}", enabled ? "&2Enabled" : "&cDisabled").build());
	}
}
