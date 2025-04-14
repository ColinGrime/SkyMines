package me.colingrimes.skymines.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.message.Message;

import static me.colingrimes.midnight.config.option.OptionFactory.message;

@Configuration("messages.yml")
public interface Messages {

	/**************************************************
	 *                 General Messages               *
	 **************************************************/
	Message<?> GENERAL_SKYMINE_LIST_TOP       = message("general.skymine-list.top", "&7&l&m━━━━━━━━━━━━━━━━━━&7 &e&lSkyMine &aList &7&l&m━━━━━━━━━━━━━━━━━━");
	Message<?> GENERAL_SKYMINE_LIST_REPEATING = message("general.skymine-list.repeating", "&7→ [#{id}] &eClick to teleport home. &7(&a{x}x&7, &a{y}y&7, &a{z}z&7)");
	Message<?> GENERAL_SKYMINE_LIST_BOTTOM    = message("general.skymine-list.bottom");
	Message<?> GENERAL_COOLDOWN_RESET_FINISH  = message("general.cooldown.reset-finish", "&aThe &eSkyMine &awith the ID &e{id} &ahas just finished cooling down.");
	Message<?> GENERAL_COOLDOWN_PICKUP_FINISH = message("general.cooldown.pickup-finish", "&aYou are free to place &eSkyMines &aagain.");

	/**************************************************
	 *                 Success Messages               *
	 **************************************************/
	Message<?> SUCCESS_RECEIVE = message("success.receive", "&2&l✓ &aYou have received &e{amount}x {token}&a.");
	Message<?> SUCCESS_UPGRADE = message("success.upgrade", "&2&l✓ &aYou have upgraded &e{upgrade} &ato level &e{level}&a.");
	Message<?> SUCCESS_HOME    = message("success.home", "&2&l✓ &aYou have been teleported to your &eSkyMine&a.");
	Message<?> SUCCESS_SETHOME = message("success.sethome", "&2&l✓ &aYou have changed your &eSkyMine's &ahome location.");
	Message<?> SUCCESS_RESET   = message("success.reset", "&2&l✓ &aYou have reset your &eSkyMine&a.");
	Message<?> SUCCESS_PICKUP  = message("success.pickup", "&2&l✓ &aYou have picked up your &eSkyMine&a.");
	Message<?> SUCCESS_PLACE   = message("success.place", "&2&l✓ &aYou have placed your &eSkyMine&a.");

	/**************************************************
	 *                 Failure Messages               *
	 **************************************************/
	Message<?> FAILURE_MAX_AMOUNT            = message("failure.max-amount", "&4&l❌ &cMaximum amount of mines reached.");
	Message<?> FAILURE_TOO_FAR_AWAY          = message("failure.too-far-away", "&4&l❌ &cYou are too far away from this &eSkyMine&c.");
	Message<?> FAILURE_NO_SPACE              = message("failure.no-space", "&4&l❌ &cThere is no space here to place a &eSkyMine&c.");
	Message<?> FAILURE_NO_INVENTORY_SPACE    = message("failure.no-inventory-space", "&4&l❌ &cYou do not have enough inventory space.");
	Message<?> FAILURE_NO_FUNDS              = message("failure.no-funds", "&4&l❌ &cYou do not have enough money to purchase this upgrade.");
	Message<?> FAILURE_NO_SKYMINE            = message("failure.no-skymine", "&4&l❌ &cNo mine &cexists with the ID '&e{id}&c'.");
	Message<?> FAILURE_NO_SKYMINES           = message("failure.no-skymines", "&4&l❌ &cYou currently do not own any mines.");
	Message<?> FAILURE_NO_PERMISSION         = message("failure.no-permission", "&4&l❌ You lack the required permission for this action.");
	Message<?> FAILURE_NO_DROP               = message("failure.no-drop", "&4&l❌ &cYou are not allowed to drop your &eSkyMine Token&c.");
	Message<?> FAILURE_ON_RESET_COOLDOWN     = message("failure.on-reset-cooldown", "&4&l❌ &cThe cooldown on this &eSkyMine &cis &e{time}&c.");
	Message<?> FAILURE_ON_PICKUP_COOLDOWN    = message("failure.on-pickup-cooldown", "&4&l❌ &cDue to picking up a &eSkyMine&c, you must wait another &e{time}&c.");
	Message<?> FAILURE_ON_PLACEMENT_COOLDOWN = message("failure.on-placement-cooldown", "&4&l❌ &cThe cooldown to attempt &eSkyMine &cplacement is &e{time}&c.");
	Message<?> FAILURE_ALREADY_MAXED         = message("failure.already-maxed", "&4&l❌ &cThis &eSkyMine's &cupgrade is already maxed out.");
	Message<?> FAILURE_INVALID_MINE          = message("failure.invalid-mine", "&4&l❌ &cThe &eSkyMine &cidentifier '&e{id}&c' does not exist.");
	Message<?> FAILURE_INVALID_PLACEMENT     = message("failure.invalid-placement", "&4&l❌ &cYou are forbidden from placing down your &eSkyMine Token&c.");
	Message<?> FAILURE_NOT_LOADED            = message("failure.not-loaded", "&4&l❌ &cPlease wait to until all &eSkyMines &chave finished loading.");

