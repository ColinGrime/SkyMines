package me.colingrimes.skymines.skymine.token;

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

	@Nonnull
	@Override
	public Optional<ItemStack> getToken(@Nonnull String identifier, @Nonnull Size mineSize) {
		return getToken(identifier, mineSize, new SkyMineUpgrades(identifier));
	}

	@Nonnull
	@Override
	public Optional<ItemStack> getToken(@Nonnull String identifier, @Nonnull Size mineSize, @Nonnull SkyMineUpgrades upgrades) {
		Mines.Mine mine = Mines.MINES.get().get(identifier);
		if (mine == null) {
			return Optional.empty();
		}

		String size = Mines.SIZE_PLACEHOLDER.get()
				.replace("{length}", String.valueOf(mineSize.getLength()))
				.replace("{height}", String.valueOf(mineSize.getHeight()))
				.replace("{width}", String.valueOf(mineSize.getWidth()));
		return Optional.of(Items.of(mine.getToken())
				.placeholder("{size}", size)
				.nbt("skymine", true)
				.nbt("skymine-id", identifier)
				.nbt("skymine-size", Json.toString(mineSize.serialize()))
				.nbt("skymine-upgrades", Json.toString(upgrades))
				.build());
	}

	@Override
	public boolean isToken(@Nullable ItemStack item) {
		return NBT.getTag(item, "skymine", Boolean.class).orElse(false);
	}

	@Nonnull
	@Override
	public Optional<Mines.Mine> getMine(@Nonnull ItemStack item) {
		return NBT.getTag(item, "skymine-id").map(s -> Mines.MINES.get().get(s));
	}

	@Override
	@Nonnull
	public Optional<Size> getMineSize(@Nullable ItemStack item) {
		Optional<String> size = NBT.getTag(item, "skymine-size");
		if (size.isEmpty()) {
			return Optional.empty();
		} else if (Json.isJson(size.get())) {
			return Optional.of(Size.deserialize(Json.toElement(size.get())));
		} else {
			return Optional.ofNullable(Size.of(size.get()));
		}
	}

	@Override
	@Nonnull
	public SkyMineUpgrades getUpgrades(@Nullable ItemStack item) {
		Optional<SkyMineUpgrades> upgrades = NBT.getTag(item, "skymine-upgrades").map(u -> SkyMineUpgrades.deserialize(Json.toElement(u)));
		if (upgrades.isPresent()) {
			return upgrades.get();
		}

		int compositionLevel = NBT.getTag(item, "skymine-blockvariety", Integer.class).orElse(1);
		int resetCooldownLevel = NBT.getTag(item, "skymine-resetcooldown", Integer.class).orElse(1);
		return new SkyMineUpgrades("default", compositionLevel, resetCooldownLevel);
	}
}
