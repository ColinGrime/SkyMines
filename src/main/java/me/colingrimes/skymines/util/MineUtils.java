package me.colingrimes.skymines.util;

import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.bukkit.Players;
import me.colingrimes.skymines.skymine.SkyMine;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class MineUtils {

	/**
	 * Applies all supported placeholders for the skymine {@link Message}.
	 *
	 * @param message the message
	 * @param skyMine the skymine to get the placeholder info
	 * @return the new message with replaced placeholders
	 */
	@Nonnull
	public static Message<?> placeholders(@Nonnull Message<?> message, @Nullable SkyMine skyMine) {
		if (skyMine == null) {
			return message;
		}
		return message
				.replace("{id}", skyMine.getIndex())
				.replace("{index}", skyMine.getIndex())
				.replace("{name}", Objects.requireNonNullElse(skyMine.getName(), "&8Unknown"))
				.replace("{label}", Objects.requireNonNullElse(skyMine.getName(), "#" + skyMine.getIndex()))
				.replace("{owner}", Players.getName(skyMine.getOwner()))
				.replace("{player}", Players.getName(skyMine.getOwner()))
				.replace("{enabled}", skyMine.isEnabled() ? "&2Enabled" : "&cDisabled")
				.replace("{identifier}", (skyMine.isEnabled() ? "&a" : "&c") + skyMine.getIdentifier())
				.replace("{x}", skyMine.getHome().getBlockX())
				.replace("{y}", skyMine.getHome().getBlockY())
				.replace("{z}", skyMine.getHome().getBlockZ())
				.replace("{world}", skyMine.getHome().getWorld().getName());
	}

	/**
	 * Checks if the specified material is a valid material for the border.
	 *
	 * @param material the material
	 * @return true if the material is valid
	 */
	public static boolean isValidBorderType(@Nullable Material material) {
		return material != null
				&& material.isBlock()
				&& material.isSolid()
				&& !material.hasGravity()
				&& !material.isAir();
	}

	private MineUtils() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
