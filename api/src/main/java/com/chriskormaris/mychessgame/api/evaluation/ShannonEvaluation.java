package com.chriskormaris.mychessgame.api.evaluation;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.square.Pawn;
import com.chriskormaris.mychessgame.api.square.PieceUtils;
import com.chriskormaris.mychessgame.api.util.Constants;

// Shannon's Evaluation Function.
// see: https://www.chessprogramming.org/Evaluation
public class ShannonEvaluation implements Evaluation {

	private static final int PAWN_VALUE = 1;
	private static final int KNIGHT_VALUE = 3;
	private static final int BISHOP_VALUE = 3;
	private static final int ROOK_VALUE = 5;
	private static final int QUEEN_VALUE = 9;
	private static final int KING_VALUE = 200;

	private static final double DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER = 0.5;
	private static final double MOBILITY_MULTIPLIER = 0.1;

	private int getPieceValue(byte chessSquare) {
		if (Math.abs(chessSquare) == Constants.PAWN) {
			return PAWN_VALUE;
		} else if (Math.abs(chessSquare) == Constants.KNIGHT) {
			return KNIGHT_VALUE;
		} else if (Math.abs(chessSquare) == Constants.BISHOP) {
			return BISHOP_VALUE;
		} else if (Math.abs(chessSquare) == Constants.ROOK) {
			return ROOK_VALUE;
		} else if (Math.abs(chessSquare) == Constants.QUEEN) {
			return QUEEN_VALUE;
		} else if (Math.abs(chessSquare) == Constants.KING) {
			return KING_VALUE;
		}
		return 0;
	}

	// Shannon's Evaluation Function.
	@Override
	public double evaluate(ChessBoard chessBoard) {
		double score = 0;

		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				byte chessSquare = chessBoard.getGameBoard()[i][j];

				String position = chessBoard.getPositionByRowCol(i, j);
				int numberOfLegalMoves = PieceUtils.getNextPositions(position, chessBoard, false).size();

				if (chessSquare > 0) {
					score += PIECE_VALUE_MULTIPLIER * getPieceValue(chessSquare);
					score += MOBILITY_MULTIPLIER * numberOfLegalMoves;
				} else if (chessSquare < 0) {
					score -= PIECE_VALUE_MULTIPLIER * getPieceValue(chessSquare);
					score -= MOBILITY_MULTIPLIER * numberOfLegalMoves;
				}

				if (Math.abs(chessSquare) == Constants.PAWN) {
					byte pawn = chessSquare;
					if (Pawn.isDoubledPawn(position, chessBoard) || Pawn.isBlockedPawn(position, chessBoard)
							|| Pawn.isIsolatedPawn(position, chessBoard)) {
						if (pawn > 0) {
							score -= DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER;
						} else if (pawn < 0) {
							score += DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER;
						}
					}
				}

			}
		}

		return score;
	}

}
