package com.chriskormaris.mychessgame.api.evaluation;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GamePhase;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;
import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.api.util.Utilities;

// Wukong Evaluation Function.
// see: https://github.com/maksimKorzh/wukongJS/blob/main/wukong.js
public class WukongEvaluation extends Evaluation {

	private static final int[][] PAWNS_SQUARES_TABLE_OPENING = new int[][]{
			{ 0,  0,  0,   0,   0,   0,  0,  0},
			{-4, 68, 61,  47,  47,  49, 45, -1},
			{ 6, 16, 25,  33,  24,  24, 14, -6},
			{ 0, -1,  9,  28,  20,   8, -1, 11},
			{ 6,  4,  6,  14,  14,  -5,  6, -6},
			{-1, -8, -4,   4,   2, -12, -1,  5},
			{ 5, 16, 16, -14, -14,  13, 15,  8},
			{ 0,  0,  0,   0,   0,   0,  0,  0}
	};

	private static final int[][] PAWNS_SQUARES_TABLE_ENDGAME = new int[][]{
			{ 0,   0,   0,  0,  0,  0,  0,  0},
			{-4, 174, 120, 94, 85, 98, 68,  4},
			{ 6,  48,  44, 45, 31, 38, 37, -6},
			{-6,  -4,  -1, -6,  2, -1, -2, -2},
			{ 2,   2,   5, -3,  0, -5,  4, -3},
			{-2,   0,   1,  5,  0, -1,  0,  1},
			{-2,   5,   6, -6,  0,  3,  4, -4},
			{ 0,   0,   0,  0,  0,  0,  0,  0}
	};

	private static final int[][] KNIGHTS_SQUARES_TABLE_OPENING = new int[][]{
			{-55, -40, -30, -28, -26, -30, -40, -50},
			{-37, -15,   0,  -6,   4,   3, -17, -40},
			{-25,   5,  16,  12,  11,   6,   6, -29},
			{-24,   5,  21,  14,  18,   9,  11, -26},
			{-36,  -5,   9,  23,  24,  21,   2, -24},
			{-32,  -1,   4,  19,  20,   4,  11, -25},
			{-38, -22,   4,  -1,   8,  -5, -18, -34},
			{-50, -46, -32, -24, -36, -25, -34, -50}
	};

	private static final int[][] KNIGHTS_SQUARES_TABLE_ENDGAME = new int[][]{
			{-50, -40, -30, -24, -24, -35, -40, -50},
			{-38, -17,   6,  -5,   5,  -4, -15, -40},
			{-24,   3,  15,   9,  15,  10,  -6, -26},
			{-29,   5,  21,  17,  18,   9,  10, -28},
			{-36,  -5,  18,  16,  14,  20,   5, -26},
			{-32,   7,   5,  20,  11,  15,   9, -27},
			{-43, -20,   5,  -1,   5,   1, -22, -40},
			{-50, -40, -32, -27, -30, -25, -35, -50}
	};

	private static final int[][] BISHOPS_SQUARES_TABLE_OPENING = new int[][]{
			{-16, -15, -12, -5, -10, -12, -10, -20},
			{-13,   5,   6,  1,  -6,  -5,   3,  -6},
			{-16,   6,  -1, 16,   7,  -1,  -6,  -5},
			{-14,  -1,  11, 14,   4,  10,  11, -13},
			{ -4,   5,  12, 16,   4,   6,   2, -16},
			{-15,   4,  14,  8,  16,   4,  16, -15},
			{ -5,   6,   6,  6,   3,   6,   9,  -7},
			{-14,  -4, -15, -4,  -9,  -4, -12, -14}
	};

	private static final int[][] BISHOPS_SQUARES_TABLE_ENDGAME = new int[][]{
			{-14, -13,  -4, -7, -14,  -9, -16, -20},
			{-11,   6,   3, -6,   4,  -3,   5,  -4},
			{-11,  -3,   5, 15,   4,  -1,  -5, -10},
			{ -7,  -1,  11, 16,   5,  11,   7, -13},
			{ -4,   4,  10, 16,   6,  12,   4, -16},
			{ -4,   4,  11, 12,  10,   7,   7, -12},
			{-11,   7,   6,  6,  -3,   2,   1,  -7},
			{-15,  -4, -11, -4, -10, -10,  -6, -17}
	};

