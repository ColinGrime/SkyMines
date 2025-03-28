package me.colingrimes.skymines.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.message.Message;

import static me.colingrimes.midnight.config.option.OptionFactory.message;

@Configuration("messages.yml")
public interface Messages {

	// general messages
	Message<?> LIST_SKYMINES_TOP_MESSAGE = message("general.list-skymines.top-message", "&aList of &eSkyMines&a:");
	Message<?> LIST_SKYMINES_REPEATING_MESSAGE = message("general.list-skymines.repeating-message", "&7→ [#{id}] &eClick to go home. &7(&a{x}x&7, &a{y}y&7, &a{z}z&7)");
	Message<?> RESET_COOLDOWN_FINISH = message("general.reset-cooldown-finish", "&aThe &eSkyMine &awith the ID &e{id} &ahas just finished cooling down!");
	Message<?> PICKUP_COOLDOWN_FINISH = message("general.pickup-cooldown-finish", "&aYou are free to place &eSkyMines &aagain.");

	// success messages
	Message<?> SUCCESS_RECEIVE = message("success.receive", "&aYou have received &e{amount}x {token}&a!");
	Message<?> SUCCESS_UPGRADE = message("success.upgrade", "&aYou have upgraded &e{upgrade} &ato level &e{level}&a!");
	Message<?> SUCCESS_HOME = message("success.home", "&aYou have been teleported to your &eSkyMine&a!");
	Message<?> SUCCESS_SETHOME = message("success.sethome", "&aYou have successfully changed your &eSkyMine&a's home location!");
	Message<?> SUCCESS_RESET = message("success.reset", "&aThe &eSkyMine &ahas been reset!");
	Message<?> SUCCESS_PICKUP = message("success.pickup", "&aYou have successfully picked up your &eSkyMine&a!");
	Message<?> SUCCESS_PLACE = message("success.place", "&aYou have successfully placed your &eSkyMine&a!");

	// failure messages
	Message<?> FAILURE_MAX_AMOUNT = message("failure.max-amount", "&cYou have reached the maximum amount of &eSkyMines &cyou can place.");
	Message<?> FAILURE_TOO_FAR_AWAY = message("failure.too-far-away", "&cYou are too far away from your &eSkyMine &cto set its home.");
	Message<?> FAILURE_NO_SPACE = message("failure.no-space", "&cThere is no space for the &eSkyMine &cto be placed here.");
	Message<?> FAILURE_NO_INVENTORY_SPACE = message("failure.no-inventory-space", "&cThere is no space in your inventory to get the &eSkyMine Token&c.");
	Message<?> FAILURE_NO_FUNDS = message("failure.no-funds", "&cYou don't have enough money to buy this upgrade.");
	Message<?> FAILURE_NO_SKYMINE = message("failure.no-skymine", "&cYou don't have a &eSkyMine &cwith the ID: &e{id}&c.");
	Message<?> FAILURE_NO_SKYMINES = message("failure.no-skymines", "&cYou don't have any &eSkyMines&c.");
	Message<?> FAILURE_NO_PERMISSION = message("failure.no-permission", "&cYou do not have permission to perform this command.");
	Message<?> FAILURE_NO_DROP = message("failure.no-drop", "&cYou are not allowed to drop your &eSkyMine Token&c.");
	Message<?> FAILURE_ON_RESET_COOLDOWN = message("failure.on-reset-cooldown", "&cThe cooldown on the &eSkyMine &cis &e{time}&c.");
	Message<?> FAILURE_ON_PICKUP_COOLDOWN = message("failure.on-pickup-cooldown", "&cDue to picking up a &eSkyMine&c, you must wait &e{time} &cto place another.");
	Message<?> FAILURE_ON_PLACEMENT_COOLDOWN = message("failure.on-placement-cooldown", "&cThe cooldown to attempt &eSkyMine &cplacement is &e{time}&c.");
	Message<?> FAILURE_ALREADY_MAXED = message("failure.already-maxed", "&cThis &eSkyMine&c's upgrade is already maxed out!");
	Message<?> FAILURE_INVALID_SENDER = message("failure.invalid-sender", "&cYou must be a player to perform this command.");
	Message<?> FAILURE_INVALID_PLACEMENT = message("failure.invalid-placement", "&cYou can't place down a &eSkyMine Token&c.");

