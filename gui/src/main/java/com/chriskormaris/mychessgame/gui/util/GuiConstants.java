package com.chriskormaris.mychessgame.gui.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GuiConstants {

	public static final String TITLE = "My Chess Game";
	public static final String VERSION = "10.4.4";

	public static final String FIRST_MOVE_TEXT = "Move: 1. White plays first.";
	public static final String ZERO_SCORE_TEXT = "Score:  0";

	public static final int CHESS_PIECE_SQUARE_PIXEL_SIZE = 56;
	public static final int CAPTURED_CHESS_PIECE_PIXEL_SIZE = 16;

	public static final long MINIMAX_AI_MOVE_DELAY_MILLISECONDS = 50L;
	public static final long RANDOM_AI_MOVE_DELAY_MILLISECONDS = 100L;

	public static final int DEFAULT_TIME_LIMIT_SECONDS = 600;

	public static final Color BRIGHT_PINK = new Color(240, 207, 207);
	public static final Color DARK_GREEN = new Color(37, 82, 59);

	public static final String IMG_PATH = "images/";
	public static final String ICON_PATH = IMG_PATH + "icon.png";
	public static final String CHECKMATE_IMG_PATH = IMG_PATH + "checkmate.png";
	public static final String DRAW_IMG_PATH = IMG_PATH + "draw.png";
	public static final String WHITE_IMG_PATH = IMG_PATH + "white/";
	public static final String WHITE_PAWN_IMG_PATH = WHITE_IMG_PATH + "pawn.png";
	public static final String WHITE_ROOK_IMG_PATH = WHITE_IMG_PATH + "rook.png";
	public static final String WHITE_KNIGHT_IMG_PATH = WHITE_IMG_PATH + "knight.png";
	public static final String WHITE_BISHOP_IMG_PATH = WHITE_IMG_PATH + "bishop.png";
	public static final String WHITE_QUEEN_IMG_PATH = WHITE_IMG_PATH + "queen.png";
	public static final String WHITE_KING_IMG_PATH = WHITE_IMG_PATH + "king.png";
	public static final String BLACK_IMG_PATH = IMG_PATH + "black/";
	public static final String BLACK_PAWN_IMG_PATH = BLACK_IMG_PATH + "pawn.png";
	public static final String BLACK_ROOK_IMG_PATH = BLACK_IMG_PATH + "rook.png";
	public static final String BLACK_KNIGHT_IMG_PATH = BLACK_IMG_PATH + "knight.png";
	public static final String BLACK_BISHOP_IMG_PATH = BLACK_IMG_PATH + "bishop.png";
	public static final String BLACK_QUEEN_IMG_PATH = BLACK_IMG_PATH + "queen.png";
	public static final String BLACK_KING_IMG_PATH = BLACK_IMG_PATH + "king.png";

	public static final String RULES = "The game of Chess is strictly played by 2 players and consists of 16 White and 16 Black pieces.\n"
			+ "There are 6 different Chess piece types: 1) King, 2) Rook, 3) Bishop, 4) Queen, 5) Knight & 6) Pawn. White always plays first.\n"
			+ "A Chess piece can only move to an empty square or take the place of an opponent's Chess piece, by capturing it.\n"
			+ "However, no Chess piece can jump over other Chess pieces, unless it is a Knight.\n"
			+ "\nThe aim of the game is to trap the opponent King. A move made by a player that threatens the opponent king is called a \"check\".\n"
			+ "If the player makes a move with a piece that checks the opponent King, in a way that the opponent player has no legal moves for his King,\n"
			+ "and he can't block the threat with another piece, that move is called a \"checkmate\" and the player that made the move wins the game.\n"
			+ "\nThe following scenarios are considered a draw:\n"
			+ "  1) If a player makes a move, that puts the opponent player in a place that he has not any legal move to make, and it is not a \"checkmate\",\n"
			+ "     that move is called a \"stalemate\" and the game ends in a draw.\n"
			+ "  2) If the only Chess pieces standing on the board are King vs King, King & Bishop vs King, King & Knight vs King, or\n"
			+ "     King & Bishop vs King & Bishop with the Bishops on the same color, then the game ends in a draw due to insufficient mating material.\n"
			+ "     In addition, if we have only Kings and Pawns left on the Chess board and the Kings are not able to capture the Pawns,\n"
			+ "     then this condition is also a draw due to insufficient mating material.\n"
			+ "  3) If 50 moves have been played and neither player has captured any opponent's Chess piece, then the player that plays next can claim a draw.\n"
			+ "  4) If 75 moves have been played and neither player has captured any opponent's Chess piece, then the game automatically ends in a draw.\n"
			+ "  5) If the exact Chess board position is repeated 3 times, at any time in the game, not necessarily successively,\n"
			+ "     then the player that plays next can claim a draw. This scenario is called threefold repetition.\n"
			+ "  6) If the exact Chess board position is repeated 5 times, at any time in the game, not necessarily successively,\n"
			+ "     then the game automatically ends in a draw. This scenario is called fivefold repetition.\n"
			+ "\nThe Chess pieces can move on the Chess board as follows:\n"
			+ "  1) The King can move only one square in each direction. It can't move to a square, where it will be threatened by an opponent's piece.\n"
			+ "     It can also perform a combined move with an ally Rook, called \"castling\". The castling can only be performed once per game, for each player.\n"
			+ "     There are two possible castling moves for each player's King, the \"queen side castling\" and the \"king side castling\".\n"
			+ "     The castling can only be performed under the condition that the King and the involved Rook have not moved from their starting positions.\n"
			+ "     In addition, no other Chess pieces must be between them, the king must not be in check and the intertwined squares must not be threatened.\n"
			+ "  2) The Rook can move any number of squares horizontally or vertically.\n"
			+ "  3) The Bishop can move any number of squares diagonally.\n"
			+ "  4) The Queen can move any number of squares diagonally, horizontally or vertically. It is the strongest piece in the game.\n"
			+ "  5) The Knight moves in an \"L\"-shape, after moving two squares either forward, backwards, left or right. It is the only piece that can jump over other pieces.\n"
			+ "  6) The Pawn can move one square forward, or 2 squares forward, if moving for the 1st time. It can capture an opponent's piece by moving one square diagonally.\n"
			+ "     It can also capture an opponent's Pawn, that has just moved 2 steps forward, in the previous move, while being next to it,\n"
			+ "     by moving diagonally towards its direction (\"en passant\" move).\n"
			+ "     If a Pawn reaches the final row of the other side of the Chess board, the player can promote it to a Bishop, Knight, Rook or even a Queen."
			+ "\n\nBesides the Standard Chess implementation there two additional variants, Chess 960 and Horde.";

}