	/**************************************************
	 *                  Usage Messages                *
	 **************************************************/
	Message<?> USAGE_SKYMINE = message("usage.skymine",
			"&7&l&m━━━━━━━━━━━━━━━━&7 &e&lSkyMine &aCommands &7&l&m━━━━━━━━━━━━━━━━",
			"&7- &a/sm list &e: &7Lists all mines.",
			"&7- &a/sm panel <id> &e: &7Opens the mine's main panel.",
			"&7- &a/sm upgrades <id> &e: &7Opens the mine's upgrade panel.",
			"&7- &a/sm home <id> &e: &7Teleports to the mine's home.",
			"&7- &a/sm sethome <id> &e: &7Sets the mine's home.",
			"&7- &a/sm reset <id> &e: &7Resets the mine.",
			"&7- &a/sm pickup <id> &e: &7Picks up the mine.",
			"&7&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
	);
	Message<?> USAGE_SKYMINE_PANEL = message("usage.skymine-panel",
			"&eUsage: &a/sm panel <id>",
			"&a► &7Opens up the specified mine's main panel."
	);
	Message<?> USAGE_SKYMINE_UPGRADES = message("usage.skymine-upgrades",
			"&eUsage: &a/sm upgrades <id>",
			"&a► &7Opens up the specified mine's upgrade panel."
	);
	Message<?> USAGE_SKYMINE_HOME = message("usage.skymine-home",
			"&eUsage: &a/sm home <id>",
			"&a► &7Teleports to the specified mine's home."
	);
	Message<?> USAGE_SKYMINE_SETHOME = message("usage.skymine-sethome",
			"&eUsage: &a/sm sethome <id>",
			"&a► &7Sets the specified mine's home."
	);
	Message<?> USAGE_SKYMINE_RESET = message("usage.skymine-reset",
			"&eUsage: &a/sm reset <id>",
			"&a► &7Resets the specified mine."
	);
	Message<?> USAGE_SKYMINE_PICKUP = message("usage.skymine-pickup",
			"&eUsage: &a/sm pickup <id>",
			"&a► &7Picks up the specified mine."
	);

