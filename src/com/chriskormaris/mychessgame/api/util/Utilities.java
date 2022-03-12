package com.chriskormaris.mychessgame.api.util;

import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.King;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;


public class Utilities {

	public static String getPositionByRowCol(int row, int column, int numOfRows) {
		String columnString = (char) (column + 65) + "";
		String rowString = (numOfRows - row) + "";

		return columnString + rowString;
	}


	// ALTERNATIVE
	/*
	public static String getPositionByRowCol(int row, int column) {
		return Constants.chessPositions[row][column];
	}
	*/


	public static int getRowFromPosition(String position, int numOfRows) {
		// examples:
		// A8, column = 0, row = 0
		// A2, column = 0, row = 6
		// A1, column = 0, row = 7
		return numOfRows - Integer.parseInt(position.substring(1));
	}


	public static int getColumnFromPosition(String position) {
		// examples:
		// A1, column = 0, row = 7
		// B1, column = 1, row = 7
		return (int) position.charAt(0) - 65;
	}


	public static ChessPiece getChessPieceFromPosition(ChessPiece[][] gameBoard, String position) {
		int row = getRowFromPosition(position, gameBoard.length);
		int column = getColumnFromPosition(position);
		return gameBoard[row][column];
	}


	public static ChessPiece[][] copyGameBoard(ChessPiece[][] gameBoard) {
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


	public static boolean checkEqualGameBoards(ChessPiece[][] gameBoard, ChessPiece[][] otherGameBoard) {
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


	public static int[][] copyIntBoard(int[][] intBoard) {
		int n1 = intBoard.length;
		int n2 = intBoard[0].length;

		int[][] newIntBoard = new int[n1][n2];
		for (int i = 0; i < n1; i++) {
			for (int j = 0; j < n2; j++) {
				newIntBoard[i][j] = intBoard[i][j];
			}
		}
		return newIntBoard;
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

}
