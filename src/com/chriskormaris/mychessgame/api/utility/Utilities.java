package com.chriskormaris.mychessgame.api.utility;

import com.chriskormaris.mychessgame.api.piece.Bishop;
import com.chriskormaris.mychessgame.api.piece.ChessPiece;
import com.chriskormaris.mychessgame.api.piece.King;
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


	public static double getChessPieceValue(int row, int column, ChessPiece chessPiece, int halfMoveNumber) {
		if (halfMoveNumber <= Constants.MIDDLE_GAME_HALF_MOVES_THRESHOLD) {
			return getMiddleGameChessPieceValue(row, column, chessPiece);
		} else {
			return getEndgameChessPieceValue(row, column, chessPiece);
		}
	}

	public static double getMiddleGameChessPieceValue(int row, int column, ChessPiece chessPiece) {
		if (chessPiece instanceof Pawn) {
			return Constants.PAWN_CENTIPAWN_VALUE + Constants.PAWNS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Knight) {
			return Constants.KNIGHT_CENTIPAWN_VALUE + Constants.KNIGHTS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Bishop) {
			return Constants.BISHOP_CENTIPAWN_VALUE + Constants.BISHOPS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Rook) {
			return Constants.ROOK_CENTIPAWN_VALUE + Constants.ROOKS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Queen) {
			return Constants.QUEEN_CENTIPAWN_VALUE + Constants.QUEEN_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof King) {
			return Constants.KING_CENTIPAWN_VALUE + Constants.KING_SQUARES_TABLE_MIDDLE_GAME[row][column];
		}
		return 0.0;
	}

	public static double getEndgameChessPieceValue(int row, int column, ChessPiece chessPiece) {
		if (chessPiece instanceof Pawn) {
			return Constants.PAWN_CENTIPAWN_VALUE + Constants.PAWNS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Knight) {
			return Constants.KNIGHT_CENTIPAWN_VALUE + Constants.KNIGHTS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Bishop) {
			return Constants.BISHOP_CENTIPAWN_VALUE + Constants.BISHOPS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Rook) {
			return Constants.ROOK_CENTIPAWN_VALUE + Constants.ROOKS_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof Queen) {
			return Constants.QUEEN_CENTIPAWN_VALUE + Constants.QUEEN_SQUARES_TABLE[row][column];
		} else if (chessPiece instanceof King) {
			return Constants.KING_CENTIPAWN_VALUE + Constants.KING_SQUARES_TABLE_MIDDLE_GAME[row][column];
		}
		return 0.0;
	}

}
