package me.colingrimes.skymines.config;

import me.colingrimes.skymines.data.MenuData;
import me.colingrimes.skymines.data.menu.MenuDataComposition;
import me.colingrimes.skymines.data.menu.MenuDataResetCooldown;
import me.colingrimes.skymines.skymine.upgrade.UpgradeType;
import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.config.util.ConfigurableInventory;

import javax.annotation.Nonnull;
import java.util.Map;

import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration("menus.yml")
public interface Menus {

	Option<ConfigurableInventory> MAIN_MENU = inventory("main-menu");
	Option<ConfigurableInventory> UPGRADE_MENU = inventory("upgrade-menu");
	Option<Map<Integer, UpgradeType>> UPGRADE_MENU_SLOTS = slots("upgrade-menu.slots", section -> UpgradeType.parse(section.getString("upgrade")));
	Option<MenuDataComposition> UPGRADE_MENU_COMPOSITION = option("upgrades.composition", MenuDataComposition::new);
	Option<MenuDataResetCooldown> UPGRADE_MENU_RESET_COOLDOWN = option("upgrades.reset-cooldown", MenuDataResetCooldown::new);

	/**
	 * Gets the {@link MenuData} for the specified type.
	 *
	 * @param type the upgrade type
	 * @return the upgrade data
	 */
	@Nonnull
	static MenuData getMenuData(@Nonnull UpgradeType type) {
		return switch (type) {
			case Composition -> UPGRADE_MENU_COMPOSITION.get();
			case ResetCooldown -> UPGRADE_MENU_RESET_COOLDOWN.get();
		};
	}
}
