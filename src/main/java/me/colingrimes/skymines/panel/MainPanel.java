package me.colingrimes.skymines.panel;

import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Panels;
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

			// Set the command of the slot if applicable.
			String command = panel.getCommand(i);
			if (command != null && !command.isEmpty()) {
				getSlot(i).bind(ClickType.LEFT, e -> {
					close();
					Players.command(getPlayer(), Placeholders.of("{id}", skyMine.getId()).apply(command).toText());
				});
			}
		});
	}
}
