package com.github.colingrime.commands.skyminesadmin.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.locale.Messages;
import org.bukkit.command.CommandSender;

public record ReloadSubCommand(SkyMines plugin) implements SubCommand {

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        plugin.reload();
        Messages.SUCCESS_RELOADED.sendTo(sender);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public Messages getUsage() {
        return Messages.SUCCESS_RELOADED;
    }

    @Override
    public String getPermission() {
        return "skymines.admin.reload";
    }
}
