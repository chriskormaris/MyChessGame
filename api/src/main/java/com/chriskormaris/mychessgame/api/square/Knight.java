package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;

import java.util.HashSet;
import java.util.Set;


public class Knight extends ChessPiece {

	public Knight(Allegiance allegiance) {
		super(allegiance);
	}

	public Knight(Allegiance allegiance, boolean isPromoted) {
		super(allegiance, isPromoted);
	}

	@Override
	public Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextKnightPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(startingPosition);
		int column = chessBoard.getColumnFromPosition(startingPosition);
		ChessSquare knight = chessBoard.getGameBoard()[row][column];

		if (!knight.isKnight()) {
			return nextKnightPositions;
		}

		// The maximum number of next moves that a knight can have is 8
		// && that situation occurs if the knight is in the center of the chess board.
		// In the sketches below, 'N' designates the position of the knight.

		/*
		2 steps forward and 1 step left
		__
		 |
		 |
		 N
		*/
		if (row >= 2 && column >= 1) {
			int newRow = row - 2;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if (endSquare.isEmpty() || knight.getAllegiance() != endSquare.getAllegiance()
					&& !endSquare.isKing() || returnThreats) {
				nextKnightPositions.add(newPosition);
			}
		}

		/*
		2 steps forward and 1 step right
		 __
		 |
		 |
		 N
		*/
		if (row >= 2 && column < chessBoard.getNumOfColumns() - 1) {
			int newRow = row - 2;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if (endSquare.isEmpty() || knight.getAllegiance() != endSquare.getAllegiance()
					&& !endSquare.isKing() || returnThreats) {
				nextKnightPositions.add(newPosition);
			}
		}

		/*
		2 steps backwards and 1 step left
		 N
		 |
		 |
		——
		*/
		if (row < chessBoard.getNumOfRows() - 2 && column >= 1) {
			int newRow = row + 2;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if (endSquare.isEmpty() || knight.getAllegiance() != endSquare.getAllegiance()
					&& !endSquare.isKing() || returnThreats) {
				nextKnightPositions.add(newPosition);
			}
		}

		/*
		2 steps backwards and 1 step right
		 N
		 |
		 |
		 ——
		*/
		if (row < chessBoard.getNumOfRows() - 2 && column < chessBoard.getNumOfColumns() - 1) {
			int newRow = row + 2;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if (endSquare.isEmpty() || knight.getAllegiance() != endSquare.getAllegiance()
					&& !endSquare.isKing() || returnThreats) {
				nextKnightPositions.add(newPosition);
			}
		}

		/*
		2 steps left and 1 step forward
		|__ __ N
		*/
		if (row >= 1 && column >= 2) {
			int newRow = row - 1;
			int newColumn = column - 2;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if (endSquare.isEmpty() || knight.getAllegiance() != endSquare.getAllegiance()
					&& !endSquare.isKing() || returnThreats) {
				nextKnightPositions.add(newPosition);
			}
		}

		/*
		2 steps right and 1 step forward
		N __ __|
		*/
		if (row >= 1 && column < chessBoard.getNumOfColumns() - 2) {
			int newRow = row - 1;
			int newColumn = column + 2;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if (endSquare.isEmpty() || knight.getAllegiance() != endSquare.getAllegiance()
					&& !endSquare.isKing() || returnThreats) {
				nextKnightPositions.add(newPosition);
			}
		}

		/*
		2 steps left and 1 step backwards
		 __ __ N
		|
		*/
		if (row < chessBoard.getNumOfRows() - 1 && column >= 2) {
			int newRow = row + 1;
			int newColumn = column - 2;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if (endSquare.isEmpty() || knight.getAllegiance() != endSquare.getAllegiance()
					&& !endSquare.isKing() || returnThreats) {
				nextKnightPositions.add(newPosition);
			}
		}

		/*
		2 steps right and 1 step backwards
		N __ __
		      |
		*/
		if (row < chessBoard.getNumOfRows() - 1 && column < chessBoard.getNumOfColumns() - 2) {
			int newRow = row + 1;
			int newColumn = column + 2;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if (endSquare.isEmpty() || knight.getAllegiance() != endSquare.getAllegiance()
					&& !endSquare.isKing() || returnThreats) {
				nextKnightPositions.add(newPosition);
			}
		}

		return nextKnightPositions;
	}


}
