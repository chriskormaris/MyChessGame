package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;

import java.util.HashSet;
import java.util.Set;


public class Queen extends ChessPiece {

	public Queen(Allegiance allegiance) {
		super(allegiance);
	}

	public Queen(Allegiance allegiance, boolean isPromoted) {
		super(allegiance, isPromoted);
	}

	@Override
	public Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextQueenPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(startingPosition);
		int column = chessBoard.getColumnFromPosition(startingPosition);
		ChessSquare queen = chessBoard.getGameBoard()[row][column];

		if (!queen.isQueen()) {
			return nextQueenPositions;
		}

		// Find all the up positions.
		for (int i = row - 1; i >= 0; i--) {
			if (row > 0) {
				int newRow = i;
				int newColumn = column;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare.isEmpty() || queen.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| queen.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare.isEmpty() || queen.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| queen.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare.isEmpty() || queen.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| queen.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare.isEmpty() || queen.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| queen.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare.isEmpty() || queen.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| queen.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare.isEmpty() || queen.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| queen.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare.isEmpty() || queen.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| queen.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare.isEmpty() || queen.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextQueenPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (queen.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| queen.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
			counter++;
		}

		return nextQueenPositions;
	}


}
