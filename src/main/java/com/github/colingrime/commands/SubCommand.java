package com.github.colingrime.commands;

import com.github.colingrime.locale.Messages;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public interface SubCommand {

    void onCommand(CommandSender sender, String[] args);

    default ArrayList<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    /**
     * @return subcommand name
     */
    String getName();

    /**
     * @return usage message for the subcommand
     */
    Messages getUsage();

    /**
     * @return permission to use the subcommand
     */
    default String getPermission() {
        return null;
    }

    /**
     * @return optional aliases of the subcommand
     */
    default String[] getAliases() {
        return new String[]{};
    }

    /**
     * If arguments are required, the usage of the
     * subcommand will be displayed if there are
     * not enough arguments inputted.
     *
     * @return amount of arguments required
     */
    default int getArgumentsRequired() {
        return 0;
    }

    /**
     * If player senders are required, the console
     * will be sent an invalid sender message if
     * they attempt to run the command.
     *
     * @return true if player sender should be required
     */
    default boolean requirePlayer() {
        return false;
    }
}
