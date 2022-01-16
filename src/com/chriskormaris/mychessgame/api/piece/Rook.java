package com.chriskormaris.mychessgame.api.piece;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.utility.Constants;
import com.chriskormaris.mychessgame.api.utility.Utilities;

import java.util.HashSet;
import java.util.Set;


public class Rook extends ChessPiece {

	public Rook(Allegiance allegiance) {
		super(allegiance);
	}

	@Override
	public ChessPiece makeCopy() {
		return new Rook(super.getAllegiance());
	}

	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {

		// System.out.println("current position: " + position);

		Set<String> nextRookPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof Rook)) {
			return nextRookPositions;
		}

		// Find all the up positions.
		for (int i = row + 1; i < chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows()) {

				int newRow = i;
				int newColumn = column;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
						&& !(endTile instanceof King) || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
		}

		// Find all the down positions.
		for (int i = row - 1; i >= 0; i--) {
			if (row > 0) {

				int newRow = i;
				int newColumn = column;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
						&& !(endTile instanceof King) || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
		}

		// Find all the right positions.
		for (int j = column + 1; j < Constants.DEFAULT_NUM_OF_COLUMNS; j++) {
			if (column < Constants.DEFAULT_NUM_OF_COLUMNS) {

				int newRow = row;
				int newColumn = j;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
						&& !(endTile instanceof King) || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
		}

		// Find all the left positions.
		for (int j = column - 1; j >= 0; j--) {
			if (column > 0) {

				int newRow = row;
				int newColumn = j;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
						&& !(endTile instanceof King) || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
		}

		return nextRookPositions;

	}

}
