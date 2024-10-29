package com.chriskormaris.mychessgame.api.evaluation;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.square.ChessSquare;
import com.chriskormaris.mychessgame.api.square.Pawn;

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

	private int getPieceValue(ChessSquare square) {
		if (square.isPawn()) {
			return PAWN_VALUE;
		} else if (square.isKnight()) {
			return KNIGHT_VALUE;
		} else if (square.isBishop()) {
			return BISHOP_VALUE;
		} else if (square.isRook()) {
			return ROOK_VALUE;
		} else if (square.isQueen()) {
			return QUEEN_VALUE;
		} else if (square.isKing()) {
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
				ChessSquare square = chessBoard.getGameBoard()[i][j];

				String position = chessBoard.getPositionByRowCol(i, j);
				int numberOfLegalMoves = 0;
				if (square.isPiece()) {
					numberOfLegalMoves = square.getNextPositions(position, chessBoard, false).size();
				}

				if (square.isWhite()) {
					score += PIECE_VALUE_MULTIPLIER * getPieceValue(square);
					score += MOBILITY_MULTIPLIER * numberOfLegalMoves;
				} else if (square.isBlack()) {
					score -= PIECE_VALUE_MULTIPLIER * getPieceValue(square);
					score -= MOBILITY_MULTIPLIER * numberOfLegalMoves;
				}

				if (square.isPawn()) {
					Pawn pawn = (Pawn) square;
					if (pawn.isDoubledPawn(position, chessBoard) || pawn.isBlockedPawn(position, chessBoard)
							|| pawn.isIsolatedPawn(position, chessBoard)) {
						if (pawn.isWhite()) {
							score -= DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER;
						} else if (pawn.isBlack()) {
							score += DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER;
						}
					}
				}

			}
		}

		return score;
	}

}
