package me.colingrimes.skymines.skymine.upgrade;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.misc.Validator;
import me.colingrimes.skymines.skymine.upgrade.type.CompositionUpgrade;
import me.colingrimes.skymines.skymine.upgrade.type.ResetCooldownUpgrade;
import me.colingrimes.skymines.skymine.upgrade.type.SkyMineUpgrade;

import javax.annotation.Nonnull;

public class SkyMineUpgrades implements Serializable {

	private final String identifier;
	private final CompositionUpgrade composition;
	private final ResetCooldownUpgrade resetCooldown;

	public SkyMineUpgrades(@Nonnull String identifier) {
		this(identifier, 1, 1);
	}

	public SkyMineUpgrades(@Nonnull String identifier, int compositionLevel, int resetCooldownLevel) {
		this(identifier, new CompositionUpgrade(identifier, compositionLevel), new ResetCooldownUpgrade(identifier, resetCooldownLevel));
	}

	public SkyMineUpgrades(@Nonnull String identifier, @Nonnull CompositionUpgrade composition, @Nonnull ResetCooldownUpgrade resetCooldown) {
		this.identifier = identifier;
		this.composition = composition;
		this.resetCooldown = resetCooldown;
	}

	/**
	 * Gets the upgrade instance associated with the specified upgrade type.
	 *
	 * @param type the upgrade type
	 * @return the upgrade associated with the upgrade type
	 */
	@Nonnull
	public SkyMineUpgrade getUpgrade(UpgradeType type) {
		return switch (type) {
			case Composition -> composition;
			case ResetCooldown -> resetCooldown;
		};
	}

	/**
	 * Gets the composition upgrade.
	 *
	 * @return the composition upgrade
	 */
	@Nonnull
	public CompositionUpgrade getComposition() {
		return composition;
	}

	/**
	 * Gets the reset cooldown upgrade.
	 *
	 * @return the reset cooldown upgrade
	 */
	@Nonnull
	public ResetCooldownUpgrade getResetCooldown() {
		return resetCooldown;
	}

	@Nonnull
	@Override
	public JsonElement serialize() {
		return Json.create()
				.add("identifier", identifier)
				.add("composition", composition.getLevel())
				.add("resetCooldown", resetCooldown.getLevel())
				.build();
	}

	@Nonnull
	public static SkyMineUpgrades deserialize(@Nonnull JsonElement element) {
		JsonObject object = Validator.checkJson(element, "identifier", "composition", "resetCooldown");
		return new SkyMineUpgrades(object.get("identifier").getAsString(), object.get("composition").getAsInt(), object.get("resetCooldown").getAsInt());
	}

	@Deprecated
	@Nonnull
	public static SkyMineUpgrades deserializeOld(@Nonnull String text) {
		String[] texts = text.split(":");

		int compositionLevel = 1;
		int resetCooldownLevel = 1;

		try {
			compositionLevel = Integer.parseInt(texts[0]);
			resetCooldownLevel = Integer.parseInt(texts[1]);
		} catch (NumberFormatException ignored) {}

		return new SkyMineUpgrades("default", compositionLevel, resetCooldownLevel);
	}
}
