package me.colingrimes.skymines.config;

import me.colingrimes.skymines.skymine.structure.material.MaterialVariety;
import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.storage.sql.DatabaseCredentials;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.midnight.util.text.Parser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.*;

import static me.colingrimes.midnight.config.option.OptionFactory.database;
import static me.colingrimes.midnight.config.option.OptionFactory.option;

@Configuration
public interface Settings {

	// Main configurations for SkyMines plugin.
	Option<DatabaseCredentials> DATABASE_CREDENTIALS = database("save");
	Option<Integer> OPTIONS_MAX_PER_PLAYER = option("options.max-per-player", 10);
	Option<Boolean> OPTIONS_REPLACE_BLOCKS = option("options.replace-blocks", false);
	Option<Boolean> OPTIONS_TELEPORT_HOME_ON_RESET = option("options.teleport-home-on-reset", true);
	Option<Boolean> OPTIONS_RESET_ON_UPGRADE = option("options.reset-on-reset", true);
	Option<Boolean> OPTIONS_NOTIFY_ON_RESET_COOLDOWN_FINISH = option("options.notify-on-reset-cooldown-finish", true);
	Option<Boolean> OPTIONS_NOTIFY_ON_PICKUP_COOLDOWN_FINISH = option("options.notify-on-pickup-cooldown-finish", true);
	Option<Integer> OPTIONS_PICKUP_COOLDOWN = option("options.pickup-cooldown", 300);
	Option<Integer> OPTIONS_PLACEMENT_COOLDOWN = option("options.placement-cooldown", 10);
	Option<Boolean> OPTIONS_PREVENT_TOKEN_DROP = option("options.prevent-token-drop", true);
	Option<Boolean> OPTIONS_OVERRIDE_TRANSPARENT_BLOCKS = option("options.override-transparent-blocks", true);
	Option<Boolean> MISC_ENABLE_METRICS = option("misc.enable-metrics", true);

	// SkyMines token item.
	Option<ItemStack> TOKEN = option("token",
			Items.of(Material.TRIPWIRE_HOOK)
					.name("&e&lSkyMine &7[&a{length}&7x&a{height}&7x&a{width}&7]")
					.lore(List.of("&7Right Click → &ePlaces Structure"))
					.build()
	);

	// Block variety configurations.
	Option<Integer>                       UPGRADES_BLOCK_VARIETY_MAX_LEVEL = option("upgrades.block-variety", sec -> Collections.max(Types.integerKeys(sec)));
	Option<Map<Integer, Double>>          UPGRADES_BLOCK_VARIETY_COSTS = option("upgrades.block-variety", sec -> Types.mapIntegerKeys(sec, slot -> sec.getDouble(slot + ".cost")));
	Option<Map<Integer, MaterialVariety>> UPGRADES_BLOCK_VARIETY = option("upgrades.block-variety", sec -> {
		return Types.mapIntegerKeys(sec, slot -> {
			MaterialVariety variety = new MaterialVariety();
			sec.getStringList(slot + ".upgrade").forEach(types -> variety.addType(types.split(" ")[0], types.split(" ")[1]));
			return variety;
		});
	});
	Option<Set<Material>> ALL_POSSIBLE_MATERIALS = option("upgrades.block-variety", sec -> {
		Set<Material> materials = new HashSet<>();
		sec.getKeys(false).stream()
				.filter(key -> key.matches("\\d+"))
				.forEach(key -> sec.getStringList(key + ".upgrade")
						.forEach(types -> {
							Material material = Material.matchMaterial(types.split(" ")[0]);
							if (material != null) {
								materials.add(material);
							}
						}));
		return materials;
	});


	// Reset cooldown configurations.
	Option<Integer>                UPGRADES_RESET_COOLDOWN_MAX_LEVEL = option("upgrades.reset-cooldown", sec -> Collections.max(Types.integerKeys(sec)));
	Option<Map<Integer, Double>>   UPGRADES_RESET_COOLDOWN_COSTS = option("upgrades.reset-cooldown", sec -> Types.mapIntegerKeys(sec, slot -> sec.getDouble(slot + ".cost")));
	Option<Map<Integer, Duration>> UPGRADES_RESET_COOLDOWN = option("upgrades.reset-cooldown", sec -> Types.mapIntegerKeys(sec, slot -> Parser.parseDuration(sec.getString(slot + ".upgrade"))));
}