package com.chriskormaris.mychessgame.gui.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GuiConstants {

	public static final String TITLE = "My Chess Game";
	public static final String VERSION = "10.7.2";

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

	public static final String RULES = "<html><p>The game of Chess is strictly played by <b>2 players</b> and consists of <b>16 White</b> and <b>16 Black</b> pieces.<br>"
			+ "There are <b>6</b> different Chess <b>piece types</b>: <b>1) Pawn, 2) Knight, 3) Bishop, 4) Rook, 5) Queen & 6) King</b>. White always plays first.<br>"
			+ "A Chess piece can only move to an empty square or take the place of an opponent's Chess piece, by capturing it.<br>"
			+ "However, no Chess piece can jump over other Chess pieces, unless it is a Knight.</p>"
			+ "<p>The aim of the game is to trap the opponent King. A move made by a player that threatens the opponent king is called a <b>check</b>.<br>"
			+ "If the player makes a move with a piece that checks the opponent King, in a way that the opponent player has no legal moves for his King,<br>"
			+ "and he can't block the threat with another piece, that move is called a <b>checkmate</b> and the player that made the move wins the game.</p>"
			+ "<p>The following scenarios are considered a <b>draw</b>:</p"
			+ "<ol><li>If a player makes a move, that puts the opponent player in a place that he has not any legal move to make, and it is not a checkmate,<br>"
			+ "that move is called a <b>stalemate</b> and the game ends in a draw.</li>"
			+ "<li>If the only Chess pieces standing on the board are King vs King, King & Bishop vs King, King & Knight vs King, or<br>"
			+ "King & Bishop vs King & Bishop with the Bishops on the same color, then the game ends in a draw due to <b>insufficient mating material</b>.<br>"
			+ "In addition, if we have only Kings and Pawns left on the Chess board and the Kings are not able to capture the Pawns,<br>"
			+ "then this condition is also a draw due to insufficient mating material.</li>"
			+ "<li>If <b>50 moves</b> have been played and neither player has captured any opponent's Chess piece, then the player that plays next can claim a draw.</li>"
			+ "<li>If <b>75 moves</b> have been played and neither player has captured any opponent's Chess piece, then the game automatically ends in a draw.</li>"
			+ "<li>If the exact Chess board position is repeated 3 times, at any time in the game, not necessarily successively,<br>"
			+ "then the player that plays next can claim a draw. This scenario is called <b>threefold repetition</b>.</li>"
			+ "<li>If the exact Chess board position is repeated 5 times, at any time in the game, not necessarily successively,<br>"
			+ "then the game automatically ends in a draw. This scenario is called <b>fivefold repetition</b>.</li></ol>"
			+ "<p>The Chess pieces can <b>move</b> on the Chess board as follows:</p>"
            + "<ol><li>The <b>Pawn</b> can move one square forward, or 2 squares forward, if moving for the 1st time. It can capture an opponent's piece by moving one square diagonally.<br>"
            + "It can also capture an opponent's Pawn, that has just moved 2 steps forward, in the previous move, while being next to it,<br>"
            + "by moving diagonally towards its direction (<b>en passant</b> move).<br>"
            + "If a Pawn reaches the final row of the other side of the Chess board, the player can <b>promote</b> it to a Bishop, Knight, Rook or even a Queen.</li>"
            + "<li>The <b>Knight</b> moves in an <b>L-shape</b>, after moving two squares either forward, backwards, left or right. It is the only piece that can jump over other pieces.</li>"
            + "<li>The <b>Bishop</b> can move any number of squares diagonally.</li>"
            + "<li>The <b>Rook</b> can move any number of squares horizontally or vertically.</li>"
            + "<li>The <b>Queen</b> is the strongest piece in the game. It combines the movement of the Bishop and the Rook.<br>"
            + "It can move any number of squares diagonally, horizontally or vertically.</li>"
            + "<li>The <b>King</b> can move only one square in each direction. It can't move to a square, where it will be threatened by an opponent's piece.<br>"
			+ "It can also perform a combined move with an ally Rook, called <b>castling</b>. The castling can only be performed once per game, for each player.<br>"
			+ "There are two possible castling moves for each player's King, the <b>queen side castling</b> and the <b>king side castling</b>.<br>"
			+ "The castling can only be performed under the condition that the King and the involved Rook have not moved from their starting positions.<br>"
			+ "In addition, no other Chess pieces must be between them, the king must not be in check and the intertwined squares must not be threatened.</li></ol>"
			+ "<p>Besides the <b>Standard Chess</b> implementation there two additional variants, <b>Chess 960</b> and <b>Horde</b>.</p></html>";

}
