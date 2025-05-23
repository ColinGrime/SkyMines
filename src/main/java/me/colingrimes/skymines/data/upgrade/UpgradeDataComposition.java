package me.colingrimes.skymines.data.upgrade;

import me.colingrimes.midnight.config.util.Configs;
import me.colingrimes.midnight.message.Placeholders;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.config.Menus;
import me.colingrimes.skymines.data.UpgradeData;
import me.colingrimes.skymines.skymine.structure.material.MineMaterial;
import me.colingrimes.skymines.skymine.structure.material.MineMaterialDynamic;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class UpgradeDataComposition extends UpgradeData {

	private final Map<Integer, Level> levels;

	/**
	 * Constructs the {@link UpgradeDataComposition} with the given configuration section.
	 * It will also validate the given data and will return {@code null} if it's invalid.
	 *
	 * @param section the configuration section
	 * @return the composition data or null if invalid
	 */
	@Nullable
	public static UpgradeDataComposition of(@Nonnull ConfigurationSection section) {
		UpgradeDataComposition data = new UpgradeDataComposition(section);
		for (int i=1; i<=Math.max(1, data.getMaxLevel()); i++) {
			if (!data.levels.containsKey(i) || data.levels.get(i).materialPercentages.isEmpty()) {
				return null;
			}
		}
		return data;
	}

	private UpgradeDataComposition(@Nonnull ConfigurationSection section) {
		super(section);
		this.levels = Configs.mapIntegerKeys(section, Level::new);
	}

	@Nonnull
	@Override
	public List<String> getLore(int level) {
		return levels.get(level).getLore();
	}

	/**
	 * Gets the composition of the upgrade.
	 *
	 * @param level the level
	 * @return the composition
	 */
	@Nonnull
	public MineMaterial getComposition(int level) {
		return levels.get(level).composition;
	}

	public class Level {
		private final int level;
		private final MineMaterialDynamic composition = new MineMaterialDynamic();
		private final Map<Material, Double> materialPercentages = new HashMap<>();
		private List<String> cachedLore = null;

		public Level(@Nonnull ConfigurationSection section, @Nonnull String level) {
			this.level = Integer.parseInt(level);
			this.init(section.getStringList("upgrade"));
		}

		/**
		 * Initializes the composition and mine material data.
		 *
		 * @param configMaterials the list of material-percentage pairs
		 */
		private void init(@Nonnull List<String> configMaterials) {
			for (String entry : configMaterials) {
				Material material = Material.getMaterial(entry.split(" ")[0].toUpperCase());
				String chance = entry.split(" ")[1].replace("%", "");
				if (!Types.isDouble(chance)) {
					continue;
				}

				// If material is invalid, we are going to pretend its AIR.
				if (material == null || !material.isBlock()) {
					material = Material.AIR;
				}

				composition.addMaterial(material, Double.parseDouble(chance));
				materialPercentages.put(material, materialPercentages.getOrDefault(material, 0.0) + Double.parseDouble(chance));
			}
		}

		@Nonnull
		public List<String> getLore() {
			if (cachedLore != null) {
				return cachedLore;
			}

			Level nextLevel = levels.get(level + 1);

			// Get all materisl from the current and next level.
			Set<Material> all = new HashSet<>(materialPercentages.keySet());
			if (nextLevel != null) {
				all.addAll(nextLevel.materialPercentages.keySet());
			}

			// Calculate the lores based on the material differences.
			List<MaterialLore> lores = new ArrayList<>();
			for (Material material : all) {
				double curr = materialPercentages.getOrDefault(material, 0.0);
				double next = nextLevel != null ? nextLevel.materialPercentages.getOrDefault(material, 0.0) : Double.NaN;
				Placeholders placeholders = Placeholders.create()
						.add("{type}", Text.format(material.name()))
						.add("{percentage}", Text.format(curr != 0 ? curr : next))
						.add("{next-percentage}", Text.format(next));

				if (curr == 0 && next > 0) {
					String add = Menus.UPGRADE_MENU_COMPOSITION.get().getLore(MaterialDiff.ADD);
					lores.add(new MaterialLore(MaterialDiff.ADD, next, placeholders.apply(add).toText()));
				} else if (curr > 0 && next == 0) {
					String remove = Menus.UPGRADE_MENU_COMPOSITION.get().getLore(MaterialDiff.REMOVE);
					lores.add(new MaterialLore(MaterialDiff.REMOVE, curr, placeholders.apply(remove).toText()));
				} else if (curr > 0 && next > 0 && curr != next) {
					String change = Menus.UPGRADE_MENU_COMPOSITION.get().getLore(MaterialDiff.CHANGE);
					lores.add(new MaterialLore(MaterialDiff.CHANGE, next, placeholders.apply(change).toText()));
				} else if (curr > 0 && next > 0 && curr == next) {
					String same = Menus.UPGRADE_MENU_COMPOSITION.get().getLore(MaterialDiff.SAME);
					lores.add(new MaterialLore(MaterialDiff.SAME, curr, placeholders.apply(same).toText()));
				} else if (Double.isNaN(next)) {
					String max = Menus.UPGRADE_MENU_COMPOSITION.get().getLore(MaterialDiff.MAX);
					lores.add(new MaterialLore(MaterialDiff.MAX, curr, placeholders.apply(max).toText()));
				}
			}

			// Sort the lores based on priority + percentages.
			lores.sort(Comparator
					.comparingInt((MaterialLore lore) -> -lore.diff.priority)
					.thenComparing((MaterialLore lore) -> -lore.percentage));
			cachedLore = lores.stream().map(lore -> lore.lore).collect(Collectors.toList());

			// Add cost if it's not already maxed out.
			if (nextLevel != null) {
				cachedLore.add("");
				cachedLore.add("&7Cost: &e$" + Text.format(getCost(nextLevel.level)));
			}

			return cachedLore;
		}
	}

	/**
	 * Represents the material lore for sorting.
	 */
	private static class MaterialLore {
		final MaterialDiff diff;
		final double percentage;
		final String lore;

		MaterialLore(@Nonnull MaterialDiff type, double percentage, @Nonnull String lore) {
			this.diff = type;
			this.percentage = percentage;
			this.lore = lore;
		}
	}

	/**
	 * Represents the material difference from one level to another.
	 */
	public enum MaterialDiff {
		/**
		 * The next level introduces a new material.
		 */
		ADD(0, "&7(&a+&7) &a{type} &7→ &e{percentage}%"),
		/**
		 * The next level removes a previously existing material.
		 */
		REMOVE(-1, "&7(&c-&7) &c{type}"),
		/**
		 * The material percentage is different when going to the next level.
		 */
		CHANGE(1, "&7(&dx&7) &d{type}&7: &e{percentage}% &7→ &e{next-percentage}%"),
		/**
		 * The material percentage is the same in both levels.
		 */
		SAME(2, "&7(&b=&7) &b{type} &7→ &e{percentage}%"),
		/**
		 * The material is already maxed out. There is no next level.
		 */
		MAX(0, "&7(&c✯&7) &c{type} &7→ &e{percentage}%");

		private final int priority;
		private final String defaultLore;

		MaterialDiff(int priority, @Nonnull String defaultLore) {
			this.priority = priority;
			this.defaultLore = defaultLore;
		}

		/**
		 * Gets the default lore.
		 *
		 * @return the default lore
		 */
		@Nonnull
		public String getDefaultLore() {
			return defaultLore;
		}
	}
}
