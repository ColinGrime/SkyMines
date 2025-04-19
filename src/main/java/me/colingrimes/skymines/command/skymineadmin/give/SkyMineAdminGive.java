package me.colingrimes.skymines.command.skymineadmin.give;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.geometry.Size;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.bukkit.Inventories;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Messages;
import me.colingrimes.skymines.config.Mines;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SkyMineAdminGive implements Command<SkyMines> {

	@Override
	public void execute(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Optional<Player> receiver = args.getPlayer(0);
		if (receiver.isEmpty()) {
			Messages.ADMIN_FAILURE_MISC_NO_PLAYER_FOUND.replace("{player}", args.getFirst()).send(sender);
			return;
		}

		Mines.Mine mine = Mines.MINES.get().get("default");
		if (args.size() >= 2) {
			mine = Mines.MINES.get().get(args.get(1));
		}

		if (mine == null) {
			Messages.FAILURE_SKYMINE_INVALID_IDENTIFIER.replace("{id}", args.getOrDefault(1, "default")).send(sender);
			return;
		}

		Size size = null;
		if (args.size() >= 3) {
			size = Size.of(args.get(2));
			if (size == null) {
				Messages.ADMIN_USAGE_SKYMINE_GIVE.send(sender);
				return;
			}
			if (size.getLength() < 1 || size.getHeight() < 1 || size.getWidth() < 1) {
				Messages.ADMIN_FAILURE_GIVE_TOO_SMALL.send(sender);
				return;
			}
			if (size.getLength() > 100 || size.getHeight() > 100 || size.getWidth() > 100) {
				Messages.ADMIN_FAILURE_GIVE_TOO_BIG.send(sender);
				return;
			}
		}

		// Set the default mine size if none is specified.
		if (size == null) {
			size = mine.getDefaultSize();
		}

		int amount = 1;
		if (args.size() >= 4) {
			if (args.getInt(3).isEmpty()) {
				Messages.ADMIN_FAILURE_GIVE_INVALID_AMOUNT.replace("{amount}", args.get(2)).send(sender);
				return;
			}
			amount = args.getInt(3).get();
		}

		// Gives the specified amount of tokens to the player.
		ItemStack token = plugin.getSkyMineManager().getToken().getToken(mine.getIdentifier(), size);
		token.setAmount(amount);
		Inventories.give(receiver.get(), token, true);

		// Sets the name of the token.
		ItemMeta meta = token.getItemMeta();
		String name = "Name not loaded.";
		if (meta != null) {
			name = meta.getDisplayName();
		}

		// Send messages.
		Placeholders placeholders = Placeholders.of("{token}", name).add("{amount}", token.getAmount());
		Messages.SUCCESS_RECEIVE.replace(placeholders).send(receiver.get());
		if (!sender.isPlayer() || !sender.player().equals(receiver.get())) {
			Messages.ADMIN_SUCCESS_GIVE.replace(placeholders).replace("{player}", receiver.get().getName()).send(sender);
		}
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull SkyMines plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Players.filter(p -> p.getName().startsWith(args.getFirst())).map(Player::getName).toList();
		} else if (args.size() == 2) {
			return Mines.MINES.get().keySet().stream().map(String::toLowerCase).filter(token -> token.contains(args.getLowercase(1))).toList();
		} else {
			return null;
		}
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setUsage(Messages.ADMIN_USAGE_SKYMINE_GIVE);
		properties.setPermission("skymines.admin.give");
		properties.setArgumentsRequired(1);
	}
}
