package utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Constants {

    public static final String VERSION = "4.7.4";
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
    public static final int DEFAULT_MAX_DEPTH = 2;
    public static final int NO_CAPTURE_DRAW_MOVES_LIMIT = 50;
    public static final int NO_CAPTURE_DRAW_HALF_MOVES_LIMIT = NO_CAPTURE_DRAW_MOVES_LIMIT * 2;
    public static final int MIDDLE_GAME_MOVES_THRESHOLD = 20;
    public static final int MIDDLE_GAME_HALF_MOVES_THRESHOLD = MIDDLE_GAME_MOVES_THRESHOLD * 2;
    public static final int DEAD_DRAW_MAX_BFS_DEPTH = 20;
    public static final long MINIMAX_AI_MOVE_MILLISECONDS = 50;
    public static final long RANDOM_AI_MOVE_MILLISECONDS = 250;
    public static final Color BRIGHT_PINK = new Color(240, 207, 207);
    public static final Color DARK_GREEN = new Color(37, 82, 59);
    public static final boolean WHITE = true;

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
    public static final String DEFAULT_STARTING_PIECES = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    public static final String DEFAULT_STARTING_FEN_POSITION = DEFAULT_STARTING_PIECES + " w KQkq - 0 1";
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
    public static final double PAWN_LATE_VALUE = 1;
    // public static final double KING_VALUE = 10;
    public static final double KNIGHT_LATE_VALUE = 3.5;
    public static final double BISHOP_LATE_VALUE = 3.5;
    public static final double ROOK_LATE_VALUE = 5.25;
    public static final double QUEEN_LATE_VALUE = 10;
    public static final double MOBILITY_MULTIPLIER = 2;
    // public static final double KING_LATE_VALUE = 100;
    public static final double ATTACK_MULTIPLIER = 2;
    public static final double CHECKMATE_VALUE = Integer.MAX_VALUE;
    public static final double CHECK_VALUE = 50;
    public static final double CHECK_LATE_VALUE = 100;
    public static final double CASTLING_VALUE = 60;
    public static final double TWO_BISHOPS_VALUE = 10;
    public static final String RULES =
            "The game of chess is strictly played by 2 players and consists of 16 White and 16 Black pieces.\n"
                    + "There are 6 different chess piece types: 1) King, 2) Rook, 3) Bishop, 4) Queen, 5) Knight & 6) Pawn. White always plays first.\n"
                    + "A chess piece can only move to an empty tile or take the place of an opponent's chess piece, by capturing it.\n"
                    + "However, no chess piece can't jump over other chess pieces, unless it is a Knight.\n"
                    + "The aim of the game is to trap the opponent King. A move made by a player that threatens the opponent king is called a \"check\".\n"
                    + "If the player makes a move with a piece that checks the opponent King, in a way that the opponent player has no legal moves for his King,\n"
                    + "and he can't block the threat with another piece, that move is called a \"checkmate\" and the player that made the move wins the game.\n"
                    + "\nThere are also 4 scenarios for a draw:\n"
                    + "  1. If a player makes a move, that puts the opponent player in a place that he has not any legal move to make, and it is not a \"checkmate\",\n"
                    + "     that move is called a \"stalemate\" and the game ends in a draw.\n"
                    + "  2. If the only chess pieces standing on the board are for both sides are either a lone King, a King and a Bishop, a King and 1 or 2 Knights,\n"
                    + "     then the game ends in a draw due to insufficient mating material.\n"
                    + "  3. If 50 turns have passed and neither player has captured any opponent's chess piece, then the player that plays next can declare a draw.\n"
                    + "  4. If the exact game board position is repeated 3 times, then the player that plays next can declare a draw. This case is called threefold repetition.\n"
                    + "\n\nThe chess pieces can move on the chess board as follows:\n"
                    + "  1) The King can move only one tile in each direction. It can't move to a tile, where it will be threatened by an opponent's piece.\n"
                    + "     It can also perform a combined move with an ally Rook, called \"castling\". The castling can only be performed once per game, for each player.\n"
                    + "     There are two possible castling moves for each player's King, the \"queen side castling\" and the \"king side castling\".\n"
                    + "     The castling can only be performed under the condition that the King and the involved Rook have not moved from their starting positions.\n"
                    + "     In addition, no other chess pieces must be between them, the king must not be in check and the intertwined tiles must not be threatened.\n"
                    + "  2) The Rook can move any number of tiles horizontally or vertically.\n"
                    + "  3) The Bishop can move any number of tiles diagonally.\n"
                    + "  4) The Queen can move any number of tiles diagonally, horizontally or vertically. It is the strongest piece in the game.\n"
                    + "  5) The Knight moves in an \"L\"-shape, after moving two tiles either forward, backwards, left or right. It is the only piece that can jump over other pieces.\n"
                    + "  6) The Pawn can move one tile forward, or 2 tiles forward, if moving for the 1st time. It can capture an opponent's piece by moving one tile diagonally.\n"
                    + "     It can also capture an opponent's Pawn, that has just moved 2 steps forward, the previous turn, while being next to it,\n"
                    + "     by moving diagonally towards its direction (\"en passant\" move).\n"
                    + "     If a Pawn reaches the final row of the other side of the chess board, the player can promote it to a Bishop, Knight, Rook or even a Queen.";

    private Constants() {
    }  // Prevents instantiation.

}
