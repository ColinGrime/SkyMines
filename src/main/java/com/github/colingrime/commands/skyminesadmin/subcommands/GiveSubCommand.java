package com.github.colingrime.commands.skyminesadmin.subcommands;

import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.locale.Replacer;
import com.github.colingrime.skymines.structure.MineSize;
import com.github.colingrime.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveSubCommand implements SubCommand {

    private final SkyMines plugin;

    public GiveSubCommand(SkyMines plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player receiver = Bukkit.getPlayer(args[0]);
        if (receiver == null) {
            Messages.FAILURE_NO_PLAYER_FOUND.sendTo(sender, new Replacer("%player%", args[0]));
            return;
        }

        MineSize size = new MineSize(10, 10, 10);
        if (args.length >= 2) {
            String sizeString = args[1];
            String[] sizeStringArray = sizeString.split("x");
            if (!isSizeValid(sizeStringArray)) {
                getUsage().sendTo(sender);
                return;
            }

            int length = Integer.parseInt(sizeStringArray[0]);
            int height = Integer.parseInt(sizeStringArray[1]);
            int width = Integer.parseInt(sizeStringArray[2]);
            if (length <= 1 || height <= 1 || width <= 1) {
                Messages.FAILURE_TOO_SMALL.sendTo(sender);
                return;
            } else if (length > 100 || height > 100 || width > 100) {
                Messages.FAILURE_TOO_BIG.sendTo(sender);
                return;
            }

            size = new MineSize(length, height, width);
        }

        int amount = 1;
        if (args.length >= 3) {
            if (!isInt(args[2])) {
                Messages.FAILURE_INVALID_AMOUNT.sendTo(sender, new Replacer("%amount%", args[2]));
                return;
            }

            amount = Integer.parseInt(args[2]);
        }

        Material borderType = args.length >= 4 ? Material.matchMaterial(args[3]) : Material.BEDROCK;
        if (borderType == null || !borderType.isBlock()) {
            Messages.FAILURE_INVALID_MATERIAL.sendTo(sender, new Replacer("%material%", args[3]));
            return;
        }

        // gets the specified amount of tokens and gives it to the player
        ItemStack item = plugin.getSkyMineManager().getToken().getToken(size, borderType);
        item.setAmount(amount);
        PlayerUtils.giveItemOrDrop(receiver, item);

        // gets the name of the item
        ItemMeta meta = item.getItemMeta();
        String name = "Name not loaded.";
        if (meta != null) {
            name = meta.getDisplayName();
        }

        // messages
        Replacer replacer = new Replacer("%token%", name).add("%amount%", item.getAmount());
        Messages.SUCCESS_GIVE.sendTo(sender, replacer.add("%player%", receiver.getName()));
        Messages.SUCCESS_RECEIVE.sendTo(receiver, replacer);
    }

    private boolean isSizeValid(String[] sizeStringArray) {
        if (sizeStringArray.length != 3) {
            return false;
        }
        return isInt(sizeStringArray[0]) && isInt(sizeStringArray[1]) && isInt(sizeStringArray[2]);
    }

    private boolean isInt(String string) {
        return string.matches("\\d+");
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public Messages getUsage() {
        return Messages.USAGE_SKYMINES_GIVE;
    }

    @Override
    public String getPermission() {
        return "skymines.admin.give";
    }

    @Override
    public int getArgumentsRequired() {
        return 1;
    }
}
