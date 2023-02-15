package com.chriskormaris.mychessgame.api.evaluation;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;

import static com.chriskormaris.mychessgame.api.util.Constants.NUM_OF_COLUMNS;

// Shannon's Evaluation Function.
// see: https://www.chessprogramming.org/Evaluation
public class ShannonEvaluation extends Evaluation {

	private static final int PAWN_VALUE = 1;
	private static final int KNIGHT_VALUE = 3;
	private static final int BISHOP_VALUE = 3;
	private static final int ROOK_VALUE = 5;
	private static final int QUEEN_VALUE = 9;
	private static final int KING_VALUE = 200;

	private static final double DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER = 0.5;
	private static final double MOBILITY_MULTIPLIER = 0.1;

	private int getPieceValue(ChessPiece chessPiece) {
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

	// Shannon's Evaluation Function.
	@Override
	public double evaluate(ChessBoard chessBoard) {
		int score = 0;

		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < NUM_OF_COLUMNS; j++) {
				ChessPiece chessPiece = chessBoard.getGameBoard()[i][j];

				String position = chessBoard.getPositionByRowCol(i, j);
				// System.out.print("chessPiece: " + chessPiece + ", i: " + i + ", j: " + j);
				// System.out.println(", position: " + position);
				int numberOfLegalMoves = chessPiece.getNextPositions(position, chessBoard, false).size();

				if (chessPiece.getAllegiance() == Allegiance.WHITE) {
					score += 2 * getPieceValue(chessPiece);
					score += MOBILITY_MULTIPLIER * numberOfLegalMoves;
				} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
					score -= 2 * getPieceValue(chessPiece);
					score -= MOBILITY_MULTIPLIER * numberOfLegalMoves;
				}

				if (chessPiece instanceof Pawn) {
					Pawn pawn = (Pawn) chessPiece;
					if (pawn.isDoubledPawn(position, chessBoard) || pawn.isBlockedPawn(position, chessBoard)
							|| pawn.isIsolatedPawn(position, chessBoard)) {
						if (pawn.getAllegiance() == Allegiance.WHITE) {
							score -= DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER;
						} else if (pawn.getAllegiance() == Allegiance.BLACK) {
							score += DOUBLED_BLOCKED_ISOLATED_PAWNS_MULTIPLIER;
						}
					}
				}

			}
		}

		return score;
	}

}
