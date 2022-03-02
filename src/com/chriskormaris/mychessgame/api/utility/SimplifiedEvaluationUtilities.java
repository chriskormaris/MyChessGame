package com.chriskormaris.mychessgame.api.utility;

import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.GamePhase;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;

// Simplified Evaluation Function.
// see: https://www.chessprogramming.org/Simplified_Evaluation_Function
// also see: https://github.com/maksimKorzh/wukongJS/blob/main/wukong.js
public class SimplifiedEvaluationUtilities {

	public static int[][] PAWNS_SQUARES_TABLE = new int[][]{
			{0,   0,   0,   0,   0,   0,  0,  0},
			{50, 50,  50,  50,  50,  50, 50, 50},
			{10, 10,  20,  30,  30,  20, 10, 10},
			{5,   5,  10,  25,  25,  10,  5,  5},
			{0,   0,   0,  20,  20,   0,  0,  0},
			{5,  -5, -10,   0,   0, -10, -5,  5},
			{5,  10,  10, -20, -20,  10, 10,  5},
			{0,   0,   0,   0,   0,   0,  0,  0}
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

	public static final int WHITE_PAWN_MIDDLE_GAME_VALUE = 89;
	public static final int WHITE_KNIGHT_MIDDLE_GAME_VALUE = 308;
	public static final int WHITE_BISHOP_MIDDLE_GAME_VALUE = 319;
	public static final int WHITE_ROOK_MIDDLE_GAME_VALUE = 488;
	public static final int WHITE_QUEEN_MIDDLE_GAME_VALUE = 888;
	public static final int WHITE_KING_MIDDLE_GAME_VALUE = 20001;

	public static final int BLACK_PAWN_MIDDLE_GAME_VALUE = 92;
	public static final int BLACK_KNIGHT_MIDDLE_GAME_VALUE = 307;
	public static final int BLACK_BISHOP_MIDDLE_GAME_VALUE = 323;
	public static final int BLACK_ROOK_MIDDLE_GAME_VALUE = 492;
	public static final int BLACK_QUEEN_MIDDLE_GAME_VALUE = 888;
	public static final int BLACK_KING_MIDDLE_GAME_VALUE = 20002;

	public static final int WHITE_PAWN_ENDGAME_VALUE = 96;
	public static final int WHITE_KNIGHT_ENDGAME_VALUE = 319;
	public static final int WHITE_BISHOP_ENDGAME_VALUE = 331;
	public static final int WHITE_ROOK_ENDGAME_VALUE = 497;
	public static final int WHITE_QUEEN_ENDGAME_VALUE = 853;
	public static final int WHITE_KING_ENDGAME_VALUE = 19998;

	public static final int BLACK_PAWN_ENDGAME_VALUE = 102;
	public static final int BLACK_KNIGHT_ENDGAME_VALUE = 318;
	public static final int BLACK_BISHOP_ENDGAME_VALUE = 334;
	public static final int BLACK_ROOK_ENDGAME_VALUE = 501;
	public static final int BLACK_QUEEN_ENDGAME_VALUE = 845;
	public static final int BLACK_KING_ENDGAME_VALUE = 20000;

	public static int getPieceValue(ChessPiece chessPiece, GamePhase gamePhase) {
		if (gamePhase == GamePhase.MIDDLE_GAME) {
			if (chessPiece.getAllegiance() == Allegiance.WHITE) {
				if (chessPiece instanceof Pawn) {
					return WHITE_PAWN_MIDDLE_GAME_VALUE;
				} else if (chessPiece instanceof Knight) {
					return WHITE_KNIGHT_MIDDLE_GAME_VALUE;
				} else if (chessPiece instanceof Bishop) {
					return WHITE_BISHOP_MIDDLE_GAME_VALUE;
				} else if (chessPiece instanceof Rook) {
					return WHITE_ROOK_MIDDLE_GAME_VALUE;
				} else if (chessPiece instanceof Queen) {
					return WHITE_QUEEN_MIDDLE_GAME_VALUE;
				} else if (chessPiece instanceof King) {
					return WHITE_KING_MIDDLE_GAME_VALUE;
				}
			} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
				if (chessPiece instanceof Pawn) {
					return BLACK_PAWN_MIDDLE_GAME_VALUE;
				} else if (chessPiece instanceof Knight) {
					return BLACK_KNIGHT_MIDDLE_GAME_VALUE;
				} else if (chessPiece instanceof Bishop) {
					return BLACK_BISHOP_MIDDLE_GAME_VALUE;
				} else if (chessPiece instanceof Rook) {
					return BLACK_ROOK_MIDDLE_GAME_VALUE;
				} else if (chessPiece instanceof Queen) {
					return BLACK_QUEEN_MIDDLE_GAME_VALUE;
				} else if (chessPiece instanceof King) {
					return BLACK_KING_MIDDLE_GAME_VALUE;
				}
			}
		}
		else if (gamePhase == GamePhase.ENDGAME) {
			if (chessPiece.getAllegiance() == Allegiance.WHITE) {
				if (chessPiece instanceof Pawn) {
					return WHITE_PAWN_ENDGAME_VALUE;
				} else if (chessPiece instanceof Knight) {
					return WHITE_KNIGHT_ENDGAME_VALUE;
				} else if (chessPiece instanceof Bishop) {
					return WHITE_BISHOP_ENDGAME_VALUE;
				} else if (chessPiece instanceof Rook) {
					return WHITE_ROOK_ENDGAME_VALUE;
				} else if (chessPiece instanceof Queen) {
					return WHITE_QUEEN_ENDGAME_VALUE;
				} else if (chessPiece instanceof King) {
					return WHITE_KING_ENDGAME_VALUE;
				}
			} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
				if (chessPiece instanceof Pawn) {
					return BLACK_PAWN_ENDGAME_VALUE;
				} else if (chessPiece instanceof Knight) {
					return BLACK_KNIGHT_ENDGAME_VALUE;
				} else if (chessPiece instanceof Bishop) {
					return BLACK_BISHOP_ENDGAME_VALUE;
				} else if (chessPiece instanceof Rook) {
					return BLACK_ROOK_ENDGAME_VALUE;
				} else if (chessPiece instanceof Queen) {
					return BLACK_QUEEN_ENDGAME_VALUE;
				} else if (chessPiece instanceof King) {
					return BLACK_KING_ENDGAME_VALUE;
				}
			}
		}
		return 0;
	}

	public static int getPieceSquareValue(int row, int column, ChessPiece chessPiece, GamePhase gamePhase) {
		if (chessPiece instanceof Pawn) {
			return PAWNS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Knight) {
			return KNIGHTS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Bishop) {
			return BISHOPS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Rook) {
			return ROOKS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Queen) {
			return QUEEN_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof King) {
			if (gamePhase == GamePhase.MIDDLE_GAME) {
				return KING_SQUARES_TABLE_MIDDLE_GAME[row][column];
			} else if (gamePhase == GamePhase.ENDGAME) {
				return KING_SQUARES_TABLE_ENDGAME[row][column];
			}
		}
		return 0;
	}

}
