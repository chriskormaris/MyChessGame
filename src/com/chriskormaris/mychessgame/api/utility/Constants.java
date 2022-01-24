package com.chriskormaris.mychessgame.api.utility;

public class Constants {

	/* the default number of rows, (this may not always be 8) */
	public static final int DEFAULT_NUM_OF_ROWS = 8;

	/* the number of columns, (this should always be 8) */
	public static final int DEFAULT_NUM_OF_COLUMNS = 8;

	/* this could be 1, 2 or 3 at maximum */
	public static final int DEFAULT_MAX_DEPTH = 2;

	public static final int NO_CAPTURE_DRAW_MOVES_LIMIT = 50;
	public static final int NO_CAPTURE_DRAW_HALF_MOVES_LIMIT = NO_CAPTURE_DRAW_MOVES_LIMIT * 2;
	public static final int MIDDLE_GAME_MOVES_THRESHOLD = 20;
	public static final int MIDDLE_GAME_HALF_MOVES_THRESHOLD = MIDDLE_GAME_MOVES_THRESHOLD * 2;

	public static final int DEAD_DRAW_MAX_BFS_DEPTH = 20;

	public static final long MINIMAX_AI_MOVE_MILLISECONDS = 50;
	public static final long RANDOM_AI_MOVE_MILLISECONDS = 250;

	public static final boolean WHITE = true;
	public static final boolean BLACK = false;

	// NOT USED
	/*
	public static String[][] CHESS_POSITIONS = new String[][]{
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

	public static int[][] PAWNS_SQUARES_TABLE = new int[][]{
		{-50, -40, -30, -30, -30, -30, -40, -50},
		{-40, -20,   0,   0,   0,   0, -20, -40},
		{-30,   0,  10,  15,  15,  10,   0, -30},
		{-30,   5,  15,  20,  20,  15,   5, -30},
		{-30,   0,  15,  20,  20,  15,   0, -30},
		{-30,   5,  10,  15,  15,  10,   5, -30},
		{-40, -20,   0,   5,   5,   0, -20, -40},
		{-50, -40, -30, -30, -30, -30, -40, -50},
	};

	public static int[][] KNIGHTS_SQUARES_TABLE = new int[][]{
		{-50, -40, -30, -30, -30, -30, -40, -50},
		{-40, -20,   0,   0,   0,   0, -20, -40},
		{-30,   0,  10,  15,  15,  10,   0, -30},
		{-30,   5,  15,  20,  20,  15,   5, -30},
		{-30,   0,  15,  20,  20,  15,   0, -30},
		{-30,   5,  10,  15,  15,  10,   5, -30},
		{-40, -20,   0,   5,   5,   0, -20, -40},
		{-50, -40, -30, -30, -30, -30, -40, -50},
	};

	public static int[][] BISHOPS_SQUARES_TABLE = new int[][]{
		{-20, -10, -10, -10, -10, -10, -10, -20},
		{-10,   0,   0,   0,   0,   0,   0, -10},
		{-10,   0,   5,  10,  10,   5,   0, -10},
		{-10,   5,   5,  10,  10,   5,   5, -10},
		{-10,   0,  10,  10,  10,  10,   0, -10},
		{-10,  10,  10,  10,  10,  10,  10, -10},
		{-10,   5,   0,   0,   0,   0,   5, -10},
		{-20, -10, -10, -10, -10, -10, -10, -20},
	};

	public static int[][] ROOKS_SQUARES_TABLE = new int[][]{
		{0,   0,  0,  0,  0,  0,  0,  0},
		{5,  10, 10, 10, 10, 10, 10,  5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{-5,  0,  0,  0,  0,  0,  0, -5},
		{0,   0,  0,  5,  5,  0,  0,  0}
	};

	public static int[][] QUEEN_SQUARES_TABLE = new int[][]{
		{-20, -10, -10,  -5,  -5, -10, -10, -20},
		{-10,   0,   0,   0,   0,   0,   0, -10},
		{-10,   0,   5,   5,   5,   5,   0, -10},
		{-5,    0,   5,   5,   5,   5,   0, -5},
		{0,     0,   5,   5,   5,   5,   0, -5},
		{-10,   5,   5,   5,   5,   5,   0, -10},
		{-10,   0,   5,   0,   0,   0,   0, -10},
		{-20, -10, -10,  -5,  -5, -10, -10, -20}
	};

	public static int[][] KING_SQUARES_TABLE_MIDDLE_GAME = new int[][]{
		{-30, -40, -40, -50, -50, -40, -40, -30},
		{-30, -40, -40, -50, -50, -40, -40, -30},
		{-30, -40, -40, -50, -50, -40, -40, -30},
		{-30, -40, -40, -50, -50, -40, -40, -30},
		{-20, -30, -30, -40, -40, -30, -30, -20},
		{-10, -20, -20, -20, -20, -20, -20, -10},
		{20,   20,   0,   0,   0,   0,  20,  20},
		{20,   30,  10,   0,   0,  10,  30,  20}
	};

	public static int[][] KING_SQUARES_TABLE_ENDGAME = new int[][]{
		{-50, -40, -30, -20, -20, -30, -40, -50},
		{-30, -20, -10,   0,   0, -10, -20, -30},
		{-30, -10,  20,  30,  30,  20, -10, -30},
		{-30, -10,  30,  40,  40,  30, -10, -30},
		{-30, -10,  30,  40,  40,  30, -10, -30},
		{-30, -10,  20,  30,  30,  20, -10, -30},
		{-30, -30,   0,   0,   0,   0, -30, -30},
		{-50, -30, -30, -30, -30, -30, -30, -50}
	};

	public static final char WHITE_PAWN = 'P';
	public static final char WHITE_KNIGHT = 'N';
	public static final char WHITE_BISHOP = 'B';
	public static final char WHITE_ROOK = 'R';
	public static final char WHITE_QUEEN = 'Q';
	public static final char WHITE_KING = 'K';
	public static final char BLACK_PAWN = 'p';
	public static final char BLACK_KNIGHT = 'n';
	public static final char BLACK_BISHOP = 'b';
	public static final char BLACK_ROOK = 'r';
	public static final char BLACK_QUEEN = 'q';
	public static final char BLACK_KING = 'k';

	public static final String DEFAULT_STARTING_PIECES = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
	public static final String DEFAULT_STARTING_FEN_POSITION = DEFAULT_STARTING_PIECES + " w KQkq - 0 1";

	public static final double PAWN_VALUE = 1;
	public static final double KNIGHT_VALUE = 3;
	public static final double BISHOP_VALUE = 3;
	public static final double ROOK_VALUE = 5;
	public static final double QUEEN_VALUE = 9;

	public static final double MOBILITY_MULTIPLIER = 1;
	public static final double ATTACK_MULTIPLIER = 2;

	public static final double CHECKMATE_VALUE = Integer.MAX_VALUE;
	public static final double CHECK_VALUE = 100;
	public static final double CHECK_LATE_VALUE = 1000;
	public static final double CASTLING_VALUE = 200;
	// public static final double TWO_BISHOPS_VALUE = 100;

	private Constants() {
	}  // Prevents instantiation.

}
