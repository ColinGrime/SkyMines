package me.colingrimes.skymines.config;

import me.colingrimes.skymines.skymine.upgrades.UpgradeType;
import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.config.util.ConfigurableInventory;
import me.colingrimes.midnight.util.misc.Types;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

import static me.colingrimes.midnight.config.option.OptionFactory.inventory;
import static me.colingrimes.midnight.config.option.OptionFactory.option;
import static me.colingrimes.midnight.config.option.OptionFactory.item;

@Configuration("panels.yml")
public interface Panels {

	// TODO this desperately needs to be refactored into some sort of upgrade config class, but not right now :-)
	Option<ConfigurableInventory> MAIN_PANEL = inventory("main-panel");
	Option<ConfigurableInventory> UPGRADE_PANEL = inventory("upgrade-panel");
	Option<Map<Integer, UpgradeType>> UPGRADE_PANEL_SLOTS = option("upgrade-panel.slots", sec -> Types.mapSlotKeys(sec, (slot -> UpgradeType.parse(sec.getString(slot + ".upgrade")))));
	Option<ItemStack> UPGRADE_PANEL_BLOCK_VARIETY = item("upgrades.block-variety");
	Option<Map<Integer, List<String>>> UPGRADE_PANEL_BLOCK_VARIETY_LORE = option("upgrades.block-variety.lore", sec -> Types.mapIntegerKeys(sec, sec::getStringList));
	Option<ItemStack> UPGRADE_PANEL_BLOCK_VARIETY_MAX = item("upgrades.block-variety.max");
	Option<ItemStack> UPGRADE_PANEL_RESET_COOLDOWN = item("upgrades.reset-cooldown");
	Option<Map<Integer, List<String>>> UPGRADE_PANEL_RESET_COOLDOWN_LORE = option("upgrades.reset-cooldown.lore", sec -> Types.mapIntegerKeys(sec, sec::getStringList));
	Option<ItemStack> UPGRADE_PANEL_RESET_COOLDOWN_MAX = item("upgrades.reset-cooldown.max");
}
