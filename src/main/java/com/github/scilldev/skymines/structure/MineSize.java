package com.github.scilldev.skymines.structure;

import com.github.scilldev.utils.Utils;

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

	@Override
	public String toString() {
		return Utils.color(String.format("&a%s&7x&a%s&7x&a%s", length, height, width));
	}
}
