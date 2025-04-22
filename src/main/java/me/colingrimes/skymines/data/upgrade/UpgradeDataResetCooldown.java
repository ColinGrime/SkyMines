package me.colingrimes.skymines.data.upgrade;

import me.colingrimes.midnight.config.util.Configs;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.text.Parser;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.config.Menus;
import me.colingrimes.skymines.data.UpgradeData;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpgradeDataResetCooldown extends UpgradeData {

	private final Map<Integer, Duration> resetCooldown;

	/**
	 * Constructs the {@link UpgradeDataResetCooldown} with the given configuration section.
	 * It will also validate the given data and will return {@code null} if it's invalid.
	 *
	 * @param section the configuration section
	 * @return the reset cooldown data or null if invalid
	 */
	@Nullable
	public static UpgradeDataResetCooldown of(@Nonnull ConfigurationSection section) {
		UpgradeDataResetCooldown data = new UpgradeDataResetCooldown(section);
		for (int i=1; i<=Math.max(1, data.getMaxLevel()); i++) {
			if (!data.resetCooldown.containsKey(i) || data.resetCooldown.get(i) == null) {
				return null;
			}
		}
		return data;
	}

	private UpgradeDataResetCooldown(@Nonnull ConfigurationSection section) {
		super(section);
		this.resetCooldown = Configs.mapIntegerKeys(section, sec -> Parser.parseDuration(sec.getString("upgrade")));
	}

	@Nonnull
	@Override
	public List<String> getLore(int level) {
		List<String> lores = new ArrayList<>();
		for (int i=1; i<=getMaxLevel(); i++) {
			Placeholders placeholders = Placeholders.of("{level}", i).add("{time}", Text.formatApprox(resetCooldown.get(i)));
			if (i == level + 1 || (i == level && level == getMaxLevel())) {
				String next = Menus.UPGRADE_MENU_RESET_COOLDOWN.get().getLoreNext();
				lores.add(placeholders.apply(next).toText());
			} else {
				String lore = Menus.UPGRADE_MENU_RESET_COOLDOWN.get().getLoreDefault();
				lores.add(placeholders.apply(lore).toText());
			}
		}

		// Add cost if it's not already maxed out.
		if (level != getMaxLevel()) {
			lores.add("");
			lores.add("&7Cost: &e$" + Text.format(getCost(level + 1)));
		}

		return lores;
	}

	/**
	 * Gets the reset cooldown of the upgrade.
	 *
	 * @param level the level
	 * @return the reset cooldown
	 */
	@Nonnull
	public Duration getResetCooldown(int level) {
		Duration duration = resetCooldown.get(level);
		return duration.getSeconds() > 1 ? duration : Duration.ofSeconds(1);
	}
}