	private static final int[][] ROOKS_SQUARES_TABLE_OPENING = new int[][]{
			{  5, -2,  6,  2, -2, -6,  4, -2},
			{  8, 13, 11, 15, 11, 15, 16,  4},
			{ -6,  3,  3,  6,  1, -2,  3, -5},
			{-10,  5, -4, -4, -1, -6,  3, -2},
			{ -4,  3,  5, -2,  4,  1, -5,  1},
			{  0,  1,  1, -3,  5,  6,  1, -9},
			{-10, -1, -4,  0,  5, -6, -6, -9},
			{ -1, -2, -6,  9,  9,  5,  4, -5}
	};

	private static final int[][] ROOKS_SQUARES_TABLE_ENDGAME = new int[][]{
			{  5, -6,  1, -4, -4, -6,  6,  -3},
			{ -6,  4,  2,  5, -1,  3,  4, -15},
			{-15,  3,  3,  0, -1, -6,  5,  -9},
			{-16,  6,  0, -6, -3, -3, -4,  -4},
			{-15,  6,  2, -6,  6,  0, -6, -10},
			{ -6, -1,  3, -2,  6,  5,  0, -15},
			{ -8, -4,  1, -4,  3, -5, -6,  -5},
			{  1,  0, -2,  1,  1,  4,  2,   0}
	};

	private static final int[][] QUEEN_SQUARES_TABLE_OPENING = new int[][]{
			{-25, -9, -11, -3, -7, -13, -10, -17},
			{ -4, -6,   4, -5, -1,   6,   4,  -5},
			{ -8, -5,   2,  0,  7,   6,  -4,  -5},
			{  0, -4,   7, -1,  7,  11,   0,   1},
			{ -6,  4,   7,  1, -1,   2,  -6,  -2},
			{-15, 11,  11, 11,  4,  11,   6, -15},
			{ -5, -6,   1, -6,  3,  -3,   3, -10},
			{-15, -4, -13, -8, -3, -16,  -8, -24}
	};

	private static final int[][] QUEEN_SQUARES_TABLE_ENDGAME = new int[][]{
			{-21, -7, -6,  1, -8, -15, -10, -16},
			{ -4, -5,  3, -4,  2,   6,   3, -10},
			{-13, -2,  7,  2,  6,  10,  -4,  -6},
			{ -1, -4,  3,  1,  8,   8,  -2,  -2},
			{  0,  6,  8,  1, -1,   1,   0,  -3},
			{-11, 10,  6,  3,  7,   9,   4, -10},
			{-12, -6,  5,  0,  0,  -5,   4, -10},
			{-20, -6, -7, -7, -4, -12,  -9, -20}
	};

	private static final int[][] KING_SQUARES_TABLE_OPENING = new int[][]{
			{-30, -40, -40, -50, -50, -40, -40, -30},
			{-30, -37, -43, -49, -50, -39, -40, -30},
			{-32, -41, -40, -46, -49, -40, -46, -30},
			{-32, -38, -39, -52, -54, -39, -39, -30},
			{-20, -33, -29, -42, -44, -29, -30, -19},
			{-10, -18, -17, -20, -22, -21, -20, -13},
			{ 14,  18,  -1,  -1,   4,  -1,  15,  14},
			{ 21,  35,  11,  6,    1,  14,  32,  22}
	};

	private static final int[][] KING_SQUARES_TABLE_ENDGAME = new int[][]{
			{-50, -40, -30, -20, -20, -30, -40, -50},
			{-30, -18, -15,   6,   3,  -6, -24, -30},
			{-35, -16,  20,  32,  34,  14, -11, -30},
			{-34,  -5,  24,  35,  34,  35, -16, -35},
			{-36,  -7,  31,  34,  34,  34, -12, -31},
			{-30,  -7,  14,  33,  36,  16, -13, -33},
			{-36, -27,   5,   2,   5,  -1, -31, -33},
			{-48, -26, -26, -26, -28, -25, -30, -51}
	};