	/**************************************************
	 *                  Admin Messages                *
	 **************************************************/
	Message<?> ADMIN_GENERAL_SKYMINE_LIST = message("admin.general.skymine-list",
			"&6Loaded SkyMines:",
			"&8- {skymines}"
	);
	Message<?> ADMIN_GENERAL_SKYMINE_LOOKUP_TOP = message("admin.general.skymine-lookup.top", "&8&l&m━━━━━━━━━━━━━━━━━━&7 &e&lSkyMine &cList &8&l&m━━━━━━━━━━━━━━━━━━");
	Message<?> ADMIN_GENERAL_SKYMINE_LOOKUP_REPEATING = message("admin.general.skymine-lookup.repeating", "&7→ [#{id}] &eLocated in '&a{world}&e' &eworld. &7(&a{x}x&7, &a{y}y&7, &a{z}z&7)");
	Message<?> ADMIN_GENERAL_SKYMINE_LOOKUP_BOTTOM = message("admin.general.skymine-lookup.bottom");
	Message<?> ADMIN_GENERAL_SKYMINE_DEBUG = message("admin.general.skymine-debug",
			"&8&l&m━━━━━━━━━━━━━━━&7 &e&lSkyMine &cDebug &8&l&m━━━━━━━━━━━━━━━",
			"&7Owner: &a{owner}",
			"&7Index: &a{index}",
			"&7Mine: {identifier} &7({enabled}&7)",
			"&7Border: &a{type}",
			"&7Size: &a{size}",
			"&7Upgrades: &a{upgrades}",
			"&7UUID: &a{uuid}",
			"&8&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
	);
	Message<?> ADMIN_SUCCESS_GIVE = message("admin.success.give", "&2&l✓ &aYou have given &e{amount}x {token} &ato &e{player}&a.");
	Message<?> ADMIN_SUCCESS_PANEL = message("admin.success.panel", "&2&l✓ &aYou have accessed &e{player}&a's &eSkyMine&a.");
	Message<?> ADMIN_SUCCESS_HOME      = message("admin.success.home", "&2&l✓ &aYou have been teleported to &e{player}&a's &eSkyMine&a.");
	Message<?> ADMIN_SUCCESS_RESET = message("admin.success.reset", "&2&l✓ &aYou have reset &e{player}&a's &eSkyMine&a.");
	Message<?> ADMIN_SUCCESS_PICKUP = message("admin.success.pickup", "&2&l✓ &aYou have picked up &e{player}&a's &eSkyMine&a.");
	Message<?> ADMIN_SUCCESS_REMOVE = message("admin.success.remove", "&2&l✓ &aYou have removed &e{player}&a's &eSkyMine&a.");
	Message<?> ADMIN_SUCCESS_RELOADED = message("admin.success.reloaded", "&2&l✓ &eSkyMines &ahas been reloaded.");
	Message<?> ADMIN_FAILURE_TOO_SMALL = message("admin.failure.too-small", "&4&l❌ &cThe &eSkyMine &cyou are trying to create is too small.");
	Message<?> ADMIN_FAILURE_TOO_BIG = message("admin.failure.too-big", "&4&l❌ &cSides are not permitted to exceed 100 blocks.");
	Message<?> ADMIN_FAILURE_NO_PLAYER_FOUND = message("admin.failure.no-player-found", "&4&l❌ &cThe player &e{player} &cdoes not exist.");
	Message<?> ADMIN_FAILURE_NO_SKYMINE_FOUND = message("admin.failure.no-skymine-found", "&4&l❌ &e{player} &cdoes not have a &eSkyMine &cwith ID '&e{id}&c'.");
	Message<?> ADMIN_FAILURE_NO_SKYMINES_FOUND = message("admin.failure.no-skymines-found", "&4&l❌ &e{player} &cdoes not have any &eSkyMines&c.");
	Message<?> ADMIN_FAILURE_INVALID_AMOUNT = message("admin.failure.invalid-amount", "&4&l❌ &cThe amount '&e{amount}&c' is an invalid amount.");
	Message<?> ADMIN_FAILURE_DEBUG_NO_MINE = message("admin.failure.debug-no-mine", "&4&l❌ &cYou are not looking at a valid &eSkyMine&c.");
	Message<?> ADMIN_USAGE_SKYMINE = message("admin.usage.skymine",
			"&8&l&m━━━━━━━━━━━━━&7 &e&lSkyMine &cAdmin Commands &8&l&m━━━━━━━━━━━━━",
			"&7- &c/sma list &e: &7Lists all enabled/disabled mines.",
			"&7- &c/sma give <player> <LxHxW> <amount> <material>",
			"&7- &c/sma lookup <player> &e: &7Lookup a player's mines.",
			"&7- &c/sma pickup <player> <id> &e: &7Pickup a player's mines.",
			"&7- &c/sma remove <player> <id> &e: &7Remove a player's mines.",
			"&7- &c/sma debug &e: &7Displays information for the selected mine.",
			"&7- &c/sma reload &e: &7Reloads config files.",
			"&8&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
	);
	Message<?> ADMIN_USAGE_SKYMINE_GIVE = message("admin.usage.skymine-give",
			"&eUsage: &c/sma give <player> <LxHxW> <amount> <material>",
			"&c► &7Gives the player mine tokens with the size and border."
	);
	Message<?> ADMIN_USAGE_SKYMINE_LOOKUP = message("admin.usage.skymine-lookup",
			"&eUsage: &c/sma lookup <player>",
			"&c► &7Lookup the specified player's list of mines."
	);
	Message<?> ADMIN_USAGE_SKYMINE_PICKUP = message("admin.usage.skymine-pickup",
			"&eUsage: &c/sma pickup <player> <id>",
			"&c► &7Pickup the specified player's mine."
	);
	Message<?> ADMIN_USAGE_SKYMINE_REMOVE = message("admin.usage.skymine-remove",
			"&eUsage: &c/sma remove <player> <id>",
			"&c► &7Remove the specified player's mine."
	);
}
