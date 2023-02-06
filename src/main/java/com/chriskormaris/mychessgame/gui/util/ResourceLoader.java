package com.chriskormaris.mychessgame.gui.util;

import lombok.experimental.UtilityClass;

import java.net.URL;

@UtilityClass
final public class ResourceLoader {

	public URL load(String path) {
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
