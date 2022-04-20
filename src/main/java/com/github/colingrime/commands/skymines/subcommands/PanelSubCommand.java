package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.skymines.SkyMinesSubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.panel.MainPanel;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PanelSubCommand extends SkyMinesSubCommand {

    private final SkyMines plugin;

    public PanelSubCommand(SkyMines plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args, SkyMine skyMine) {
        Player player = (Player) sender;
        new MainPanel(plugin, player, skyMine).openInventory(player);
    }

    @Override
    public boolean requireSkyMine() {
        return true;
    }

    @Override
    public String getName() {
        return "panel";
    }

    @Override
    public Messages getUsage() {
        return Messages.USAGE_SKYMINES_PANEL;
    }
}
