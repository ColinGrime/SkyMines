package me.colingrimes.skymines.skymine.token;

import me.colingrimes.skymines.SkyMines;
import me.colingrimes.skymines.config.Settings;
import me.colingrimes.skymines.skymine.structure.MineSize;
import me.colingrimes.skymines.skymine.upgrades.SkyMineUpgrades;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.bukkit.NBT;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class DefaultSkyMineToken implements SkyMineToken {

	private final SkyMines plugin;

	public DefaultSkyMineToken(@Nonnull SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	@Nonnull
	public ItemStack getToken(@Nonnull MineSize size, @Nonnull Material borderType) {
		return getToken(size, borderType, new SkyMineUpgrades());
	}

	@Override
	@Nonnull
	public ItemStack getToken(@Nonnull MineSize size, @Nonnull Material borderType, @Nonnull SkyMineUpgrades upgrades) {
		return Items.of(Settings.TOKEN.get().clone())
				.placeholder("{length}", size.getLength())
				.placeholder("{height}", size.getHeight())
				.placeholder("{width}", size.getWidth())
				.nbt("skymine", true)
				.nbt("skymine-size", size.serialize())
				.nbt("skymine-blockvariety", upgrades.getBlockVarietyUpgrade().getLevel())
				.nbt("skymine-resetcooldown", upgrades.getResetCooldownUpgrade().getLevel())
				.nbt("skymine-bordertype", borderType.name())
				.build();
	}

	@Override
	public boolean isToken(@Nullable ItemStack item) {
		return NBT.getTag(item, "skymine", Boolean.class).orElse(false);
	}

	@Override
	@Nonnull
	public Optional<MineSize> getMineSize(@Nullable ItemStack item) {
		return NBT.getTag(item, "skymine-size", String.class).map(MineSize::deserialize);
	}

	@Override
	@Nonnull
	public Optional<Material> getBorderType(@Nullable ItemStack item) {
		return NBT.getTag(item, "skymine-bordertype").map(Material::getMaterial);
	}

	@Override
	@Nonnull
	public SkyMineUpgrades getUpgrades(@Nullable ItemStack item) {
		int blockVarietyLevel = NBT.getTag(item, "skymine-blockvariety", Integer.class).orElse(1);
		int resetCooldownLevel = NBT.getTag(item, "skymine-resetcooldown", Integer.class).orElse(1);
		return new SkyMineUpgrades(blockVarietyLevel, resetCooldownLevel);
	}
}
