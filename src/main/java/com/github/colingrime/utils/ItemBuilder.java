package com.github.colingrime.utils;

import com.github.colingrime.locale.Replacer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * Quick way to build ItemStack objects.
 * Supports parsing of Material enums from String formats.
 */
public final class ItemBuilder {

	private final Material defMaterial;
	private final Replacer replacer = new Replacer();

	private Material material;
	private String name;
	private List<String> lore;

	public ItemBuilder(@Nonnull Material def) {
		this.defMaterial = Objects.requireNonNull(def, "material");
	}

	public ItemBuilder material(Material material) {
		this.material = material;
		return this;
	}

	public ItemBuilder material(String input) {
		this.material = Material.matchMaterial(input);
		return this;
	}

	public ItemBuilder name(String name) {
		this.name = name;
		return this;
	}

	public ItemBuilder lore(List<String> lore) {
		this.lore = lore;
		return this;
	}

	public ItemBuilder replace(String placeholder, String replacement) {
		replacer.add(placeholder, replacement);
		return this;
	}

	public ItemBuilder replace(String placeholder, int replacement) {
		replacer.add(placeholder, replacement);
		return this;
	}

	public ItemStack build() {
		Material material = this.material == null ? this.defMaterial : this.material;
		ItemStack item = new ItemStack(material);

		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return item;
		}

		// set name and lore if available
		if (name != null) meta.setDisplayName(replacer.replace(StringUtils.color(name)));
		if (lore != null) meta.setLore(replacer.replace(StringUtils.color(lore)));

		item.setItemMeta(meta);
		return item;
	}
}
