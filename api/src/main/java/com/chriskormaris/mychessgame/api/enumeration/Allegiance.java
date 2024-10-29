package com.chriskormaris.mychessgame.api.enumeration;

public enum Allegiance {
	NONE,
	WHITE,
	BLACK;

	public boolean isWhite() {
		return this == WHITE;
	}

	public boolean isBlack() {
		return this == BLACK;
	}

}
