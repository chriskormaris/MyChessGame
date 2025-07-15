package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.HashSet;
import java.util.Set;


public class Rook extends ChessPiece {

	public Rook(Allegiance allegiance) {
		super(allegiance);
	}

	public Rook(Allegiance allegiance, boolean isPromoted) {
		super(allegiance, isPromoted);
	}

	@Override
	public Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextRookPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(startingPosition);
		int column = chessBoard.getColumnFromPosition(startingPosition);
		ChessSquare rook = chessBoard.getGameBoard()[row][column];

		if (!rook.isRook()) {
			return nextRookPositions;
		}

		// Find all the up positions.
		for (int i = row - 1; i >= 0; i--) {
			if (row > 0) {
				int newRow = i;
				int newColumn = column;

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare.isEmpty() || rook.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (rook.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| rook.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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

				if (endSquare.isEmpty() || rook.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (rook.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| rook.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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

				if (endSquare.isEmpty() || rook.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (rook.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| rook.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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

				if (endSquare.isEmpty() || rook.getAllegiance() != endSquare.getAllegiance()
						&& !endSquare.isKing() || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (rook.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| rook.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
		}

		return nextRookPositions;
	}

	@Override
	public char getSymbol() {
		return isWhite() ? Constants.WHITE_ROOK_CHAR : Constants.BLACK_ROOK_CHAR;
	}

}
