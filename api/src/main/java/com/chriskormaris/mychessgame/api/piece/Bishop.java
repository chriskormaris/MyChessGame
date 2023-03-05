package com.chriskormaris.mychessgame.api.piece;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.HashSet;
import java.util.Set;


public class Bishop extends ChessPiece {

	public Bishop(Allegiance allegiance) {
		super(allegiance);
	}

	public Bishop(Allegiance allegiance, boolean isPromoted) {
		super(allegiance, isPromoted);
	}

	@Override
	public ChessPiece makeCopy() {
		return new Bishop(super.getAllegiance(), super.isPromoted());
	}

	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextBishopPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof Bishop)) {
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
				ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endSquare: " + endSquare);
				if (endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
						&& !(endSquare instanceof King) || returnThreats) {
					nextBishopPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
				if (endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
						&& !(endSquare instanceof King) || returnThreats) {
					nextBishopPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
				if (endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
						&& !(endSquare instanceof King) || returnThreats) {
					nextBishopPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
			counter++;
		}

		// Find all the lower right diagonal positions.
		counter = 1;
		for (int i = row + 1; i < chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows() && column + counter < Constants.NUM_OF_COLUMNS) {

				int newRow = i;
				int newColumn = column + counter;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
				if (endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
						&& !(endSquare instanceof King) || returnThreats) {
					nextBishopPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
			counter++;
		}

		return nextBishopPositions;
	}

}
