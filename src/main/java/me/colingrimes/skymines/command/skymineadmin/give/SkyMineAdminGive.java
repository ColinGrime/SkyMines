package me.colingrimes.skymines.command.skymineadmin.give;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.bukkit.Inventories;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.skymine.structure.MineSize;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SkyMineAdminGive implements Command<SkyMines> {

	private static final Set<Material> validBlocks = Arrays.stream(Material.values())
			.filter(Material::isBlock)
			.filter(Material::isSolid)
			.filter(m -> !m.hasGravity())
			.filter(m -> !m.isAir())
			.collect(Collectors.toSet());

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<Player> receiver = args.getPlayer(0);
		if (receiver.isEmpty()) {
			Messages.FAILURE_NO_PLAYER_FOUND.replace("{player}", args.get(0)).send(sender);
			return;
		}

		MineSize size = new MineSize(10, 10, 10);
		if (args.size() >= 2) {
			String sizeString = args.get(1);
			String[] sizeStringArray = sizeString.split("x");
			if (!isSizeValid(sizeStringArray)) {
				Messages.USAGE_SKYMINES_GIVE.send(sender);
				return;
			}

			int length = Integer.parseInt(sizeStringArray[0]);
			int height = Integer.parseInt(sizeStringArray[1]);
			int width = Integer.parseInt(sizeStringArray[2]);
			if (length <= 1 || height <= 1 || width <= 1) {
				Messages.FAILURE_TOO_SMALL.send(sender);
				return;
			} else if (length > 100 || height > 100 || width > 100) {
				Messages.FAILURE_TOO_BIG.send(sender);
				return;
			}

			size = new MineSize(length, height, width);
		}

		int amount = 1;
		if (args.size() >= 3) {
			if (args.getInt(2).isEmpty()) {
				Messages.FAILURE_INVALID_AMOUNT.replace("{amount}", args.get(2)).send(sender);
				return;
			}
			amount = args.getInt(2).get();
		}

		Material borderType = args.size() >= 4 ? Material.matchMaterial(args.get(3)) : Material.BEDROCK;
		if (borderType == null || !validBlocks.contains(borderType)) {
			Messages.FAILURE_INVALID_MATERIAL.replace("{material}", args.get(3)).send(sender);
			return;
		}

		// gets the specified amount of tokens and gives it to the player
		ItemStack item = plugin.getSkyMineManager().getToken().getToken(size, borderType);
		item.setAmount(amount);
		Inventories.give(receiver.get(), item, true);

		// gets the name of the item
		ItemMeta meta = item.getItemMeta();
		String name = "Name not loaded.";
		if (meta != null) {
			name = meta.getDisplayName();
		}

		// messages
		Placeholders placeholders = Placeholders.of("{token}", name).add("{amount}", item.getAmount());
		Messages.SUCCESS_RECEIVE.replace(placeholders).send(receiver.get());
		if (!sender.isPlayer() || !sender.player().equals(receiver.get())) {
			Messages.SUCCESS_GIVE.replace(placeholders).replace("{player}", receiver.get().getName()).send(sender);
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Players.filter(p -> p.getName().startsWith(args.get(0))).map(Player::getName).toList();
		} else if (args.size() == 4) {
			return validBlocks.stream().map(m -> m.name().toLowerCase()).filter(name -> name.startsWith(args.getLowercase(3))).toList();
		} else {
			return null;
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.USAGE_SKYMINES_GIVE);
		properties.setPermission("skymines.admin.give");
		properties.setArgumentsRequired(1);
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
}
