package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.HashSet;
import java.util.Set;


public class Rook {

	public static Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextRookPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(startingPosition);
		int column = chessBoard.getColumnFromPosition(startingPosition);
		byte rook = chessBoard.getGameBoard()[row][column];

		if (Math.abs(rook) != Constants.ROOK) {
			return nextRookPositions;
		}

		// Find all the up positions.
		for (int i = row - 1; i >= 0; i--) {
			if (row > 0) {
				int newRow = i;
				int newColumn = column;

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare == Constants.EMPTY_SQUARE || rook * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (rook * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| rook * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
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

				if (endSquare == Constants.EMPTY_SQUARE || rook * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (rook * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| rook * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
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

				if (endSquare == Constants.EMPTY_SQUARE || rook * endSquare < 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (rook * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| rook * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
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

				if (endSquare == Constants.EMPTY_SQUARE || rook * endSquare <= 0
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (rook * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| rook * chessBoard.getGameBoard()[newRow][newColumn] > 0) {
					break;
				}
			}
		}

		return nextRookPositions;
	}

}