	private static final int WHITE_PAWN_OPENING_CENTIPAWN_VALUE = 89;
	private static final int WHITE_KNIGHT_OPENING_CENTIPAWN_VALUE = 308;
	private static final int WHITE_BISHOP_OPENING_CENTIPAWN_VALUE = 319;
	private static final int WHITE_ROOK_OPENING_CENTIPAWN_VALUE = 488;
	private static final int WHITE_QUEEN_OPENING_CENTIPAWN_VALUE = 888;
	private static final int WHITE_KING_OPENING_CENTIPAWN_VALUE = 20001;

	private static final int BLACK_PAWN_OPENING_CENTIPAWN_VALUE = 92;
	private static final int BLACK_KNIGHT_OPENING_CENTIPAWN_VALUE = 307;
	private static final int BLACK_BISHOP_OPENING_CENTIPAWN_VALUE = 323;
	private static final int BLACK_ROOK_OPENING_CENTIPAWN_VALUE = 492;
	private static final int BLACK_QUEEN_OPENING_CENTIPAWN_VALUE = 888;
	private static final int BLACK_KING_OPENING_CENTIPAWN_VALUE = 20002;

	private static final int WHITE_PAWN_ENDGAME_CENTIPAWN_VALUE = 96;
	private static final int WHITE_KNIGHT_ENDGAME_CENTIPAWN_VALUE = 319;
	private static final int WHITE_BISHOP_ENDGAME_CENTIPAWN_VALUE = 331;
	private static final int WHITE_ROOK_ENDGAME_CENTIPAWN_VALUE = 497;
	private static final int WHITE_QUEEN_ENDGAME_CENTIPAWN_VALUE = 853;
	private static final int WHITE_KING_ENDGAME_CENTIPAWN_VALUE = 19998;

	private static final int BLACK_PAWN_ENDGAME_CENTIPAWN_VALUE = 102;
	private static final int BLACK_KNIGHT_ENDGAME_CENTIPAWN_VALUE = 318;
	private static final int BLACK_BISHOP_ENDGAME_CENTIPAWN_VALUE = 334;
	private static final int BLACK_ROOK_ENDGAME_CENTIPAWN_VALUE = 501;
	private static final int BLACK_QUEEN_ENDGAME_CENTIPAWN_VALUE = 845;
	private static final int BLACK_KING_ENDGAME_CENTIPAWN_VALUE = 20000;

