package com.chriskormaris.mychessgame.api.util;

import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.square.Bishop;
import com.chriskormaris.mychessgame.api.square.ChessSquare;
import com.chriskormaris.mychessgame.api.square.EmptySquare;
import com.chriskormaris.mychessgame.api.square.King;
import com.chriskormaris.mychessgame.api.square.Knight;
import com.chriskormaris.mychessgame.api.square.Pawn;
import com.chriskormaris.mychessgame.api.square.Queen;
import com.chriskormaris.mychessgame.api.square.Rook;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utilities {

	public static ChessSquare getChessPiece(char pieceChar) {
		if (pieceChar == Constants.WHITE_PAWN) {
			return new Pawn(Allegiance.WHITE);
		} else if (pieceChar == Constants.WHITE_ROOK) {
			return new Rook(Allegiance.WHITE);
		} else if (pieceChar == Constants.WHITE_KNIGHT) {
			return new Knight(Allegiance.WHITE);
		} else if (pieceChar == Constants.WHITE_BISHOP) {
			return new Bishop(Allegiance.WHITE);
		} else if (pieceChar == Constants.WHITE_QUEEN) {
			return new Queen(Allegiance.WHITE);
		} else if (pieceChar == Constants.WHITE_KING) {
			return new King(Allegiance.WHITE);
		}

		if (pieceChar == Constants.BLACK_PAWN) {
			return new Pawn(Allegiance.BLACK);
		} else if (pieceChar == Constants.BLACK_ROOK) {
			return new Rook(Allegiance.BLACK);
		} else if (pieceChar == Constants.BLACK_KNIGHT) {
			return new Knight(Allegiance.BLACK);
		} else if (pieceChar == Constants.BLACK_BISHOP) {
			return new Bishop(Allegiance.BLACK);
		} else if (pieceChar == Constants.BLACK_QUEEN) {
			return new Queen(Allegiance.BLACK);
		} else if (pieceChar == Constants.BLACK_KING) {
			return new King(Allegiance.BLACK);
		}

		if (pieceChar != '-') {
			System.err.println("Invalid chessPiece character \"" + pieceChar + "\"!");
		}
		return new EmptySquare();
	}

	public static char getPieceChar(ChessSquare chessSquare) {
		if (chessSquare.isWhite()) {
			if (chessSquare.isPawn()) {
				return Constants.WHITE_PAWN;
			} else if (chessSquare.isRook()) {
				return Constants.WHITE_ROOK;
			} else if (chessSquare.isKnight()) {
				return Constants.WHITE_KNIGHT;
			} else if (chessSquare.isBishop()) {
				return Constants.WHITE_BISHOP;
			} else if (chessSquare.isQueen()) {
				return Constants.WHITE_QUEEN;
			} else if (chessSquare.isKing()) {
				return Constants.WHITE_KING;
			}
		} else if (chessSquare.isBlack()) {
			if (chessSquare.isPawn()) {
				return Constants.BLACK_PAWN;
			} else if (chessSquare.isRook()) {
				return Constants.BLACK_ROOK;
			} else if (chessSquare.isKnight()) {
				return Constants.BLACK_KNIGHT;
			} else if (chessSquare.isBishop()) {
				return Constants.BLACK_BISHOP;
			} else if (chessSquare.isQueen()) {
				return Constants.BLACK_QUEEN;
			} else if (chessSquare.isKing()) {
				return Constants.BLACK_KING;
			}
		}
		if (chessSquare.isPiece()) {
			System.err.println("Invalid chessPiece value \"" + chessSquare + "\"!");
		}
		return '-';
	}

	public static ChessSquare[][] copyGameBoard(ChessSquare[][] gameBoard) {
		int n1 = gameBoard.length;
		int n2 = gameBoard[0].length;

		ChessSquare[][] newGameBoard = new ChessSquare[n1][n2];
		for (int i = 0; i < n1; i++) {
			System.arraycopy(gameBoard[i], 0, newGameBoard[i], 0, n2);
		}
		return newGameBoard;
	}

	public static int getScoreValue(ChessSquare chessSquare) {
		int score = 0;
		if (chessSquare.isPawn()) {
			score = Constants.PAWN_SCORE_VALUE;
		}
		if (chessSquare.isKnight()) {
			score = Constants.KNIGHT_SCORE_VALUE;
		}
		if (chessSquare.isBishop()) {
			score = Constants.BISHOP_SCORE_VALUE;
		}
		if (chessSquare.isRook()) {
			score = Constants.ROOK_SCORE_VALUE;
		}
		if (chessSquare.isQueen()) {
			score = Constants.QUEEN_SCORE_VALUE;
		}
		if (chessSquare.isBlack()) {
			score = -score;
		}
		return score;
	}

}
