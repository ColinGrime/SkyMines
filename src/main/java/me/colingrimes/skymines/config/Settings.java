package me.colingrimes.skymines.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.storage.database.DatabaseCredentials;

import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration
public interface Settings {

	// Main configurations for SkyMines plugin.
	Option<DatabaseCredentials> DATABASE_CREDENTIALS           = database("storage");
	Option<Integer> OPTION_SKYMINE_MAX_PER_PLAYER              = option("option.skymine.max-per-player", 10);
	Option<Boolean> OPTION_SKYMINE_OVERRIDE_TRANSPARENT_BLOCKS = option("option.skymine.override-transparent-blocks", true);
	Option<Boolean> OPTION_SKYMINE_FAST_HOME                   = option("option.skymine.fast-home", true);
	Option<Integer> OPTION_SKYMINE_SETHOME_DISTANCE            = option("option.skymine.sethome-distance", 5);
	Option<Boolean> OPTION_SKYMINE_IGNORE_PHYSICS              = option("option.skymine.ignore-physics", true);
	Option<Boolean> OPTION_TOKEN_PREVENT_DROP                  = option("option.token.prevent-drop", true);
	Option<Boolean> OPTION_RESET_REPLACE_BLOCKS                = option("option.reset.replace-blocks", false);
	Option<Boolean> OPTION_RESET_TELEPORT_HOME                 = option("option.reset.teleport-home", true);
	Option<Boolean> OPTION_RESET_ON_UPGRADE                    = option("option.reset.on-upgrade", true);
	Option<String>  OPTION_COOLDOWN_PICKUP_COOLDOWN            = option("option.cooldown.pickup-cooldown", "dynamic");
	Option<Boolean> OPTION_HOLOGRAM_TOGGLE                     = option("option.hologram.toggle", true);
	Message<?> OPTION_HOLOGRAM_LINES                          = message("option.hologram.lines",
			"&7[&e&lSkyMine&7] &8- &a{name}",
			"&9Owner: &e{player}",
			"&9Reset Timer: &c{reset-cooldown}"
	);
	Option<Boolean> MISC_ENABLE_METRICS                        = option("misc.enable-metrics", true);
}