	private int getPieceCentipawnValue(ChessPiece chessPiece, GamePhase gamePhase) {
		if (gamePhase == GamePhase.OPENING) {
			if (chessPiece.getAllegiance() == Allegiance.WHITE) {
				if (chessPiece instanceof Pawn) {
					return WHITE_PAWN_OPENING_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Knight) {
					return WHITE_KNIGHT_OPENING_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Bishop) {
					return WHITE_BISHOP_OPENING_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Rook) {
					return WHITE_ROOK_OPENING_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Queen) {
					return WHITE_QUEEN_OPENING_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof King) {
					return WHITE_KING_OPENING_CENTIPAWN_VALUE;
				}
			} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
				if (chessPiece instanceof Pawn) {
					return BLACK_PAWN_OPENING_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Knight) {
					return BLACK_KNIGHT_OPENING_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Bishop) {
					return BLACK_BISHOP_OPENING_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Rook) {
					return BLACK_ROOK_OPENING_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Queen) {
					return BLACK_QUEEN_OPENING_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof King) {
					return BLACK_KING_OPENING_CENTIPAWN_VALUE;
				}
			}
		} else if (gamePhase == GamePhase.ENDGAME) {
			if (chessPiece.getAllegiance() == Allegiance.WHITE) {
				if (chessPiece instanceof Pawn) {
					return WHITE_PAWN_ENDGAME_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Knight) {
					return WHITE_KNIGHT_ENDGAME_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Bishop) {
					return WHITE_BISHOP_ENDGAME_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Rook) {
					return WHITE_ROOK_ENDGAME_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Queen) {
					return WHITE_QUEEN_ENDGAME_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof King) {
					return WHITE_KING_ENDGAME_CENTIPAWN_VALUE;
				}
			} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
				if (chessPiece instanceof Pawn) {
					return BLACK_PAWN_ENDGAME_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Knight) {
					return BLACK_KNIGHT_ENDGAME_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Bishop) {
					return BLACK_BISHOP_ENDGAME_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Rook) {
					return BLACK_ROOK_ENDGAME_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof Queen) {
					return BLACK_QUEEN_ENDGAME_CENTIPAWN_VALUE;
				} else if (chessPiece instanceof King) {
					return BLACK_KING_ENDGAME_CENTIPAWN_VALUE;
				}
			}
		}
		return 0;
	}

	private int getOpeningGamePieceSquareValue(int row, int column, ChessPiece chessPiece) {
		if (chessPiece instanceof Pawn) {
			return PAWNS_SQUARES_TABLE_OPENING[row][column];
		} else if (chessPiece instanceof Knight) {
			return KNIGHTS_SQUARES_TABLE_OPENING[row][column];
		} else if (chessPiece instanceof Bishop) {
			return BISHOPS_SQUARES_TABLE_OPENING[row][column];
		} else if (chessPiece instanceof Rook) {
			return ROOKS_SQUARES_TABLE_OPENING[row][column];
		} else if (chessPiece instanceof Queen) {
			return QUEEN_SQUARES_TABLE_OPENING[row][column];
		} else if (chessPiece instanceof King) {
			return KING_SQUARES_TABLE_OPENING[row][column];
		}
		return 0;
	}

	private int getEndgamePieceSquareValue(int row, int column, ChessPiece chessPiece) {
		if (chessPiece instanceof Pawn) {
			return PAWNS_SQUARES_TABLE_ENDGAME[row][column];
		} else if (chessPiece instanceof Knight) {
			return KNIGHTS_SQUARES_TABLE_ENDGAME[row][column];
		} else if (chessPiece instanceof Bishop) {
			return BISHOPS_SQUARES_TABLE_ENDGAME[row][column];
		} else if (chessPiece instanceof Rook) {
			return ROOKS_SQUARES_TABLE_ENDGAME[row][column];
		} else if (chessPiece instanceof Queen) {
			return QUEEN_SQUARES_TABLE_ENDGAME[row][column];
		} else if (chessPiece instanceof King) {
			return KING_SQUARES_TABLE_ENDGAME[row][column];
		}
		return 0;
	}

	private int getPieceSquareValue(int row, int column, ChessPiece chessPiece, GamePhase gamePhase) {
		if (gamePhase == GamePhase.OPENING) {
			return getOpeningGamePieceSquareValue(row, column, chessPiece);
		} else {
			return getEndgamePieceSquareValue(row, column, chessPiece);
		}
	}

	// Wukong Evaluation Function.
	@Override
	public double evaluate(ChessBoard chessBoard) {
		int gamePhase = 0;
		int openingScore = 0;
		int endgameScore = 0;

		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < Constants.NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = chessBoard.getGameBoard()[i][j];
				gamePhase += Utilities.getPieceGamePhaseValue(chessPiece);

				if (chessPiece.getAllegiance() == Allegiance.WHITE) {
					openingScore += 2 * getPieceCentipawnValue(chessPiece, GamePhase.OPENING);
					endgameScore += 2 * getPieceCentipawnValue(chessPiece, GamePhase.ENDGAME);

					openingScore += getPieceSquareValue(i, j, chessPiece, GamePhase.OPENING);
					endgameScore += getPieceSquareValue(i, j, chessPiece, GamePhase.ENDGAME);
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
					openingScore -= 2 * getPieceCentipawnValue(chessPiece, GamePhase.OPENING);
					endgameScore -= 2 * getPieceCentipawnValue(chessPiece, GamePhase.ENDGAME);

					int row = chessBoard.getNumOfRows() - 1 - i;
					openingScore -= getPieceSquareValue(row, j, chessPiece, GamePhase.OPENING);
					endgameScore -= getPieceSquareValue(row, j, chessPiece, GamePhase.ENDGAME);
				}
			}
		}

		// In case of early promotion, the "gamePhase" value could be more than 24.
		int openingGamePhase = Math.min(gamePhase, 24);
		int endGamePhase = 24 - openingGamePhase;
		return (openingScore * openingGamePhase + endgameScore * endGamePhase) / 24.0;
	}

}
