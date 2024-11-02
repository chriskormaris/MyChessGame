package com.chriskormaris.mychessgame.api.evaluation;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.GamePhase;
import com.chriskormaris.mychessgame.api.square.ChessSquare;

// Wukong Evaluation Function.
// see: https://github.com/maksimKorzh/wukongJS/blob/main/wukong.js
public class WukongEvaluation implements Evaluation {

	private static final int[][] OPENING_PAWN_SQUARE_TABLE = new int[][]{
			{ 0,  0,  0,   0,   0,   0,  0,  0},
			{-4, 68, 61,  47,  47,  49, 45, -1},
			{ 6, 16, 25,  33,  24,  24, 14, -6},
			{ 0, -1,  9,  28,  20,   8, -1, 11},
			{ 6,  4,  6,  14,  14,  -5,  6, -6},
			{-1, -8, -4,   4,   2, -12, -1,  5},
			{ 5, 16, 16, -14, -14,  13, 15,  8},
			{ 0,  0,  0,   0,   0,   0,  0,  0}
	};

	private static final int[][] ENDGAME_PAWN_SQUARE_TABLE = new int[][]{
			{ 0,   0,   0,  0,  0,  0,  0,  0},
			{-4, 174, 120, 94, 85, 98, 68,  4},
			{ 6,  48,  44, 45, 31, 38, 37, -6},
			{-6,  -4,  -1, -6,  2, -1, -2, -2},
			{ 2,   2,   5, -3,  0, -5,  4, -3},
			{-2,   0,   1,  5,  0, -1,  0,  1},
			{-2,   5,   6, -6,  0,  3,  4, -4},
			{ 0,   0,   0,  0,  0,  0,  0,  0}
	};

	private static final int[][] OPENING_KNIGHT_SQUARE_TABLE = new int[][]{
			{-55, -40, -30, -28, -26, -30, -40, -50},
			{-37, -15,   0,  -6,   4,   3, -17, -40},
			{-25,   5,  16,  12,  11,   6,   6, -29},
			{-24,   5,  21,  14,  18,   9,  11, -26},
			{-36,  -5,   9,  23,  24,  21,   2, -24},
			{-32,  -1,   4,  19,  20,   4,  11, -25},
			{-38, -22,   4,  -1,   8,  -5, -18, -34},
			{-50, -46, -32, -24, -36, -25, -34, -50}
	};

	private static final int[][] ENDGAME_KNIGHT_SQUARE_TABLE = new int[][]{
			{-50, -40, -30, -24, -24, -35, -40, -50},
			{-38, -17,   6,  -5,   5,  -4, -15, -40},
			{-24,   3,  15,   9,  15,  10,  -6, -26},
			{-29,   5,  21,  17,  18,   9,  10, -28},
			{-36,  -5,  18,  16,  14,  20,   5, -26},
			{-32,   7,   5,  20,  11,  15,   9, -27},
			{-43, -20,   5,  -1,   5,   1, -22, -40},
			{-50, -40, -32, -27, -30, -25, -35, -50}
	};

	private static final int[][] OPENING_BISHOP_SQUARE_TABLE = new int[][]{
			{-16, -15, -12, -5, -10, -12, -10, -20},
			{-13,   5,   6,  1,  -6,  -5,   3,  -6},
			{-16,   6,  -1, 16,   7,  -1,  -6,  -5},
			{-14,  -1,  11, 14,   4,  10,  11, -13},
			{ -4,   5,  12, 16,   4,   6,   2, -16},
			{-15,   4,  14,  8,  16,   4,  16, -15},
			{ -5,   6,   6,  6,   3,   6,   9,  -7},
			{-14,  -4, -15, -4,  -9,  -4, -12, -14}
	};

	private static final int[][] ENDGAME_BISHOP_SQUARE_TABLE = new int[][]{
			{-14, -13,  -4, -7, -14,  -9, -16, -20},
			{-11,   6,   3, -6,   4,  -3,   5,  -4},
			{-11,  -3,   5, 15,   4,  -1,  -5, -10},
			{ -7,  -1,  11, 16,   5,  11,   7, -13},
			{ -4,   4,  10, 16,   6,  12,   4, -16},
			{ -4,   4,  11, 12,  10,   7,   7, -12},
			{-11,   7,   6,  6,  -3,   2,   1,  -7},
			{-15,  -4, -11, -4, -10, -10,  -6, -17}
	};

