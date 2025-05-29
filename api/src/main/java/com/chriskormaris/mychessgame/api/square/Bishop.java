package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.HashSet;
import java.util.Set;


public class Bishop {

	public static Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextBishopPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(startingPosition);
		int column = chessBoard.getColumnFromPosition(startingPosition);
		byte bishop = chessBoard.getGameBoard()[row][column];

		if (Math.abs(bishop) != Constants.BISHOP) {
			return nextBishopPositions;
		}

		int counter;

		// Find all the upper right diagonal positions.
		counter = 1;
		for (int i = row - 1; i >= 0; i--) {
			if (row >= 0 && column + counter < 8) {

				int newRow = i;
				int newColumn = column + counter;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				// System.out.println("newPosition: " + newPosition);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endSquare: " + endSquare);
				if (endSquare == Constants.EMPTY_SQUARE || bishop * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextBishopPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (bishop * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| bishop * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
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
				if (endSquare == Constants.EMPTY_SQUARE || bishop * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextBishopPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (bishop * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| bishop * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
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
				if (endSquare == Constants.EMPTY_SQUARE || bishop * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextBishopPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (bishop * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| bishop * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
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
				if (endSquare == Constants.EMPTY_SQUARE || bishop * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextBishopPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (bishop * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| bishop * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
					break;
				}
			}
			counter++;
		}

		return nextBishopPositions;
	}

}
