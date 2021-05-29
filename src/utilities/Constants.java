package utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Constants {
	
	private Constants() { }  // Prevents instantiation.
	
	public static final double VERSION = 4.7;
	
	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static final int DEFAULT_HEIGHT = (int) SCREEN_SIZE.getHeight() - 60;
	public static final int DEFAULT_WIDTH = DEFAULT_HEIGHT + 40;
	
	public static final int CHESS_SQUARE_PIXEL_SIZE = 48;
	public static final int CAPTURED_PIECE_PIXEL_SIZE = 16;
	
	/* the default number of rows, (this may not always be 8) */
	public static final int DEFAULT_NUM_OF_ROWS = 8;
	
	/* the number of columns, (this should always be 8) */
	public static final int DEFAULT_NUM_OF_COLUMNS = 8;
	
	/* this could be 1, 2 or 3 at maximum */
	public static final int DEFAULT_MAX_DEPTH = 1;
	
	public static final int NO_PIECE_CAPTURE_DRAW_HALFMOVES_LIMIT = 100;
	public static final int MIDDLEGAME_HALFMOVES_THRESHOLD = 24;
	
	public static final int DEAD_DRAW_MAX_BFS_DEPTH = 20;
	
	public static final long AI_MOVE_MILLISECONDS = 250;
	
	public static final Color BRIGHT_PINK = new Color(240, 207, 207);
	public static final Color DARK_GREEN = new Color(37, 82, 59);
	
	// NOT USED
	/*
	public static String[][] chessPositions = new String[][]{
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
	
	public static final boolean WHITE = true;
	public static final boolean BLACK = false;
		
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
	
	public static final String ICON_PATH = "images/icon.png";
	
	public static final String WHITE_IMG_PATH = "images/white/";
	
	public static final String WHITE_PAWN_IMG_PATH = WHITE_IMG_PATH + "pawn.png";
	public static final String WHITE_ROOK_IMG_PATH = WHITE_IMG_PATH + "rook.png";
	public static final String WHITE_KNIGHT_IMG_PATH = WHITE_IMG_PATH + "knight.png";
	public static final String WHITE_BISHOP_IMG_PATH = WHITE_IMG_PATH + "bishop.png";
	public static final String WHITE_QUEEN_IMG_PATH = WHITE_IMG_PATH + "queen.png";
	public static final String WHITE_KING_IMG_PATH = WHITE_IMG_PATH + "king.png";

	public static final String BLACK_IMG_PATH = "images/black/";

	public static final String BLACK_PAWN_IMG_PATH = BLACK_IMG_PATH + "pawn.png";
	public static final String BLACK_ROOK_IMG_PATH = BLACK_IMG_PATH + "rook.png";
	public static final String BLACK_KNIGHT_IMG_PATH = BLACK_IMG_PATH + "knight.png";
	public static final String BLACK_BISHOP_IMG_PATH = BLACK_IMG_PATH + "bishop.png";
	public static final String BLACK_QUEEN_IMG_PATH = BLACK_IMG_PATH + "queen.png";
	public static final String BLACK_KING_IMG_PATH = BLACK_IMG_PATH + "king.png";
	
	public static final double PAWN_VALUE = 1;
	public static final double KNIGHT_VALUE = 3;
	public static final double BISHOP_VALUE = 3;
	public static final double ROOK_VALUE = 5;
	public static final double QUEEN_VALUE = 9;
	// public static final double KING_VALUE = 10;

	public static final double PAWN_LATE_VALUE = 1;
	public static final double KNIGHT_LATE_VALUE = 3.5;
	public static final double BISHOP_LATE_VALUE = 3.5;
	public static final double ROOK_LATE_VALUE = 5.25;
	public static final double QUEEN_LATE_VALUE = 10;
	// public static final double KING_LATE_VALUE = 100;
	
	public static final double MOBILITY_MULTIPLIER = 2;
	// public static final double ATTACK_MULTIPLIER = 2;
	public static final double CHECKMATE_VALUE = Integer.MAX_VALUE;
	public static final double CHECK_VALUE = 50;
	public static final double CHECK_LATE_VALUE = 100;
	public static final double CASTLING_VALUE = 60;
	public static final double TWO_BISHOPS_VALUE = 10;

}