	private static final int[][] OPENING_ROOK_SQUARE_TABLE = new int[][]{
			{  5, -2,  6,  2, -2, -6,  4, -2},
			{  8, 13, 11, 15, 11, 15, 16,  4},
			{ -6,  3,  3,  6,  1, -2,  3, -5},
			{-10,  5, -4, -4, -1, -6,  3, -2},
			{ -4,  3,  5, -2,  4,  1, -5,  1},
			{  0,  1,  1, -3,  5,  6,  1, -9},
			{-10, -1, -4,  0,  5, -6, -6, -9},
			{ -1, -2, -6,  9,  9,  5,  4, -5}
	};

	private static final int[][] ENDGAME_ROOK_SQUARE_TABLE = new int[][]{
			{  5, -6,  1, -4, -4, -6,  6,  -3},
			{ -6,  4,  2,  5, -1,  3,  4, -15},
			{-15,  3,  3,  0, -1, -6,  5,  -9},
			{-16,  6,  0, -6, -3, -3, -4,  -4},
			{-15,  6,  2, -6,  6,  0, -6, -10},
			{ -6, -1,  3, -2,  6,  5,  0, -15},
			{ -8, -4,  1, -4,  3, -5, -6,  -5},
			{  1,  0, -2,  1,  1,  4,  2,   0}
	};

	private static final int[][] OPENING_QUEEN_SQUARE_TABLE = new int[][]{
			{-25, -9, -11, -3, -7, -13, -10, -17},
			{ -4, -6,   4, -5, -1,   6,   4,  -5},
			{ -8, -5,   2,  0,  7,   6,  -4,  -5},
			{  0, -4,   7, -1,  7,  11,   0,   1},
			{ -6,  4,   7,  1, -1,   2,  -6,  -2},
			{-15, 11,  11, 11,  4,  11,   6, -15},
			{ -5, -6,   1, -6,  3,  -3,   3, -10},
			{-15, -4, -13, -8, -3, -16,  -8, -24}
	};

	private static final int[][] ENDGAME_QUEEN_SQUARE_TABLE = new int[][]{
			{-21, -7, -6,  1, -8, -15, -10, -16},
			{ -4, -5,  3, -4,  2,   6,   3, -10},
			{-13, -2,  7,  2,  6,  10,  -4,  -6},
			{ -1, -4,  3,  1,  8,   8,  -2,  -2},
			{  0,  6,  8,  1, -1,   1,   0,  -3},
			{-11, 10,  6,  3,  7,   9,   4, -10},
			{-12, -6,  5,  0,  0,  -5,   4, -10},
			{-20, -6, -7, -7, -4, -12,  -9, -20}
	};

	private static final int[][] OPENING_KING_SQUARE_TABLE = new int[][]{
			{-30, -40, -40, -50, -50, -40, -40, -30},
			{-30, -37, -43, -49, -50, -39, -40, -30},
			{-32, -41, -40, -46, -49, -40, -46, -30},
			{-32, -38, -39, -52, -54, -39, -39, -30},
			{-20, -33, -29, -42, -44, -29, -30, -19},
			{-10, -18, -17, -20, -22, -21, -20, -13},
			{ 14,  18,  -1,  -1,   4,  -1,  15,  14},
			{ 21,  35,  11,  6,    1,  14,  32,  22}
	};

	private static final int[][] ENDGAME_KING_SQUARE_TABLE = new int[][]{
			{-50, -40, -30, -20, -20, -30, -40, -50},
			{-30, -18, -15,   6,   3,  -6, -24, -30},
			{-35, -16,  20,  32,  34,  14, -11, -30},
			{-34,  -5,  24,  35,  34,  35, -16, -35},
			{-36,  -7,  31,  34,  34,  34, -12, -31},
			{-30,  -7,  14,  33,  36,  16, -13, -33},
			{-36, -27,   5,   2,   5,  -1, -31, -33},
			{-48, -26, -26, -26, -28, -25, -30, -51}
	};

	private static final int OPENING_WHITE_PAWN_CENTIPAWN_VALUE = 89;
	private static final int OPENING_WHITE_KNIGHT_CENTIPAWN_VALUE = 308;
	private static final int OPENING_WHITE_BISHOP_CENTIPAWN_VALUE = 319;
	private static final int OPENING_WHITE_ROOK_CENTIPAWN_VALUE = 488;
	private static final int OPENING_WHITE_QUEEN_CENTIPAWN_VALUE = 888;
	private static final int OPENING_WHITE_KING_CENTIPAWN_VALUE = 20001;

