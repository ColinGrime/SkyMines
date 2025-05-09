package me.colingrimes.skymines.manager;

import me.colingrimes.midnight.geometry.Region;
import me.colingrimes.midnight.hologram.Hologram;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.text.Text;
import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.SkyMine;
import me.colingrimes.skymines.util.MineUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramManager {

	private final Map<SkyMine, Hologram> holograms = new HashMap<>();
	private final SkyMines plugin;

	public HologramManager(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
		Scheduler.sync().runRepeating(() -> {
			if (plugin.getStorage().isLoaded()) {
				holograms.forEach(this::update);
			}
		}, 10L, 10L);
	}

	private void update(@Nonnull SkyMine skyMine, @Nonnull Hologram hologram) {
		// Hologram has been disabled, remove it.
		if (!skyMine.isEnabled() || !Settings.OPTION_HOLOGRAM_TOGGLE.get()) {
			hologram.remove();
			return;
		} else if (!hologram.active()) {
			hologram.spawn();
		}

		List<String> lines = MineUtils.placeholders(Settings.OPTION_HOLOGRAM_LINES, skyMine)
				.replace("{time}", Text.format(plugin.getCooldownManager().getSkyMineCooldown().getTimeLeft(skyMine)))
				.toTextList();
		hologram.setLines(lines);
	}

	/**
	 * Adds a hologram for the specified {@link SkyMine}.
	 *
	 * @param skyMine the skymine to add the hologram to
	 */
	public void addHologram(@Nonnull SkyMine skyMine) {
		Region region = skyMine.getStructure();
		Hologram hologram = Hologram.of(region.getMid().setY(region.getMax().getY() + 3));
		hologram.spawn();
		holograms.put(skyMine, hologram);
	}

	/**
	 * Removes the hologram from the specified {@link SkyMine}.
	 *
	 * @param skyMine the skymine to remove the hologram from
	 */
	public void removeHologram(@Nonnull SkyMine skyMine) {
		if (holograms.containsKey(skyMine)) {
			holograms.get(skyMine).remove();
		}
		holograms.remove(skyMine);
	}
}
