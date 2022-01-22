package com.chriskormaris.mychessgame.api.utility;

import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.Knight;
import com.chriskormaris.mychessgame.api.piece.Pawn;
import com.chriskormaris.mychessgame.api.piece.Queen;
import com.chriskormaris.mychessgame.api.piece.Rook;


public class Utilities {


	public static String getPositionByRowCol(int row, int column) {
		String columnString = (char) (column + 65) + "";

		// We add '0' which is equal to 48 in decimal,
		// so that we get the correct number.
		String rowString = (row + 1) + "";

		String position = columnString + rowString;

		return position;
	}

	// ALTERNATIVE
	/*
	public static String getPositionByRowCol(int row, int column) {
		return Constants.chessPositions[row][column];
	}
	*/

	public static int getRowFromPosition(String position) {
		// example: A2, column = 0, row = 1
		int row = Integer.parseInt(position.substring(1)) - 1;
		return row;
	}


	public static int getColumnFromPosition(String position) {
		// example: B1, column = 1, row = 0
		int column = (int) position.charAt(0) - 65;
		return column;
	}


	public static ChessPiece getChessPieceFromPosition(ChessPiece[][] gameBoard, String position) {
		int row = getRowFromPosition(position);
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


	public static double getChessPieceValue(String position, ChessPiece chessPiece, int halfMoveNumber) {
		int row = Utilities.getRowFromPosition(position);
		double value = 0;
		if (halfMoveNumber <= Constants.MIDDLE_GAME_HALF_MOVES_THRESHOLD) {
			if (chessPiece instanceof Pawn) {
				if (chessPiece.getAllegiance() == Allegiance.WHITE && row <= 3
						|| chessPiece.getAllegiance() == Allegiance.BLACK && row >= 4) {
					value = Constants.PAWN_VALUE;
				} else {
					value = Constants.CENTER_PAWN_VALUE;
				}
			} else if (chessPiece instanceof Knight) {
				value = Constants.KNIGHT_VALUE;
			} else if (chessPiece instanceof Bishop) {
				value = Constants.BISHOP_VALUE;
			} else if (chessPiece instanceof Rook) {
				value = Constants.ROOK_VALUE;
			} else if (chessPiece instanceof Queen) {
				value = Constants.QUEEN_VALUE;
			}
		} else {
			if (chessPiece instanceof Pawn) {
				value = Constants.PAWN_LATE_VALUE;
			} else if (chessPiece instanceof Knight) {
				value = Constants.KNIGHT_LATE_VALUE;
			} else if (chessPiece instanceof Bishop) {
				value = Constants.BISHOP_LATE_VALUE;
			} else if (chessPiece instanceof Rook) {
				value = Constants.ROOK_LATE_VALUE;
			} else if (chessPiece instanceof Queen) {
				value = Constants.QUEEN_LATE_VALUE;
			}
		}
		return value;
	}


}