package me.colingrimes.skymines.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.storage.database.DatabaseCredentials;

import static me.colingrimes.midnight.config.option.OptionFactory.database;
import static me.colingrimes.midnight.config.option.OptionFactory.option;

@Configuration
public interface Settings {

	// Main configurations for SkyMines plugin.
	Option<DatabaseCredentials> DATABASE_CREDENTIALS         = database("save");
	Option<Integer> OPTIONS_MAX_PER_PLAYER                   = option("options.max-per-player", 10);
	Option<Boolean> OPTIONS_REPLACE_BLOCKS                   = option("options.replace-blocks", false);
	Option<Boolean> OPTIONS_TELEPORT_HOME_ON_RESET           = option("options.teleport-home-on-reset", true);
	Option<Boolean> OPTIONS_RESET_ON_UPGRADE                 = option("options.reset-on-upgrade", true);
	Option<Boolean> OPTIONS_NOTIFY_ON_RESET_COOLDOWN_FINISH  = option("options.notify-on-reset-cooldown-finish", true);
	Option<Boolean> OPTIONS_NOTIFY_ON_PICKUP_COOLDOWN_FINISH = option("options.notify-on-pickup-cooldown-finish", true);
	Option<String>  OPTIONS_PICKUP_COOLDOWN                  = option("options.pickup-cooldown", "dynamic");
	Option<Integer> OPTIONS_PLACEMENT_COOLDOWN               = option("options.placement-cooldown", 10);
	Option<Boolean> OPTIONS_PREVENT_TOKEN_DROP               = option("options.prevent-token-drop", true);
	Option<Boolean> OPTIONS_OVERRIDE_TRANSPARENT_BLOCKS      = option("options.override-transparent-blocks", true);
	Option<Boolean> OPTIONS_FAST_HOME                        = option("options.fast-home", true);
	Option<Boolean> MISC_ENABLE_METRICS                      = option("misc.enable-metrics", true);
}