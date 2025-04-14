package me.colingrimes.skymines.skymine.upgrade.data;

import me.colingrimes.midnight.config.util.Configs;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.text.Parser;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.config.Menus;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResetCooldownData extends UpgradeData {

	private final Map<Integer, Duration> resetCooldown;
	private Boolean valid = null;

	public ResetCooldownData(@Nonnull ConfigurationSection section) {
		super(section);
		this.resetCooldown = Configs.mapIntegerKeys(section, sec -> Parser.parseDuration(sec.getString("upgrade")));
	}

	@Override
	public boolean isValid() {
		if (valid != null) {
			return valid;
		}

		for (int i=1; i<=Math.max(1, getMaxLevel()); i++) {
			if (!resetCooldown.containsKey(i) || resetCooldown.get(i) == null) {
				valid = false;
				return false;
			}
		}

		valid = true;
		return true;
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
		return resetCooldown.get(level);
	}
}