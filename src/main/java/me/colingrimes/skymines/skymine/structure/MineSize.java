package me.colingrimes.skymines.skymine.structure;

import me.colingrimes.midnight.util.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MineSize {

	private final int length;
	private final int height;
	private final int width;

	public MineSize(int length, int height, int width) {
		this.length = length;
		this.height = height;
		this.width = width;
	}

	/**
	 * Gets the length of the skymine.
	 *
	 * @return the skymine's length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gets the height of the skymine.
	 *
	 * @return the skymine's height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the width of the skymine.
	 *
	 * @return the skymine's width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Serializes the mine size into a string.
	 *
	 * @return a string representing the mine size
	 */
	@Nonnull
	public String serialize() {
		return length + ":" + height + ":" + width;
	}

	/**
	 * Deserializes the string into a mine size
	 *
	 * @param text the text to parse
	 * @return the mine size if available
	 */
	@Nullable
	public static MineSize deserialize(@Nullable String text) {
		if (text == null || text.isEmpty() || text.split(":").length != 3) {
			return null;
		}

		try {
			String[] texts = text.split(":");
			return new MineSize(Integer.parseInt(texts[0]), Integer.parseInt(texts[1]), Integer.parseInt(texts[2]));
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	@Override
	@Nonnull
	public String toString() {
		return Text.color(String.format("&a%s&7x&a%s&7x&a%s", length, height, width));
	}
}
