package com.github.colingrime.commands;

import com.github.colingrime.locale.Messages;
import com.github.colingrime.utils.Logger;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class BaseCommand implements CommandExecutor, TabExecutor {

	private final Map<String, SubCommand> subCommands = new HashMap<>();

	public BaseCommand(JavaPlugin plugin, String name) {
		PluginCommand command = plugin.getCommand(name);

		// disable plugin if a command is invalid
		if (command == null) {
			Logger.severe("Commands have failed to load. Plugin has been disabled.");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		// if there's no subcommand input, send the command usage
		if (args.length == 0 || !getSubCommand(args[0]).isPresent()) {
			getUsage().sendTo(sender);
			return true;
		}

		SubCommand subCommand = getSubCommand(args[0]).get();
		String permission = subCommand.getPermission();

		// if the console is both disabled and the sender, it's invalid
		if (subCommand.requirePlayer() && !(sender instanceof Player)) {
			Messages.FAILURE_INVALID_SENDER.sendTo(sender);
			return true;
		}

		// sender doesn't have permission
		if (permission != null && !sender.hasPermission(permission)) {
			Messages.FAILURE_NO_PERMISSION.sendTo(sender);
			return true;
		}

		String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
		if (subCommand.requireArguments() && subArgs.length == 0) {
			subCommand.getUsage().sendTo(sender);
			return true;
		}

		// subcommand runs
		subCommand.onCommand(sender, args[0], subArgs);
		return true;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		if (args.length == 1) {
			return new ArrayList<>(subCommands.keySet());
		}

		SubCommand subCommand = subCommands.get(args[0]);
		if (subCommand == null) {
			return null;
		}

		return subCommand.onTabComplete(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
	}

	/**
	 * Registers a subcommand.
	 *
	 * When a base command is called, it attempts to
	 * match its first argument to the names/aliases
	 * of the cached subcommands.
	 *
	 * @param subCommand any subcommand
	 */
	public void registerSubCommand(SubCommand subCommand) {
		// register subcommand with name
		if (subCommand.getName() != null) {
			subCommands.put(subCommand.getName().toLowerCase(), subCommand);
		}

		// register subcommand with aliases
		if (subCommand.getAliases() != null) {
			for (String alias : subCommand.getAliases()) {
				subCommands.put(alias.toLowerCase(), subCommand);
			}
		}
	}

	/**
	 * @param subCommandString name/alias of a subcommand
	 * @return subcommand wrapped in an Optional object
	 */
	public Optional<SubCommand> getSubCommand(String subCommandString) {
		return Optional.ofNullable(subCommands.get(subCommandString.toLowerCase()));
	}

	/**
	 * @return command usage as configured in the config.yml
	 */
	public abstract Messages getUsage();
}
