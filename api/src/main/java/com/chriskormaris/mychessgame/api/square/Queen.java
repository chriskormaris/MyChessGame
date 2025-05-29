package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.HashSet;
import java.util.Set;


public class Queen {

	public static Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextQueenPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(startingPosition);
		int column = chessBoard.getColumnFromPosition(startingPosition);
		byte queen = chessBoard.getGameBoard()[row][column];

		if (Math.abs(queen) != Constants.QUEEN) {
			return nextQueenPositions;
		}

		// Find all the up positions.
		for (int i = row - 1; i >= 0; i--) {
			if (row > 0) {
				int newRow = i;
				int newColumn = column;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare == Constants.EMPTY_SQUARE || queen * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| queen * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
					break;
				}
			}
		}

		// Find all the down positions.
		for (int i = row + 1; i < chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows()) {
				int newRow = i;
				int newColumn = column;

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare == Constants.EMPTY_SQUARE || queen * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| queen * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
					break;
				}
			}
		}

		// Find all the right positions.
		for (int j = column + 1; j < chessBoard.getNumOfColumns(); j++) {
			if (column < chessBoard.getNumOfColumns()) {
				int newRow = row;
				int newColumn = j;

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare == Constants.EMPTY_SQUARE || queen * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| queen * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
					break;
				}
			}
		}

		// Find all the left positions.
		for (int j = column - 1; j >= 0; j--) {
			if (column > 0) {
				int newRow = row;
				int newColumn = j;

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare == Constants.EMPTY_SQUARE || queen * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| queen * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
					break;
				}
			}
		}

		int counter;

		// Find all the upper right diagonal positions.
		counter = 1;
		for (int i = row - 1; i >= 0; i--) {
			if (row >= 0 && column + counter < chessBoard.getNumOfColumns()) {
				int newRow = i;
				int newColumn = column + counter;

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare == Constants.EMPTY_SQUARE || queen * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| queen * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
					break;
				}
			}
			counter++;
		}

		// Find all the lower left diagonal positions.
		counter = 1;
		for (int i = row + 1; i < chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows() && column - counter >= 0) {
				int newRow = i;
				int newColumn = column - counter;

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare == Constants.EMPTY_SQUARE || queen * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| queen * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
					break;
				}
			}
			counter++;
		}

		// Find all the upper left diagonal positions.
		counter = 1;
		for (int i = row - 1; i >= 0; i--) {
			if (row >= 0 && column - counter >= 0) {
				int newRow = i;
				int newColumn = column - counter;

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare == Constants.EMPTY_SQUARE || queen * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| queen * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
					break;
				}
			}
			counter++;
		}

		// Find all the lower right diagonal positions.
		counter = 1;
		for (int i = row + 1; i < chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows() && column + counter < chessBoard.getNumOfColumns()) {
				int newRow = i;
				int newColumn = column + counter;

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare == Constants.EMPTY_SQUARE || queen * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| queen * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
					break;
				}
			}
			counter++;
		}

		return nextQueenPositions;
	}


}
