package utilities;

public class Constants {
	
	private Constants() { }  // Prevents instantiation.
	
	public final static double VERSION = 3.5;
	
	public final static int CAPTURED_PIECE_PIXEL_SIZE = 16;
	
	/* the default number of rows, (this should always be 8). */
	public final static int DEFAULT_NUM_OF_ROWS = 8;
	
	/* the number of columns, (this should always be 8). */
	public final static int NUM_OF_COLUMNS = 8;  
	
	public final static int NO_PIECE_CAPTURE_HALFMOVES_DRAW_LIMIT = 100;
	public final static int MIDDLEGAME_HALFMOVES_THRESHOLD = 24;
	
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
	
	public final static boolean WHITE = true;
	public final static boolean BLACK = false;
		
	public final static char WHITE_PAWN = 'P';
	public final static char WHITE_KNIGHT = 'N';
	public final static char WHITE_BISHOP = 'B';
	public final static char WHITE_ROOK = 'R';
	public final static char WHITE_QUEEN = 'Q';
	public final static char WHITE_KING = 'K';

	public final static char BLACK_PAWN = 'p';
	public final static char BLACK_KNIGHT = 'n';
	public final static char BLACK_BISHOP = 'b';
	public final static char BLACK_ROOK = 'r';
	public final static char BLACK_QUEEN = 'q';
	public final static char BLACK_KING = 'k';
	
	public final static String WHITE_IMG_PATH = "images/white/";
	
	public final static String WHITE_PAWN_IMG_PATH = WHITE_IMG_PATH + "pawn.png";
	public final static String WHITE_ROOK_IMG_PATH = WHITE_IMG_PATH + "rook.png";
	public final static String WHITE_KNIGHT_IMG_PATH = WHITE_IMG_PATH + "knight.png";
	public final static String WHITE_BISHOP_IMG_PATH = WHITE_IMG_PATH + "bishop.png";
	public final static String WHITE_QUEEN_IMG_PATH = WHITE_IMG_PATH + "queen.png";
	public final static String WHITE_KING_IMG_PATH = WHITE_IMG_PATH + "king.png";

	public final static String BLACK_IMG_PATH = "images/black/";

	public final static String BLACK_PAWN_IMG_PATH = BLACK_IMG_PATH + "pawn.png";
	public final static String BLACK_ROOK_IMG_PATH = BLACK_IMG_PATH + "rook.png";
	public final static String BLACK_KNIGHT_IMG_PATH = BLACK_IMG_PATH + "knight.png";
	public final static String BLACK_BISHOP_IMG_PATH = BLACK_IMG_PATH + "bishop.png";
	public final static String BLACK_QUEEN_IMG_PATH = BLACK_IMG_PATH + "queen.png";
	public final static String BLACK_KING_IMG_PATH = BLACK_IMG_PATH + "king.png";
	
	public final static double PAWN_VALUE = 1;
	public final static double KNIGHT_VALUE = 3;
	public final static double BISHOP_VALUE = 3;
	public final static double ROOK_VALUE = 5;
	public final static double QUEEN_VALUE = 9;
	// public final static double KING_VALUE = 10;

	public final static double PAWN_LATE_VALUE = 1;
	public final static double KNIGHT_LATE_VALUE = 3.5;
	public final static double BISHOP_LATE_VALUE = 3.5;
	public final static double ROOK_LATE_VALUE = 5.25;
	public final static double QUEEN_LATE_VALUE = 10;
	// public final static double KING_LATE_VALUE = 100;
	
	public final static double MOBILITY_MULTIPLIER = 2;
	// public final static double ATTACK_MULTIPLIER = 2;
	public final static double CHECKMATE_VALUE = Integer.MAX_VALUE;
	public final static double CHECK_VALUE = 50;
	public final static double CHECK_LATE_VALUE = 100;
	public final static double CASTLING_VALUE = 60;
	public final static double TWO_BISHOPS_VALUE = 50;

}
