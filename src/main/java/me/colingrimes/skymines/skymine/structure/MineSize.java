package me.colingrimes.skymines.skymine.structure;

import me.colingrimes.midnight.util.text.Text;

import javax.annotation.Nonnull;

public class MineSize {

	private final int length;
	private final int height;
	private final int width;

	public MineSize(int length, int height, int width) {
		this.length = length;
		this.height = height;
		this.width = width;
	}

	public int getLength() {
		return length;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getMaxSide() {
		return Math.max(length, Math.max(height, width));
	}

	public static String parse(@Nonnull MineSize size) {
		return size.length + ":" + size.height + ":" + size.width;
	}

	public static MineSize parse(@Nonnull String text) {
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
