package com.github.colingrime.locale;

import com.github.colingrime.utils.Logger;
import com.github.colingrime.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Messages {

	// general messages
	LIST_SKYMINES_TOP_MESSAGE("general.list-skymines.top-message", "&aList of &eSkyMines&a:"),
	LIST_SKYMINES_REPEATING_MESSAGE("general.list-skymines.repeating-message", "&7→ [#%id%] &eClick to go home. &7(&a%x%x&7, &a%y%y&7, &a%z%z&7)"),
	RESET_COOLDOWN_FINISH("general.reset-cooldown-finish", "&aThe &eSkyMine &awith the ID &e%id% &ahas just finished cooling down!"),
	PICKUP_COOLDOWN_FINISH("general.pickup-cooldown-finish", "&aYou are free to place &eSkyMines &aagain."),

	// success messages
	SUCCESS_RECEIVE("success.receive", "&aYou have received &e%amount%x %token%&a!"),
	SUCCESS_UPGRADE("success.upgrade", "&aYou have upgraded &e%upgrade% &ato level &e%level%&a!"),
	SUCCESS_HOME("success.home", "&aYou have been teleported to your &eSkyMine&a!"),
	SUCCESS_SETHOME("success.sethome", "&aYou have successfully changed your &eSkyMine&a's home location!"),
	SUCCESS_RESET("success.reset", "&aThe &eSkyMine &ahas been reset!"),
	SUCCESS_PICKUP("success.pickup", "&aYou have successfully picked up your &eSkyMine&a!"),
	SUCCESS_PLACE("success.place", "&aYou have successfully placed your &eSkyMine&a!"),

	// failure messages
	FAILURE_MAX_AMOUNT("failure.max-amount", "&cYou have reached the maximum amount of &eSkyMines &cyou can place."),
	FAILURE_TOO_FAR_AWAY("failure.too-far-away", "&cYou are too far away from your &eSkyMine &cto set its home."),
	FAILURE_NO_SPACE("failure.no-space", "&cThere is no space for the &eSkyMine &cto be placed here."),
	FAILURE_NO_INVENTORY_SPACE("failure.no-inventory-space", "&cThere is no space in your inventory to get the &eSkyMine Token&c."),
	FAILURE_NO_FUNDS("failure.no-funds", "&cYou don't have enough money to buy this upgrade."),
	FAILURE_NO_SKYMINE("failure.no-skymine", "&cYou don't have a &eSkyMine &cwith the ID: &e%id%&c."),
	FAILURE_NO_SKYMINES("failure.no-skymines", "&cYou don't have any &eSkyMines&c."),
	FAILURE_NO_PERMISSION("failure.no-permission", "&cYou do not have permission to perform this command."),
	FAILURE_NO_DROP("failure.no-drop", "&cYou are not allowed to drop your &eSkyMine Token&c."),
	FAILURE_ON_RESET_COOLDOWN("failure.on-reset-cooldown", "&cThe cooldown on the &eSkyMine &cis &e%time%&c."),
	FAILURE_ON_PICKUP_COOLDOWN("failure.on-pickup-cooldown", "&cDue to picking up a &eSkyMine&c, you must wait &e%time% &cto place another."),
	FAILURE_ON_PLACEMENT_COOLDOWN("failure.on-placement-cooldown", "&cThe cooldown to attempt &eSkyMine &cplacement is &e%time%&c."),
	FAILURE_ALREADY_MAXED("failure.already-maxed", "&cThis &eSkyMine&c's upgrade is already maxed out!"),
	FAILURE_INVALID_SENDER("failure.invalid-sender", "&cYou must be a player to perform this command."),
	FAILURE_INVALID_PLACEMENT("failure.invalid-placement", "&cYou can't place down a &eSkyMine Token&c."),

	// usage messages
	USAGE_SKYMINES_COMMAND("usage.skymines-command", "&eSkyMine &aCommands:",
			"&7→ &a/sm list &7- lists your &eSkyMines&7.",
			"&7→ &a/sm panel [id] &7- open the &eSkyMine&7's main panel.",
			"&7→ &a/sm home [id] &7- go to the &eSkyMine&7.",
			"&7→ &a/sm sethome [id] &7- set the &eSkyMine&7's home.",
			"&7→ &a/sm reset [id] &7- reset the &eSkyMine&7.",
			"&7→ &a/sm upgrades [id] &7- open the &eSkyMine&7's upgrade panel.",
			"&7→ &a/sm pickup [id] &7- pickup the &eSkyMine&7."),
	USAGE_SKYMINES_LIST("usage.skymines-list", "&7Usage: &a/sm list &7→ lists your &eSkyMines&7."),
	USAGE_SKYMINES_PANEL("usage.skymines-panel", "&7Usage: &a/sm panel [id] &7→ open the &eSkyMine&7's main panel."),
	USAGE_SKYMINES_HOME("usage.skymines-home", "&7Usage: &a/sm home [id] &7→ go to the &eSkyMine&7."),
	USAGE_SKYMINES_SETHOME("usage.skymines-sethome", "&7Usage: &a/sm sethome [id] &7→ set the &eSkyMine&7's home."),
	USAGE_SKYMINES_RESET("usage.skymines-reset", "&7Usage: &a/sm reset [id] &7→ reset the &eSkyMine&7."),
	USAGE_SKYMINES_UPGRADES("usage.skymines-upgrades", "&7Usage: &a/sm upgrades [id] &7→ open the &eSkyMine&7's upgrade panel."),
	USAGE_SKYMINES_PICKUP("usage.skymines-pickup", "&7Usage: &a/sm pickup [id] &7→ pickup the &eSkyMine&7."),

	// admin messages
	LOOKUP_SKYMINES_TOP_MESSAGE("admin.general.lookup-skymines.top-message", "&aList of &eSkyMines &aowned by &e%player%&a:"),
	LOOKUP_SKYMINES_REPEATING_MESSAGE("admin.general.lookup-skymines.repeating-message", "&7→ [#%id%] &eThis home is located in the &a%world% &eworld. &7(&a%x%x&7, &a%y%y&7, &a%z%z&7)"),
	SUCCESS_GIVE("admin.success.give", "&aYou have given &e%amount%x %token% &ato &e%player%&a!"),
	SUCCESS_PICKUP_ADMIN("admin.success.pickup", "&aYou have successfully picked up &e%player%&a's &eSkyMine&a."),
	SUCCESS_REMOVE("admin.success.remove", "&aYou have successfully removed &e%player%&a's &eSkyMine&a."),
	SUCCESS_RELOADED("admin.success.reloaded", "&eSkyMines &ahas been reloaded!"),
	FAILURE_TOO_SMALL("admin.failure.too-small", "&cThe &eSkyMine &cyou are trying to create is too small."),
	FAILURE_TOO_BIG("admin.failure.too-big", "&cFor performance reasons, a side is not permitted to exceed 100 blocks."),
	FAILURE_NO_PLAYER_FOUND("admin.failure.no-player-found", "&cThe player &e%player% &cdoes not exist."),
	FAILURE_NO_SKYMINE_FOUND("admin.failure.no-skymine-found", "&e%player% &cdoes not have a &eSkyMine &cwith the ID: &e%id%&c."),
	FAILURE_NO_SKYMINES_FOUND("admin.failure.no-skymines.found", "&e%player% &cdoes not have any &eSkyMines&e."),
	USAGE_SKYMINES_ADMIN_COMMAND("admin.usage.skymines-admin-command", "&eSkyMine &cAdmin Commands:",
			"&7→ &c/sma give [player] {LxHxW} {amount} &7- give away &eSkyMine Tokens&7.",
			"&7→ &c/sma lookup [player] &7- lookup a player's &eSkyMine&7's list.",
			"&7→ &c/sma pickup [player] &7- pickup a player's &eSkyMine&7.",
			"&7→ &c/sma remove [player] &7- remove a player's &eSkyMine&7.",
			"&7→ &c/sma reload &7- reloads the YAML files."),
	USAGE_SKYMINES_GIVE("admin.usage.skymines-give", "&7Usage &c/sma give [player] {LxHxW} {amount} &7→ give away &eSkyMine Tokens&7."),
	USAGE_SKYMINES_LOOKUP("admin.usage.skymines-lookup", "&7Usage &c/sma lookup [player] &7→ lookup a player's &eSkyMine&7's list."),
	USAGE_SKYMINES_PICKUP_ADMIN("admin.usage.skymines-pickup", "&7Usage &c/sma pickup [player] &7→ pickup a player's &eSkyMine&7."),
	USAGE_SKYMINES_REMOVE("admin.usage.skymines-remove", "&7Usage &c/sma remove [player] &7→ remove a player's &eSkyMine&7."),
	USAGE_SKYMINES_RELOAD("admin.usage.skymines-reload", "&7Usage &c/sma reload &7→ reloads the YAML files.")
	;

	private static File file;
	private static FileConfiguration config;

	private final String path;
	private List<String> messages;

	Messages(String path, String...messages) {
		this.path = path;
		this.messages = Arrays.asList(messages);
	}

	public static void init(JavaPlugin plugin) {
		file = new File(plugin.getDataFolder(), "messages.yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource("messages.yml", false);
		}
	}

	public static void reload() {
		config = YamlConfiguration.loadConfiguration(file);
		Arrays.stream(Messages.values()).forEach(Messages::update);
	}

	public void update() {
		if (!config.getStringList(path).isEmpty()) {
			messages = Utils.color(config.getStringList(path));
		} else if (config.getString(path) != null) {
			messages = Collections.singletonList(Utils.color(config.getString(path)));
		} else {
			Logger.log("Messages path \"" + path + "\" has failed to load (using default value).");
			messages = Utils.color(messages);
		}
	}

	public void sendTo(CommandSender sender) {
		if (messages.isEmpty()) {
			return;
		}

		messages.forEach(sender::sendMessage);
	}

	public void sendTo(CommandSender sender, Replacer replacer) {
		if (messages.isEmpty()) {
			return;
		}

		replacer.replace(messages).forEach(sender::sendMessage);
	}

	@Override
	public String toString() {
		return String.join("\n", messages);
	}
}
