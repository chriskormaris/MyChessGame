package com.chriskormaris.mychessgame.api.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utilities {

	public static byte[][] copyGameBoard(byte[][] gameBoard) {
		int n1 = gameBoard.length;
		int n2 = gameBoard[0].length;

		byte[][] newGameBoard = new byte[n1][n2];
		for (int i = 0; i < n1; i++) {
			System.arraycopy(gameBoard[i], 0, newGameBoard[i], 0, n2);
		}
		return newGameBoard;
	}

	public static int getScoreValue(byte chessSquare) {
		int score = 0;
		if (Math.abs(chessSquare) == Constants.PAWN) {
			score = Constants.PAWN_SCORE_VALUE;
		}
		if (Math.abs(chessSquare) == Constants.KNIGHT) {
			score = Constants.KNIGHT_SCORE_VALUE;
		}
		if (Math.abs(chessSquare) == Constants.BISHOP) {
			score = Constants.BISHOP_SCORE_VALUE;
		}
		if (Math.abs(chessSquare) == Constants.ROOK) {
			score = Constants.ROOK_SCORE_VALUE;
		}
		if (Math.abs(chessSquare) == Constants.QUEEN) {
			score = Constants.QUEEN_SCORE_VALUE;
		}
		if (chessSquare < 0) {
			score = -score;
		}
		return score;
	}

}
