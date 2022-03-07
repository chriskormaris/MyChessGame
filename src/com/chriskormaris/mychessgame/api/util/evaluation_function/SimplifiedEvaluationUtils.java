package com.chriskormaris.mychessgame.api.util.evaluation_function;

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

// Simplified Evaluation Function by Polish chess programmer Tomasz Michniewski.
// see: https://www.chessprogramming.org/Simplified_Evaluation_Function
public class SimplifiedEvaluationUtils {

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

	public static int[][] KING_SQUARES_TABLE_OPENING = new int[][]{
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

	public static final int PAWN_CENTIPAWN_VALUE = 100;
	public static final int KNIGHT_CENTIPAWN_VALUE = 320;
	public static final int BISHOP_CENTIPAWN_VALUE = 330;
	public static final int ROOK_CENTIPAWN_VALUE = 500;
	public static final int QUEEN_CENTIPAWN_VALUE = 900;
	public static final int KING_CENTIPAWN_VALUE = 20000;

	public static GamePhase getGamePhase(ChessBoard chessBoard) {
		if (chessBoard.countQueens(Allegiance.WHITE) == 0 && chessBoard.countQueens(Allegiance.BLACK) == 0
			|| chessBoard.isQueenPlusOneMinorPieceMaximum(Allegiance.WHITE)
				&& chessBoard.isQueenPlusOneMinorPieceMaximum(Allegiance.BLACK)) {
			return GamePhase.ENDGAME;
		} else {
			return GamePhase.OPENING;
		}
	}

	public static int getPieceCentipawnValue(ChessPiece chessPiece) {
		if (chessPiece instanceof Pawn) {
			return PAWN_CENTIPAWN_VALUE;
		} else if (chessPiece instanceof Knight) {
			return KNIGHT_CENTIPAWN_VALUE;
		} else if (chessPiece instanceof Bishop) {
			return BISHOP_CENTIPAWN_VALUE;
		} else if (chessPiece instanceof Rook) {
			return ROOK_CENTIPAWN_VALUE;
		} else if (chessPiece instanceof Queen) {
			return QUEEN_CENTIPAWN_VALUE;
		} else if (chessPiece instanceof King) {
			return KING_CENTIPAWN_VALUE;
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
			if (gamePhase == GamePhase.OPENING) {
				return KING_SQUARES_TABLE_OPENING[row][column];
			} else if (gamePhase == GamePhase.ENDGAME) {
				return KING_SQUARES_TABLE_ENDGAME[row][column];
			}
		}
		return 0;
	}

}
