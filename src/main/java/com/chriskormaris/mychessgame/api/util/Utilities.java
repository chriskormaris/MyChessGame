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
import lombok.experimental.UtilityClass;


@UtilityClass
public class Utilities {


	public ChessPiece getChessPiece(char pieceChar) {
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
		System.err.println("Invalid chessPiece character \"" + pieceChar + "\"!");
		return new EmptySquare();
	}


	public char getPieceChar(ChessPiece chessPiece) {
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


	public String getPositionByRowCol(int row, int column, int numOfRows) {
		String columnString = (char) (column + 65) + "";
		String rowString = (numOfRows - row) + "";

		return columnString + rowString;
	}

	// ALTERNATIVE
	/*
	public String getPositionByRowCol(int row, int column) {
		return Constants.chessPositions[row][column];
	}
	*/


	public int getRowFromPosition(String position, int numOfRows) {
		// examples:
		// A8, column = 0, row = 0
		// A2, column = 0, row = 6
		// A1, column = 0, row = 7
		return numOfRows - Integer.parseInt(position.substring(1));
	}


	public int getColumnFromPosition(String position) {
		// examples:
		// A1, column = 0, row = 7
		// B1, column = 1, row = 7
		return (int) position.charAt(0) - 65;
	}


	public ChessPiece getChessPieceFromPosition(ChessPiece[][] gameBoard, String position) {
		int row = getRowFromPosition(position, gameBoard.length);
		int column = getColumnFromPosition(position);
		return gameBoard[row][column];
	}


	public ChessPiece[][] copyGameBoard(ChessPiece[][] gameBoard) {
		int n1 = gameBoard.length;
		int n2 = gameBoard[0].length;

		ChessPiece[][] newGameBoard = new ChessPiece[n1][n2];
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				newGameBoard[i][j] = gameBoard[i][j].makeCopy();
			}
		}
		return newGameBoard;
	}


	public boolean checkEqualGameBoards(ChessPiece[][] gameBoard, ChessPiece[][] otherGameBoard) {
		int n1 = gameBoard.length;
		int n2 = gameBoard[0].length;

		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				if (!(gameBoard[i][j].getAllegiance() == otherGameBoard[i][j].getAllegiance()
						&& gameBoard[i][j].getChessPieceChar() == otherGameBoard[i][j].getChessPieceChar())) {
					return false;
				}
			}
		}
		return true;
	}


	public int[][] copyIntBoard(int[][] intBoard) {
		int n1 = intBoard.length;
		int n2 = intBoard[0].length;

		int[][] newIntBoard = new int[n1][n2];
		for (int i = 0; i < n1; i++) {
			System.arraycopy(intBoard[i], 0, newIntBoard[i], 0, n2);
		}
		return newIntBoard;
	}


	// At the start of the game, the sum of all pieces' "gamePhase" values should be equal to 24.
	// In case of early promotion, the sum could be more than 24.
	public int getPieceGamePhaseValue(ChessPiece chessPiece) {
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


}
