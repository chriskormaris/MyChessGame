package com.chriskormaris.mychessgame.api.util;

import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.EmptySquare;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utilities {


	public static ChessPiece getChessPiece(char pieceChar) {
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


	public static char getPieceChar(ChessPiece chessPiece) {
		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			if (chessPiece instanceof Pawn) {
				return Constants.WHITE_PAWN;
			} else if (chessPiece instanceof Rook) {
				return Constants.WHITE_ROOK;
			} else if (chessPiece instanceof Knight) {
				return Constants.WHITE_KNIGHT;
			} else if (chessPiece instanceof Bishop) {
				return Constants.WHITE_BISHOP;
			} else if (chessPiece instanceof Queen) {
				return Constants.WHITE_QUEEN;
			} else if (chessPiece instanceof King) {
				return Constants.WHITE_KING;
			}
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			if (chessPiece instanceof Pawn) {
				return Constants.BLACK_PAWN;
			} else if (chessPiece instanceof Rook) {
				return Constants.BLACK_ROOK;
			} else if (chessPiece instanceof Knight) {
				return Constants.BLACK_KNIGHT;
			} else if (chessPiece instanceof Bishop) {
				return Constants.BLACK_BISHOP;
			} else if (chessPiece instanceof Queen) {
				return Constants.BLACK_QUEEN;
			} else if (chessPiece instanceof King) {
				return Constants.BLACK_KING;
			}
		}
		if (!(chessPiece instanceof EmptySquare)) {
			System.err.println("Invalid chessPiece value \"" + chessPiece + "\"!");
		}
		return '-';
	}


	public static ChessPiece[][] copyGameBoard(ChessPiece[][] gameBoard) {
		int n1 = gameBoard.length;
		int n2 = gameBoard[0].length;

		ChessPiece[][] newGameBoard = new ChessPiece[n1][n2];
		for (int i = 0; i < n1; i++) {
			System.arraycopy(gameBoard[i], 0, newGameBoard[i], 0, n2);
		}
		return newGameBoard;
	}


	// At the start of the game, the sum of all pieces' "gamePhase" values should be equal to 24.
	// In case of early promotion, the sum could be more than 24.
	public static int getPieceGamePhaseValue(ChessPiece chessPiece) {
		if (chessPiece instanceof Pawn) {
			return Constants.PAWN_GAME_PHASE_VALUE;
		} else if (chessPiece instanceof Knight) {
			return Constants.KNIGHT_GAME_PHASE_VALUE;
		} else if (chessPiece instanceof Bishop) {
			return Constants.BISHOP_GAME_PHASE_VALUE;
		} else if (chessPiece instanceof Rook) {
			return Constants.ROOK_GAME_PHASE_VALUE;
		} else if (chessPiece instanceof Queen) {
			return Constants.QUEEN_GAME_PHASE_VALUE;
		} else if (chessPiece instanceof King) {
			return Constants.KING_GAME_PHASE_VALUE;
		}
		return 0;
	}


	public static int getScoreValue(ChessPiece chessPiece) {
		int score = 0;
		if (chessPiece instanceof Pawn) {
			score = Constants.PAWN_SCORE_VALUE;
		}
		if (chessPiece instanceof Knight) {
			score = Constants.KNIGHT_SCORE_VALUE;
		}
		if (chessPiece instanceof Bishop) {
			score = Constants.BISHOP_SCORE_VALUE;
		}
		if (chessPiece instanceof Rook) {
			score = Constants.ROOK_SCORE_VALUE;
		}
		if (chessPiece instanceof Queen) {
			score = Constants.QUEEN_SCORE_VALUE;
		}
		if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			score = -score;
		}
		return score;
	}


}
