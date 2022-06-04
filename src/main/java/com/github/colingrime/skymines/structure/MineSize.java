package com.github.colingrime.skymines.structure;

import com.github.colingrime.utils.StringUtils;

import javax.annotation.Nonnull;

public record MineSize(int length, int height, int width) {

	public static String serialize(@Nonnull MineSize size) {
		return size.length + ":" + size.height + ":" + size.width;
	}

	public static MineSize deserialize(@Nonnull String text) {
		try {
			String[] texts = text.split(":");
			return new MineSize(Integer.parseInt(texts[0]), Integer.parseInt(texts[1]), Integer.parseInt(texts[2]));
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	@Override
	public String toString() {
		return StringUtils.color(String.format("&a%s&7x&a%s&7x&a%s", length, height, width));
	}
}
