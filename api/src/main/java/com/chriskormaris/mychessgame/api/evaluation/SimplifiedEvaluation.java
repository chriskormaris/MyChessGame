package com.chriskormaris.mychessgame.api.evaluation;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.GamePhase;
import com.chriskormaris.mychessgame.api.square.ChessSquare;

// Simplified Evaluation Function by Polish chess programmer Tomasz Michniewski.
// see: https://www.chessprogramming.org/Simplified_Evaluation_Function
public class SimplifiedEvaluation implements Evaluation {

	private static final int[][] PAWN_SQUARE_TABLE = new int[][]{
			{ 0,  0,   0,   0,   0,   0,  0,  0},
			{50, 50,  50,  50,  50,  50, 50, 50},
			{10, 10,  20,  30,  30,  20, 10, 10},
			{ 5,  5,  10,  25,  25,  10,  5,  5},
			{ 0,  0,   0,  20,  20,   0,  0,  0},
			{ 5, -5, -10,   0,   0, -10, -5,  5},
			{ 5, 10,  10, -20, -20,  10, 10,  5},
			{ 0,  0,   0,   0,   0,   0,  0,  0}
	};

	private static final int[][] KNIGHT_SQUARE_TABLE = new int[][]{
			{-50, -40, -30, -30, -30, -30, -40, -50},
			{-40, -20,   0,   0,   0,   0, -20, -40},
			{-30,   0,  10,  15,  15,  10,   0, -30},
			{-30,   5,  15,  20,  20,  15,   5, -30},
			{-30,   0,  15,  20,  20,  15,   0, -30},
			{-30,   5,  10,  15,  15,  10,   5, -30},
			{-40, -20,   0,   5,   5,   0, -20, -40},
			{-50, -40, -30, -30, -30, -30, -40, -50},
	};

	private static final int[][] BISHOP_SQUARE_TABLE = new int[][]{
			{-20, -10, -10, -10, -10, -10, -10, -20},
			{-10,   0,   0,   0,   0,   0,   0, -10},
			{-10,   0,   5,  10,  10,   5,   0, -10},
			{-10,   5,   5,  10,  10,   5,   5, -10},
			{-10,   0,  10,  10,  10,  10,   0, -10},
			{-10,  10,  10,  10,  10,  10,  10, -10},
			{-10,   5,   0,   0,   0,   0,   5, -10},
			{-20, -10, -10, -10, -10, -10, -10, -20},
	};

