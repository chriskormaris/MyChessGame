package utilities;

public class Constants {
	
	public final static double VERSION = 2.9;
	
	/* the default number of rows, this should always be 8 */
	public final static int DEFAULT_NUM_OF_ROWS = 8;
	/* the number of columns, this should always be 8 */
	public final static int NUM_OF_COLUMNS = 8;  
	
	public final static int NO_CAPTURE_DRAW_HALFMOVES_LIMIT = 100;
	public final static int MIDDLEGAME_HALFMOVES_THRESHOLD = 24;
	
	// NOT USED
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
	
	public final static boolean WHITE = true;
	public final static boolean BLACK = false;
	
	// GUI styles
	public static final int CROSS_PLATFORM_STYLE = 1;
	public static final int NIMBUS_STYLE = 2;
	
	// Game modes
	public static final int HUMAN_VS_MINIMAX_AI = 1;
	public static final int HUMAN_VS_RANDOM_AI = 2;
	public static final int HUMAN_VS_HUMAN = 3;
	public static final int MINIMAX_AI_VS_MINIMAX_AI = 4;

	public final static int EMPTY = 0;
	
	public final static int PAWN = 1;
	public final static int KNIGHT = 2;
	public final static int BISHOP = 3;
	public final static int ROOK = 4;
	public final static int QUEEN = 5;
	public final static int KING = 6;
	
	public final static int WHITE_PAWN = PAWN;
	public final static int WHITE_KNIGHT = KNIGHT;
	public final static int WHITE_BISHOP = BISHOP;
	public final static int WHITE_ROOK = ROOK;
	public final static int WHITE_QUEEN = QUEEN;
	public final static int WHITE_KING = KING;

	public final static int BLACK_PAWN = -PAWN;
	public final static int BLACK_KNIGHT = -KNIGHT;
	public final static int BLACK_BISHOP = -BISHOP;
	public final static int BLACK_ROOK = -ROOK;
	public final static int BLACK_QUEEN = -QUEEN;
	public final static int BLACK_KING = -KING;
	
	public final static String WHITE_PAWN_IMG_PATH = "images/white/pawn.png";
	public final static String WHITE_ROOK_IMG_PATH = "images/white/rook.png";
	public final static String WHITE_KNIGHT_IMG_PATH = "images/white/knight.png";
	public final static String WHITE_BISHOP_IMG_PATH = "images/white/bishop.png";
	public final static String WHITE_QUEEN_IMG_PATH = "images/white/queen.png";
	public final static String WHITE_KING_IMG_PATH = "images/white/king.png";

	public final static String BLACK_PAWN_IMG_PATH = "images/black/pawn.png";
	public final static String BLACK_ROOK_IMG_PATH = "images/black/rook.png";
	public final static String BLACK_KNIGHT_IMG_PATH = "images/black/knight.png";
	public final static String BLACK_BISHOP_IMG_PATH = "images/black/bishop.png";
	public final static String BLACK_QUEEN_IMG_PATH = "images/black/queen.png";
	public final static String BLACK_KING_IMG_PATH = "images/black/king.png";
	
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
	public final static double ATTACK_MULTIPLIER = 2;
	public final static double CHECKMATE_VALUE = Integer.MAX_VALUE;
	public final static double CHECK_VALUE = 50;
	public final static double CHECK_LATE_VALUE = 100;
	public final static double CASTLING_VALUE = 60;
	public final static double TWO_BISHOPS_VALUE = 50;

}