	private static final int OPENING_BLACK_PAWN_CENTIPAWN_VALUE = 92;
	private static final int OPENING_BLACK_KNIGHT_CENTIPAWN_VALUE = 307;
	private static final int OPENING_BLACK_BISHOP_CENTIPAWN_VALUE = 323;
	private static final int OPENING_BLACK_ROOK_CENTIPAWN_VALUE = 492;
	private static final int OPENING_BLACK_QUEEN_CENTIPAWN_VALUE = 888;
	private static final int OPENING_BLACK_KING_CENTIPAWN_VALUE = 20002;

	private static final int ENDGAME_WHITE_PAWN_CENTIPAWN_VALUE = 96;
	private static final int ENDGAME_WHITE_KNIGHT_CENTIPAWN_VALUE = 319;
	private static final int ENDGAME_WHITE_BISHOP_CENTIPAWN_VALUE = 331;
	private static final int ENDGAME_WHITE_ROOK_CENTIPAWN_VALUE = 497;
	private static final int ENDGAME_WHITE_QUEEN_CENTIPAWN_VALUE = 853;
	private static final int ENDGAME_WHITE_KING_CENTIPAWN_VALUE = 19998;

	private static final int ENDGAME_BLACK_PAWN_CENTIPAWN_VALUE = 102;
	private static final int ENDGAME_BLACK_KNIGHT_CENTIPAWN_VALUE = 318;
	private static final int ENDGAME_BLACK_BISHOP_CENTIPAWN_VALUE = 334;
	private static final int ENDGAME_BLACK_ROOK_CENTIPAWN_VALUE = 501;
	private static final int ENDGAME_BLACK_QUEEN_CENTIPAWN_VALUE = 845;
	private static final int ENDGAME_BLACK_KING_CENTIPAWN_VALUE = 20000;

	private int getPieceCentipawnValue(ChessSquare chessSquare, GamePhase gamePhase) {
		if (gamePhase == GamePhase.OPENING_MIDDLEGAME) {
			if (chessSquare.isWhite()) {
				if (chessSquare.isPawn()) {
					return OPENING_WHITE_PAWN_CENTIPAWN_VALUE;
				} else if (chessSquare.isKnight()) {
					return OPENING_WHITE_KNIGHT_CENTIPAWN_VALUE;
				} else if (chessSquare.isBishop()) {
					return OPENING_WHITE_BISHOP_CENTIPAWN_VALUE;
				} else if (chessSquare.isRook()) {
					return OPENING_WHITE_ROOK_CENTIPAWN_VALUE;
				} else if (chessSquare.isQueen()) {
					return OPENING_WHITE_QUEEN_CENTIPAWN_VALUE;
				} else if (chessSquare.isKing()) {
					return OPENING_WHITE_KING_CENTIPAWN_VALUE;
				}
			} else if (chessSquare.isBlack()) {
				if (chessSquare.isPawn()) {
					return OPENING_BLACK_PAWN_CENTIPAWN_VALUE;
				} else if (chessSquare.isKnight()) {
					return OPENING_BLACK_KNIGHT_CENTIPAWN_VALUE;
				} else if (chessSquare.isBishop()) {
					return OPENING_BLACK_BISHOP_CENTIPAWN_VALUE;
				} else if (chessSquare.isRook()) {
					return OPENING_BLACK_ROOK_CENTIPAWN_VALUE;
				} else if (chessSquare.isQueen()) {
					return OPENING_BLACK_QUEEN_CENTIPAWN_VALUE;
				} else if (chessSquare.isKing()) {
					return OPENING_BLACK_KING_CENTIPAWN_VALUE;
				}
			}
		} else if (gamePhase == GamePhase.ENDGAME) {
			if (chessSquare.isWhite()) {
				if (chessSquare.isPawn()) {
					return ENDGAME_WHITE_PAWN_CENTIPAWN_VALUE;
				} else if (chessSquare.isKnight()) {
					return ENDGAME_WHITE_KNIGHT_CENTIPAWN_VALUE;
				} else if (chessSquare.isBishop()) {
					return ENDGAME_WHITE_BISHOP_CENTIPAWN_VALUE;
				} else if (chessSquare.isRook()) {
					return ENDGAME_WHITE_ROOK_CENTIPAWN_VALUE;
				} else if (chessSquare.isQueen()) {
					return ENDGAME_WHITE_QUEEN_CENTIPAWN_VALUE;
				} else if (chessSquare.isKing()) {
					return ENDGAME_WHITE_KING_CENTIPAWN_VALUE;
				}
			} else if (chessSquare.isBlack()) {
				if (chessSquare.isPawn()) {
					return ENDGAME_BLACK_PAWN_CENTIPAWN_VALUE;
				} else if (chessSquare.isKnight()) {
					return ENDGAME_BLACK_KNIGHT_CENTIPAWN_VALUE;
				} else if (chessSquare.isBishop()) {
					return ENDGAME_BLACK_BISHOP_CENTIPAWN_VALUE;
				} else if (chessSquare.isRook()) {
					return ENDGAME_BLACK_ROOK_CENTIPAWN_VALUE;
				} else if (chessSquare.isQueen()) {
					return ENDGAME_BLACK_QUEEN_CENTIPAWN_VALUE;
				} else if (chessSquare.isKing()) {
					return ENDGAME_BLACK_KING_CENTIPAWN_VALUE;
				}
			}
		}
		return 0;
	}

