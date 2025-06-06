package com.chriskormaris.mychessgame.api.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

	/* the default number of rows, (this may not always be 8) */
	public static final int DEFAULT_NUM_OF_ROWS = 8;

	/* the number of columns, (this should always be 8) */
	public static final int DEFAULT_NUM_OF_COLUMNS = 8;

	/* this could be 1, 2 or 3 at maximum */
	public static final int DEFAULT_MAX_DEPTH = 2;

	public static final int CONDITIONAL_NO_CAPTURE_DRAW_MOVES_LIMIT = 50;
	public static final int UNCONDITIONAL_NO_CAPTURE_DRAW_MOVES_LIMIT = 75;

	public static final int BLOCKED_KING_AND_PAWNS_DRAW_MAX_BFS_DEPTH = 21;

	// public static final int ENDGAME_MOVES_THRESHOLD = 24;

	public static final boolean WHITE = true;
	public static final boolean BLACK = false;

	// NOT USED
	/*
	public static final String[][] CHESS_POSITIONS = new String[][]{
		{"A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8"},
		{"A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7"},
		{"A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6"},
		{"A5", "B5", "C5", "D5", "E5", "F5", "G5", "H5"},
		{"A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4"},
		{"A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3"},
		{"A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2"},
		{"A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1"}
    };
    */

	public static final char WHITE_PAWN_CHAR = 'P';
	public static final char WHITE_KNIGHT_CHAR = 'N';
	public static final char WHITE_BISHOP_CHAR = 'B';
	public static final char WHITE_ROOK_CHAR = 'R';
	public static final char WHITE_QUEEN_CHAR = 'Q';
	public static final char WHITE_KING_CHAR = 'K';
	public static final char BLACK_PAWN_CHAR = 'p';
	public static final char BLACK_KNIGHT_CHAR = 'n';
	public static final char BLACK_BISHOP_CHAR = 'b';
	public static final char BLACK_ROOK_CHAR = 'r';
	public static final char BLACK_QUEEN_CHAR = 'q';
	public static final char BLACK_KING_CHAR = 'k';

	public static final String DEFAULT_STARTING_PIECES = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	public static final String DEFAULT_STARTING_FEN_POSITION = DEFAULT_STARTING_PIECES + " w KQkq - 0 1";
	public static final String DEFAULT_STARTING_SHREDDER_FEN_POSITION = DEFAULT_STARTING_PIECES + " w AHah - 0 1";
	public static final String DEFAULT_STARTING_HORDE_FEN_POSITION = "rnbqkbnr/pppppppp/8/1PP2PP1/PPPPPPPP/PPPPPPPP/PPPPPPPP/PPPPPPPP w kq - 0 1";

	public static final int PAWN_SCORE_VALUE = 1;
	public static final int KNIGHT_SCORE_VALUE = 3;
	public static final int BISHOP_SCORE_VALUE = 3;
	public static final int ROOK_SCORE_VALUE = 5;
	public static final int QUEEN_SCORE_VALUE = 9;

}
