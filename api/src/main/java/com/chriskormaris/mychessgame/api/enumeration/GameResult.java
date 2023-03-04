package com.chriskormaris.mychessgame.api.enumeration;

// This enum has NO effect on the code at the moment.
public enum GameResult {
	NONE,

	WHITE_CHECKMATE,
	BLACK_CHECKMATE,

	WHITE_STALEMATE_DRAW,
	BLACK_STALEMATE_DRAW,

	INSUFFICIENT_MATERIAL_DRAW,
	NO_CAPTURE_DRAW,
	THREEFOLD_REPETITION_DRAW,
	FIVEFOLD_REPETITION_DRAW

	// WIN_BY_RESIGNATION,
	// DRAW_BY_MUTUAL_AGREEMENT
}
