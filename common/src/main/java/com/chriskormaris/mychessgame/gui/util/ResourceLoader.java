package com.chriskormaris.mychessgame.gui.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.net.URL;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceLoader {

	public static URL load(String path) {
		// InputStream input = ResourceLoader.class.getResourceAsStream(path);
		URL input = ResourceLoader.class.getResource(path);
		if (input == null) {
			input = ResourceLoader.class.getResource("/" + path);
		}
		// String in = input.getPath();
		// System.out.println("in: " + in);
		return input;
	}

}
