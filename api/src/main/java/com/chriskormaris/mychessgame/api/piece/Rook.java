package com.chriskormaris.mychessgame.api.piece;

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
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextRookPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof Rook)) {
			return nextRookPositions;
		}

		// Find all the up positions.
		for (int i = row - 1; i >= 0; i--) {
			if (row > 0) {
				int newRow = i;
				int newColumn = column;

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
						&& !(endSquare instanceof King) || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
						&& !(endSquare instanceof King) || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
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
				ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
						&& !(endSquare instanceof King) || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)
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

				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
						&& !(endSquare instanceof King) || returnThreats) {
					nextRookPositions.add(newPosition);
				}

				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endSquare.getAllegiance() && !(endSquare instanceof EmptySquare)
						|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
		}

		return nextRookPositions;
	}

}