	private int getOpeningPieceSquareValue(int row, int column, ChessSquare chessSquare) {
		if (chessSquare.isPawn()) {
			return OPENING_PAWN_SQUARE_TABLE[row][column];
		} else if (chessSquare.isKnight()) {
			return OPENING_KNIGHT_SQUARE_TABLE[row][column];
		} else if (chessSquare.isBishop()) {
			return OPENING_BISHOP_SQUARE_TABLE[row][column];
		} else if (chessSquare.isRook()) {
			return OPENING_ROOK_SQUARE_TABLE[row][column];
		} else if (chessSquare.isQueen()) {
			return OPENING_QUEEN_SQUARE_TABLE[row][column];
		} else if (chessSquare.isKing()) {
			return OPENING_KING_SQUARE_TABLE[row][column];
		}
		return 0;
	}

	private int getEndgamePieceSquareValue(int row, int column, ChessSquare chessSquare) {
		if (chessSquare.isPawn()) {
			return ENDGAME_PAWN_SQUARE_TABLE[row][column];
		} else if (chessSquare.isKnight()) {
			return ENDGAME_KNIGHT_SQUARE_TABLE[row][column];
		} else if (chessSquare.isBishop()) {
			return ENDGAME_BISHOP_SQUARE_TABLE[row][column];
		} else if (chessSquare.isRook()) {
			return ENDGAME_ROOK_SQUARE_TABLE[row][column];
		} else if (chessSquare.isQueen()) {
			return ENDGAME_QUEEN_SQUARE_TABLE[row][column];
		} else if (chessSquare.isKing()) {
			return ENDGAME_KING_SQUARE_TABLE[row][column];
		}
		return 0;
	}

	private int getPieceSquareValue(int row, int column, ChessSquare chessSquare, GamePhase gamePhase) {
		if (gamePhase == GamePhase.OPENING_MIDDLEGAME) {
			return getOpeningPieceSquareValue(row, column, chessSquare);
		} else {
			return getEndgamePieceSquareValue(row, column, chessSquare);
		}
	}

	// Wukong Evaluation Function.
	@Override
	public double evaluate(ChessBoard chessBoard) {
		int gamePhase = 0;
		int openingScore = 0;
		int endgameScore = 0;

		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				ChessSquare chessSquare = chessBoard.getGameBoard()[i][j];
				gamePhase += getPieceGamePhaseValue(chessSquare);

				if (chessSquare.isWhite()) {
					openingScore += PIECE_VALUE_MULTIPLIER * getPieceCentipawnValue(chessSquare, GamePhase.OPENING_MIDDLEGAME);
					endgameScore += PIECE_VALUE_MULTIPLIER * getPieceCentipawnValue(chessSquare, GamePhase.ENDGAME);

					openingScore += getPieceSquareValue(i, j, chessSquare, GamePhase.OPENING_MIDDLEGAME);
					endgameScore += getPieceSquareValue(i, j, chessSquare, GamePhase.ENDGAME);
				} else if (chessSquare.isBlack()) {
					openingScore -= PIECE_VALUE_MULTIPLIER * getPieceCentipawnValue(chessSquare, GamePhase.OPENING_MIDDLEGAME);
					endgameScore -= PIECE_VALUE_MULTIPLIER * getPieceCentipawnValue(chessSquare, GamePhase.ENDGAME);

					int row = chessBoard.getNumOfRows() - 1 - i;
					openingScore -= getPieceSquareValue(row, j, chessSquare, GamePhase.OPENING_MIDDLEGAME);
					endgameScore -= getPieceSquareValue(row, j, chessSquare, GamePhase.ENDGAME);
				}
			}
		}

		// In case of early promotion, the "gamePhase" value could be more than 24.
		int openingPhase = Math.min(gamePhase, 24);
		int endgamePhase = 24 - openingPhase;
		return (openingScore * openingPhase + endgameScore * endgamePhase) / 24.0;
	}

}
