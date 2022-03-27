package com.github.scilldev.skymines.structure;

import com.github.scilldev.utils.Utils;

public class MineSize {

	private final int length;
	private final int width;
	private final int height;

	public MineSize(int length, int width, int height) {
		this.length = length;
		this.width = width;
		this.height = height;
	}

	public int getLength() {
		return length;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return Utils.color(String.format("&a%s&7x&a%s&7x&a%s", length, height, width));
	}
}
