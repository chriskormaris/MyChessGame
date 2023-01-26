package com.chriskormaris.mychessgame.api.evaluation_function;

import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;

// Shannon's Evaluation Function.
// see: https://www.chessprogramming.org/Evaluation
public class ShannonEvaluationUtils {

	public static final int PAWN_VALUE = 1;
	public static final int KNIGHT_VALUE = 3;
	public static final int BISHOP_VALUE = 3;
	public static final int ROOK_VALUE = 5;
	public static final int QUEEN_VALUE = 9;
	public static final int KING_VALUE = 200;

	public static final double DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER = 0.5;
	public static final double MOBILITY_MULTIPLIER = 0.1;

	public static int getPieceValue(ChessPiece chessPiece) {
		if (chessPiece instanceof Pawn) {
			return PAWN_VALUE;
		} else if (chessPiece instanceof Knight) {
			return KNIGHT_VALUE;
		} else if (chessPiece instanceof Bishop) {
			return BISHOP_VALUE;
		} else if (chessPiece instanceof Rook) {
			return ROOK_VALUE;
		} else if (chessPiece instanceof Queen) {
			return QUEEN_VALUE;
		} else if (chessPiece instanceof King) {
			return KING_VALUE;
		}
		return 0;
	}

}
