package me.colingrimes.skymines.panel;

import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Panels;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import me.colingrimes.skymines.skymine.upgrades.UpgradeType;
import me.colingrimes.skymines.skymine.upgrades.types.SkyMineUpgrade;
import me.colingrimes.midnight.menu.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;

public class UpgradePanel extends Gui {

	private final SkyMine skyMine;
	private final SkyMineUpgrades upgrades;

	public UpgradePanel(@Nonnull Player player, @Nonnull SkyMine skyMine) {
		super(player, Panels.UPGRADE_PANEL.get().getTitle(), Panels.UPGRADE_PANEL.get().getRows());
		this.skyMine = skyMine;
		this.upgrades = skyMine.getUpgrades();
	}

	@Override
	public void draw() {
		// Setup initial panel.
		Panels.UPGRADE_PANEL.get().getItems().forEach((i, item) -> getSlot(i).setItem(item));

		// Setup Block Variety upgrade.
		SkyMineUpgrade blockVariety = skyMine.getUpgrades().getBlockVarietyUpgrade();
		ItemStack blockVarietyItem = Items.of(Panels.UPGRADE_PANEL_BLOCK_VARIETY_MAX.get().clone()).build();
		if (blockVariety.canBeUpgraded()) {
			blockVarietyItem = Items.of(Panels.UPGRADE_PANEL_BLOCK_VARIETY.get().clone())
					.lore(Panels.UPGRADE_PANEL_BLOCK_VARIETY_LORE.get().get(blockVariety.getLevel() + 1))
					.placeholder("{level}", blockVariety.getLevel())
					.placeholder("{next-level}", blockVariety.getLevel() + 1)
					.build();
		}
		int blockVarietySlot = Panels.UPGRADE_PANEL_SLOTS.get().entrySet().stream()
				.filter(e -> e.getValue() == UpgradeType.BlockVariety)
				.map(Map.Entry::getKey)
				.findFirst()
				.orElse(-1);
		if (blockVarietySlot != -1) {
			getSlot(blockVarietySlot).setItem(blockVarietyItem).bind(ClickType.LEFT, e -> {
				attemptUpgrade(getPlayer(), upgrades.getUpgrade(UpgradeType.BlockVariety));
				close();
			});
		}

		// Setup Reset Cooldown upgrade.
		SkyMineUpgrade resetCooldown = skyMine.getUpgrades().getResetCooldownUpgrade();
		ItemStack resetCooldownItem = Items.of(Panels.UPGRADE_PANEL_RESET_COOLDOWN_MAX.get().clone()).build();
		if (resetCooldown.canBeUpgraded()) {
			resetCooldownItem = Items.of(Panels.UPGRADE_PANEL_RESET_COOLDOWN.get().clone())
					.lore(Panels.UPGRADE_PANEL_RESET_COOLDOWN_LORE.get().get(resetCooldown.getLevel() + 1))
					.placeholder("{level}", resetCooldown.getLevel())
					.placeholder("{next-level}", resetCooldown.getLevel() + 1)
					.build();
		}
		int resetCooldownSlot = Panels.UPGRADE_PANEL_SLOTS.get().entrySet().stream()
				.filter(e -> e.getValue() == UpgradeType.ResetCooldown)
				.map(Map.Entry::getKey)
				.findFirst()
				.orElse(-1);
		if (resetCooldownSlot != -1) {
			getSlot(resetCooldownSlot).setItem(resetCooldownItem).bind(ClickType.LEFT, e -> {
				attemptUpgrade(getPlayer(), upgrades.getUpgrade(UpgradeType.ResetCooldown));
				close();
			});
		}
	}

	/**
	 * Attempts to upgrade the specified skymine upgrade.
	 * @param player any player
	 * @param upgrade any upgrade
	 */
	private void attemptUpgrade(@Nonnull Player player, @Nonnull SkyMineUpgrade upgrade) {
		if (!upgrade.canBeUpgraded()) {
			Messages.FAILURE_ALREADY_MAXED.send(player);
		} else if (!upgrade.hasPermission(player)) {
			Messages.FAILURE_NO_PERMISSION.send(player);
		} else if (upgrade.levelUp(player)) {
			Messages.SUCCESS_UPGRADE
					.replace("{upgrade}", Text.format(upgrade.getType().name()))
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
