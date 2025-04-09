package me.colingrimes.skymines.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Option;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.midnight.util.text.Parser;
import me.colingrimes.skymines.skymine.structure.material.MineMaterial;
import me.colingrimes.skymines.skymine.structure.material.MineMaterialDynamic;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static me.colingrimes.midnight.config.option.OptionFactory.option;

@Configuration("upgrades.yml")
public interface Upgrades {

	// Composition configurations.
	Option<Map<String, Composition>> COMPOSITION = option("composition", sec -> {
		Map<String, Composition> compositions = new HashMap<>();
		for (String name : sec.getKeys(false)) {
			ConfigurationSection s = sec.getConfigurationSection(name);
			if (s != null) {
				compositions.put(name, new Composition(s));
			}
		}
		return compositions;
	});

	// Reset Cooldown configurations.
	Option<Map<String, ResetCooldown>> RESET_COOLDOWN = option("reset-cooldown", sec -> {
		Map<String, ResetCooldown> resetCooldowns = new HashMap<>();
		for (String name : sec.getKeys(false)) {
			ConfigurationSection s = sec.getConfigurationSection(name);
			if (s != null) {
				resetCooldowns.put(name, new ResetCooldown(s));
			}
		}
		return resetCooldowns;
	});

	class Composition {
		private final Map<Integer, MineMaterial> composition = new HashMap<>();
		private final Map<Integer, Double> costs = new HashMap<>();
		private final int maxLevel;

		public Composition(@Nonnull ConfigurationSection sec) {
			for (String level : sec.getKeys(false)) {
				if (Types.isInteger(level)) {
					composition.put(Integer.parseInt(level), new MineMaterialDynamic(sec.getStringList(level + ".upgrade")));
					costs.put(Integer.parseInt(level), sec.getDouble(level + ".cost"));
				}
			}
			maxLevel = Collections.max(composition.keySet());
		}

		@Nonnull
		public Map<Integer, MineMaterial> getComposition() {
			return composition;
		}

		@Nonnull
		public Map<Integer, Double> getCosts() {
			return costs;
		}

		public int getMaxLevel() {
			return maxLevel;
		}
	}

	class ResetCooldown {
		private final Map<Integer, Duration> resetCooldown = new HashMap<>();
		private final Map<Integer, Double> costs = new HashMap<>();
		private final int maxLevel;

		public ResetCooldown(@Nonnull ConfigurationSection sec) {
			for (String level : sec.getKeys(false)) {
				if (Types.isInteger(level)) {
					resetCooldown.put(Integer.parseInt(level), Parser.parseDuration(sec.getString(level + ".upgrade")));
					costs.put(Integer.parseInt(level), sec.getDouble(level + ".cost"));
				}
			}
			maxLevel = Collections.max(resetCooldown.keySet());
		}

		@Nonnull
		public Map<Integer, Duration> getResetCooldown() {
			return resetCooldown;
		}

		@Nonnull
		public Map<Integer, Double> getCosts() {
			return costs;
		}

		public int getMaxLevel() {
			return maxLevel;
		}
	}
}
