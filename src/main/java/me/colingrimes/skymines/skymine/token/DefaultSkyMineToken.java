package me.colingrimes.skymines.skymine.token;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.geometry.Size;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.skymines.config.Mines;
import me.colingrimes.skymines.skymine.upgrade.SkyMineUpgrades;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.bukkit.NBT;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class DefaultSkyMineToken implements SkyMineToken {

	@Override
	public boolean isToken(@Nullable ItemStack item) {
		return NBT.getTag(item, "skymine", Boolean.class).orElse(false);
	}

	@Override
	public boolean isValidToken(@Nonnull ItemStack item) {
		return isToken(item) && getMine(item) != null;
	}

	@Nonnull
	@Override
	public ItemStack getToken(@Nonnull String identifier, @Nonnull Size mineSize) {
		return getToken(identifier, mineSize, new SkyMineUpgrades(identifier));
	}

	@Nonnull
	@Override
	public ItemStack getToken(@Nonnull String identifier, @Nonnull Size mineSize, @Nonnull SkyMineUpgrades upgrades) {
		Mines.Mine mine = Mines.MINES.get().get(identifier);
		String size = Mines.SIZE_PLACEHOLDER.get()
				.replace("{length}", String.valueOf(mineSize.getLength()))
				.replace("{height}", String.valueOf(mineSize.getHeight()))
				.replace("{width}", String.valueOf(mineSize.getWidth()));
		return Items.of(mine != null ? mine.getToken() : Mines.DEFAULT_TOKEN.get())
				.placeholder("{size}", size)
				.nbt("skymine", true)
				.nbt("skymine-id", identifier)
				.nbt("skymine-size", Json.toString(mineSize.serialize()))
				.nbt("skymine-upgrades", Json.toString(upgrades))
				.build();
	}

	@Nullable
	@Override
	public Mines.Mine getMine(@Nonnull ItemStack token) {
		return Mines.MINES.get().get(getMineIdentifier(token));
	}

	@Nonnull
	@Override
	public String getMineIdentifier(@Nonnull ItemStack token) {
		return NBT.getTag(token, "skymine-id").orElse("default");
	}

	@Override
	@Nonnull
	public Size getMineSize(@Nullable ItemStack token) {
		Optional<String> size = NBT.getTag(token, "skymine-size");
		if (size.isEmpty()) {
			throw new IllegalArgumentException("SkyMine token does not have an attached size.");
		} else if (Json.isJson(size.get())) {
			return Size.deserialize(Json.toElement(size.get()));
		} else {
			return Preconditions.checkNotNull(Size.of(size.get()), "SkyMine token does not have a valid size.");
		}
	}

	@Override
	@Nonnull
	public SkyMineUpgrades getUpgrades(@Nullable ItemStack token) {
		Optional<SkyMineUpgrades> upgrades = NBT.getTag(token, "skymine-upgrades").map(u -> SkyMineUpgrades.deserialize(Json.toElement(u)));
		if (upgrades.isPresent()) {
			return upgrades.get();
		}

		int compositionLevel = NBT.getTag(token, "skymine-blockvariety", Integer.class).orElse(1);
		int resetCooldownLevel = NBT.getTag(token, "skymine-resetcooldown", Integer.class).orElse(1);
		return new SkyMineUpgrades("default", compositionLevel, resetCooldownLevel);
	}
}