	// usage messages
	Message<?> USAGE_SKYMINES_COMMAND = message("usage.skymines-command", "&eSkyMine &aCommands:",
			"&7→ &a/sm list &7- lists your &eSkyMines&7.",
			"&7→ &a/sm panel [id] &7- open the &eSkyMine&7's main panel.",
			"&7→ &a/sm home [id] &7- go to the &eSkyMine&7.",
			"&7→ &a/sm sethome [id] &7- set the &eSkyMine&7's home.",
			"&7→ &a/sm reset [id] &7- reset the &eSkyMine&7.",
			"&7→ &a/sm upgrades [id] &7- open the &eSkyMine&7's upgrade panel.",
			"&7→ &a/sm pickup [id] &7- pickup the &eSkyMine&7.");
	Message<?> USAGE_SKYMINES_LIST = message("usage.skymines-list", "&7Usage: &a/sm list &7→ lists your &eSkyMines&7.");
	Message<?> USAGE_SKYMINES_PANEL = message("usage.skymines-panel", "&7Usage: &a/sm panel [id] &7→ open the &eSkyMine&7's main panel.");
	Message<?> USAGE_SKYMINES_HOME = message("usage.skymines-home", "&7Usage: &a/sm home [id] &7→ go to the &eSkyMine&7.");
	Message<?> USAGE_SKYMINES_SETHOME = message("usage.skymines-sethome", "&7Usage: &a/sm sethome [id] &7→ set the &eSkyMine&7's home.");
	Message<?> USAGE_SKYMINES_RESET = message("usage.skymines-reset", "&7Usage: &a/sm reset [id] &7→ reset the &eSkyMine&7.");
	Message<?> USAGE_SKYMINES_UPGRADES = message("usage.skymines-upgrades", "&7Usage: &a/sm upgrades [id] &7→ open the &eSkyMine&7's upgrade panel.");
	Message<?> USAGE_SKYMINES_PICKUP = message("usage.skymines-pickup", "&7Usage: &a/sm pickup [id] &7→ pickup the &eSkyMine&7.");

	// admin messages
	Message<?> LOOKUP_SKYMINES_TOP_MESSAGE = message("admin.general.lookup-skymines.top-message", "&aList of &eSkyMines &aowned by &e{player}&a:");
	Message<?> LOOKUP_SKYMINES_REPEATING_MESSAGE = message("admin.general.lookup-skymines.repeating-message", "&7→ [#{id}] &eThis home is located in the &a{world} &eworld. &7(&a{x}x&7, &a{y}y&7, &a{z}z&7)");
	Message<?> SUCCESS_PANEL = message("admin.success.panel", "&aYou have accessed another player's &eSkyMine&a.");
	Message<?> SUCCESS_GIVE = message("admin.success.give", "&aYou have given &e{amount}x {token} &ato &e{player}&a!");
	Message<?> SUCCESS_PICKUP_ADMIN = message("admin.success.pickup", "&aYou have successfully picked up &e{player}&a's &eSkyMine&a.");
	Message<?> SUCCESS_REMOVE = message("admin.success.remove", "&aYou have successfully removed &e{player}&a's &eSkyMine&a.");
	Message<?> SUCCESS_RELOADED = message("admin.success.reloaded", "&eSkyMines &ahas been reloaded!");
	Message<?> FAILURE_TOO_SMALL = message("admin.failure.too-small", "&cThe &eSkyMine &cyou are trying to create is too small.");
	Message<?> FAILURE_TOO_BIG = message("admin.failure.too-big", "&cFor performance reasons, a side is not permitted to exceed 100 blocks.");
	Message<?> FAILURE_NO_PLAYER_FOUND = message("admin.failure.no-player-found", "&cThe player &e{player} &cdoes not exist.");
	Message<?> FAILURE_NO_SKYMINE_FOUND = message("admin.failure.no-skymine-found", "&e{player} &cdoes not have a &eSkyMine &cwith the ID: &e{id}&c.");
	Message<?> FAILURE_NO_SKYMINES_FOUND = message("admin.failure.no-skymines-found", "&e{player} &cdoes not have any &eSkyMines&e.");
	Message<?> FAILURE_INVALID_AMOUNT = message("admin.failure.invalid-amount", "&cThe amount &e{amount} &cis an invalid amount.");
	Message<?> FAILURE_INVALID_MATERIAL = message("admin.failure.invalid-material", "&cThe material &e{material} &cis an invalid border type.");
	Message<?> USAGE_SKYMINES_ADMIN_COMMAND = message("admin.usage.skymines-admin-command", "&eSkyMine &cAdmin Commands:",
			"&7→ &c/sma give [player] {LxHxW} {amount} &7- give away &eSkyMine Tokens&7.",
			"&7→ &c/sma lookup [player] &7- lookup a player's &eSkyMine&7's list.",
			"&7→ &c/sma pickup [player] &7- pickup a player's &eSkyMine&7.",
			"&7→ &c/sma remove [player] &7- remove a player's &eSkyMine&7.",
			"&7→ &c/sma reload &7- reloads the YAML files.");
	Message<?> USAGE_SKYMINES_GIVE = message("admin.usage.skymines-give", "&7Usage &c/sma give [player] {LxHxW} {amount} &7→ give away &eSkyMine Tokens&7.");
	Message<?> USAGE_SKYMINES_LOOKUP = message("admin.usage.skymines-lookup", "&7Usage &c/sma lookup [player] &7→ lookup a player's &eSkyMine&7's list.");
	Message<?> USAGE_SKYMINES_PICKUP_ADMIN = message("admin.usage.skymines-pickup", "&7Usage &c/sma pickup [player] &7→ pickup a player's &eSkyMine&7.");
	Message<?> USAGE_SKYMINES_REMOVE = message("admin.usage.skymines-remove", "&7Usage &c/sma remove [player] &7→ remove a player's &eSkyMine&7.");
	Message<?> USAGE_SKYMINES_RELOAD = message("admin.usage.skymines-reload", "&7Usage &c/sma reload &7→ reloads the YAML files.");
}
