package com.github.colingrime.config.custom.implementation;

import com.github.colingrime.SkyMines;
import com.github.colingrime.config.custom.CustomConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

abstract class AbstractConfiguration implements CustomConfiguration {

	final SkyMines plugin;
	final File file;
	FileConfiguration config;

	AbstractConfiguration(SkyMines plugin) {
		this.plugin = plugin;
		this.file = new File(plugin.getDataFolder(), getName());
		this.checkFileExistence();
	}

	private void checkFileExistence() {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource(getName(), false);
		}
	}

	@Override
	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
		updateValues();
	}

	/**
	 * Updates the config options. Called after the config file is reloaded.
	 */
	abstract void updateValues();

	/**
	 * Iterates over a section of the config and performs an action on each key.
	 *
	 * @param name name of the section to act on
	 * @param action action to perform on each key of the section
	 */
	void iterateSection(@Nonnull String name, BiConsumer<ConfigurationSection, String> action) {
		ConfigurationSection section = Objects.requireNonNull(config.getConfigurationSection(name), getName());
		for (String key : section.getKeys(false)) {
			action.accept(section, key);
		}
	}

	/**
	 * Creates a Map for the given section.
	 * Each key of the Map is given from the keys in the ConfigurationSection.
	 * Each value of the Map is given from the {@code input}'s return vaue.
	 *
	 * @param section name of the section to act on
	 * @param input function to retrieve value pair for the key
	 * @param <V> object you want the values to be
	 * @return new Map object
	 */
	<V> Map<String, V> createMap(@Nonnull String section, BiFunction<ConfigurationSection, String, V> input) {
		return createMap(section, key -> key, input);
	}

	/**
	 * Creates a Map for the given section.
	 * Each key of the Map is given from the keys in the ConfigurationSection.
	 * Each value of the Map is given from the {@code input}'s return vaue.
	 *
	 * @param section name of the section to act on
	 * @param typeConverter function to change the name to a different type
	 * @param input function to retrieve value pair for the key
	 * @param <K> object you want the keys to be
	 * @param <V> object you want the values to be
	 * @return new Map object
	 */
	<K, V> Map<K, V> createMap(@Nonnull String section, Function<String, K> typeConverter, BiFunction<ConfigurationSection, String, V> input) {
		Map<K, V> map = new HashMap<>();
		ConfigurationSection sec = Objects.requireNonNull(config.getConfigurationSection(section), getName());

		for (String keyString : sec.getKeys(false)) {
			K key = typeConverter.apply(keyString);
			V value = input.apply(sec, keyString);
			if (key != null && value != null) {
				map.put(key, value);
			}
		}

		return map;
	}
}