	private static final int[][] ROOK_SQUARE_TABLE = new int[][]{
			{ 0,  0,  0,  0,  0,  0,  0,  0},
			{ 5, 10, 10, 10, 10, 10, 10,  5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{ 0,  0,  0,  5,  5,  0,  0,  0}
	};

	private static final int[][] QUEEN_SQUARE_TABLE = new int[][]{
			{-20, -10, -10, -5, -5, -10, -10, -20},
			{-10,   0,   0,  0,  0,   0,   0, -10},
			{-10,   0,   5,  5,  5,   5,   0, -10},
			{ -5,   0,   5,  5,  5,   5,   0,  -5},
			{  0,   0,   5,  5,  5,   5,   0,  -5},
			{-10,   5,   5,  5,  5,   5,   0, -10},
			{-10,   0,   5,  0,  0,   0,   0, -10},
			{-20, -10, -10, -5, -5, -10, -10, -20}
	};

	private static final int[][] MIDDLEGAME_KING_SQUARE_TABLE = new int[][]{
			{-30, -40, -40, -50, -50, -40, -40, -30},
			{-30, -40, -40, -50, -50, -40, -40, -30},
			{-30, -40, -40, -50, -50, -40, -40, -30},
			{-30, -40, -40, -50, -50, -40, -40, -30},
			{-20, -30, -30, -40, -40, -30, -30, -20},
			{-10, -20, -20, -20, -20, -20, -20, -10},
			{ 20,  20,   0,   0,   0,   0,  20,  20},
			{ 20,  30,  10,   0,   0,  10,  30,  20}
	};

	private static final int[][] ENDGAME_KING_SQUARE_TABLE = new int[][]{
			{-50, -40, -30, -20, -20, -30, -40, -50},
			{-30, -20, -10,   0,   0, -10, -20, -30},
			{-30, -10,  20,  30,  30,  20, -10, -30},
			{-30, -10,  30,  40,  40,  30, -10, -30},
			{-30, -10,  30,  40,  40,  30, -10, -30},
			{-30, -10,  20,  30,  30,  20, -10, -30},
			{-30, -30,   0,   0,   0,   0, -30, -30},
			{-50, -30, -30, -30, -30, -30, -30, -50}
	};

	private static final int PAWN_CENTIPAWN_VALUE = 100;
	private static final int KNIGHT_CENTIPAWN_VALUE = 320;
	private static final int BISHOP_CENTIPAWN_VALUE = 330;
	private static final int ROOK_CENTIPAWN_VALUE = 500;
	private static final int QUEEN_CENTIPAWN_VALUE = 900;
	private static final int KING_CENTIPAWN_VALUE = 20000;

	private GamePhase getGamePhase(ChessBoard chessBoard, int halfMoveNumber) {
		// if ((chessBoard.isEndGame()) && Constants.ENDGAME_MOVES_THRESHOLD * 2 <= halfMoveNumber) {
		if ((chessBoard.isEndGame())) {
			return GamePhase.ENDGAME;
		} else {
			return GamePhase.OPENING_MIDDLEGAME;
		}
	}

	private int getPieceCentipawnValue(ChessSquare chessSquare) {
		if (chessSquare.isPawn()) {
			return PAWN_CENTIPAWN_VALUE;
		} else if (chessSquare.isKnight()) {
			return KNIGHT_CENTIPAWN_VALUE;
		} else if (chessSquare.isBishop()) {
			return BISHOP_CENTIPAWN_VALUE;
		} else if (chessSquare.isRook()) {
			return ROOK_CENTIPAWN_VALUE;
		} else if (chessSquare.isQueen()) {
			return QUEEN_CENTIPAWN_VALUE;
		} else if (chessSquare.isKing()) {
			return KING_CENTIPAWN_VALUE;
		}
		return 0;
	}

	private int getPieceSquareValue(int row, int column, ChessSquare chessSquare, GamePhase gamePhase) {
		if (chessSquare.isPawn()) {
			return PAWN_SQUARE_TABLE[row][column];
		} else if (chessSquare.isKnight()) {
			return KNIGHT_SQUARE_TABLE[row][column];
		} else if (chessSquare.isBishop()) {
			return BISHOP_SQUARE_TABLE[row][column];
		} else if (chessSquare.isRook()) {
			return ROOK_SQUARE_TABLE[row][column];
		} else if (chessSquare.isQueen()) {
			return QUEEN_SQUARE_TABLE[row][column];
		} else if (chessSquare.isKing()) {
			if (gamePhase == GamePhase.OPENING_MIDDLEGAME) {
				return MIDDLEGAME_KING_SQUARE_TABLE[row][column];
			} else if (gamePhase == GamePhase.ENDGAME) {
				return ENDGAME_KING_SQUARE_TABLE[row][column];
			}
		}
		return 0;
	}

	// Simplified Evaluation Function.
	@Override
	public double evaluate(ChessBoard chessBoard) {
		int score = 0;
		GamePhase gamePhase = getGamePhase(chessBoard, chessBoard.getHalfMoveNumber());

		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				ChessSquare chessSquare = chessBoard.getGameBoard()[i][j];
				if (chessSquare.isWhite()) {
					score += PIECE_VALUE_MULTIPLIER * getPieceCentipawnValue(chessSquare);
					score += getPieceSquareValue(i, j, chessSquare, gamePhase);
				} else if (chessSquare.isBlack()) {
					score -= PIECE_VALUE_MULTIPLIER * getPieceCentipawnValue(chessSquare);

					int row = chessBoard.getNumOfRows() - 1 - i;
					score -= getPieceSquareValue(row, j, chessSquare, gamePhase);
				}
			}
		}

		return score;
	}

}
