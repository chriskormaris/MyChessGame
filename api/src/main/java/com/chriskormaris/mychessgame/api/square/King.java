package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class King {

	public static Set<String> getCastlingPositions(String position, ChessBoard chessBoard) {
		Set<String> castlingPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		byte king = chessBoard.getGameBoard()[row][column];

		if (Math.abs(king) != Constants.KING) {
			return castlingPositions;
		}

		String newPosition;

		chessBoard.setThreats();

		if (king > 0) {
			// White castling queen side
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isLeftWhiteRookMoved() && !chessBoard.isWhiteKingInCheck()
					&& position.equals(chessBoard.getWhiteKingPosition()) && column > 0
					&& Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][chessBoard.getLeftWhiteRookColumn()]) == Constants.ROOK
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][chessBoard.getLeftWhiteRookColumn()] > 0) {

				boolean condition = true;

				// The squares between the King and the castling Rook must be empty and not threatened by Black.
				for (int j = chessBoard.getLeftWhiteRookColumn() + 1;
				     j < chessBoard.getColumnFromPosition(chessBoard.getWhiteKingPosition()); j++) {
					if (chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][j] != Constants.EMPTY_SQUARE
							|| chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][j]) {
						condition = false;
						break;
					}
				}

				// The squares on the 3rd and 4th columns must be empty, or contain the King or the castling Rook.
				if (condition && (chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2] != Constants.EMPTY_SQUARE
						&& Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2]) != Constants.KING 
						&& Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2]) != Constants.ROOK
						|| Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3]) != Constants.EMPTY_SQUARE
						&& Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3]) != Constants.KING
						&& Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3]) != Constants.ROOK
						|| Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2]) == Constants.ROOK
						&& chessBoard.getLeftWhiteRookColumn() != 2
						|| Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3]) == Constants.ROOK
						&& chessBoard.getLeftWhiteRookColumn() != 3)) {
					condition = false;
				}

				if (condition) {
					newPosition = chessBoard.getPositionByRowCol(row, chessBoard.getLeftWhiteRookColumn());
					castlingPositions.add(newPosition);
				}
			}
			// White castling king side
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isWhiteKingInCheck()
					&& !chessBoard.isRightWhiteRookMoved()
					&& position.equals(chessBoard.getWhiteKingPosition()) && column < 7
					&& Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][chessBoard.getRightWhiteRookColumn()]) == Constants.ROOK
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][chessBoard.getRightWhiteRookColumn()] > 0) {

				boolean condition = true;

				// The squares between the King and the castling Rook must be empty and not threatened by Black.
				for (int j = chessBoard.getColumnFromPosition(chessBoard.getWhiteKingPosition()) + 1;
				     j < chessBoard.getRightWhiteRookColumn(); j++) {
					if (chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][j] != Constants.EMPTY_SQUARE
							|| chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][j]) {
						condition = false;
						break;
					}
				}

				// The squares on the 5th and 6th columns must be empty, or contain the King or the castling Rook.
				if (condition && (chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6] != Constants.EMPTY_SQUARE
						&& Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6]) != Constants.KING
						&& Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6]) != Constants.ROOK
						|| Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5]) != Constants.EMPTY_SQUARE
						&& Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5]) != Constants.KING
						&& Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5]) != Constants.ROOK
						|| Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6]) == Constants.ROOK
						&& chessBoard.getRightWhiteRookColumn() != 6
						|| Math.abs(chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5]) == Constants.ROOK
						&& chessBoard.getRightWhiteRookColumn() != 5)) {
					condition = false;
				}

				if (condition) {
					newPosition = chessBoard.getPositionByRowCol(row, chessBoard.getRightWhiteRookColumn());
					castlingPositions.add(newPosition);
				}
			}
		} else if (king < 0) {
			// Black castling queen side
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isLeftBlackRookMoved() && !chessBoard.isBlackKingInCheck()
					&& position.equals(chessBoard.getBlackKingPosition()) && column > 0
					&& Math.abs(chessBoard.getGameBoard()[0][chessBoard.getLeftBlackRookColumn()]) == Constants.ROOK
					&& chessBoard.getGameBoard()[0][chessBoard.getLeftBlackRookColumn()] < 0) {

				boolean condition = true;

				// The squares between the King and the castling Rook must be empty and not threatened by White.
				for (int j = chessBoard.getLeftBlackRookColumn() + 1;
				     j < chessBoard.getColumnFromPosition(chessBoard.getBlackKingPosition()); j++) {
					if (chessBoard.getGameBoard()[0][j] != Constants.EMPTY_SQUARE || chessBoard.getSquaresThreatenedByWhite()[0][j]) {
						condition = false;
						break;
					}
				}

				// The squares on the 3rd and 4th columns must be empty, or contain the King or the castling Rook.
				if (condition && (chessBoard.getGameBoard()[0][2] != Constants.EMPTY_SQUARE
						&& Math.abs(chessBoard.getGameBoard()[0][2]) != Constants.KING
						&& Math.abs(chessBoard.getGameBoard()[0][2]) != Constants.ROOK
						|| Math.abs(chessBoard.getGameBoard()[0][3]) != Constants.EMPTY_SQUARE
						&& Math.abs(chessBoard.getGameBoard()[0][3]) != Constants.KING
						&& Math.abs(chessBoard.getGameBoard()[0][3]) != Constants.ROOK
						|| Math.abs(chessBoard.getGameBoard()[0][2]) == Constants.ROOK
						&& chessBoard.getLeftBlackRookColumn() != 2
						|| Math.abs(chessBoard.getGameBoard()[0][3]) == Constants.ROOK
						&& chessBoard.getLeftBlackRookColumn() != 3)) {
					condition = false;
				}

				if (condition) {
					newPosition = chessBoard.getPositionByRowCol(row, chessBoard.getLeftBlackRookColumn());
					castlingPositions.add(newPosition);
				}
			}
			// Black castling king side
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isRightBlackRookMoved()
					&& !chessBoard.isBlackKingInCheck()
					&& position.equals(chessBoard.getBlackKingPosition()) && column < 7
					&& Math.abs(chessBoard.getGameBoard()[0][chessBoard.getRightBlackRookColumn()]) == Constants.ROOK
					&& chessBoard.getGameBoard()[0][chessBoard.getRightBlackRookColumn()] < 0) {

				boolean condition = true;

				// The squares between the King and the castling Rook must be empty and not threatened by White.
				for (int j = chessBoard.getColumnFromPosition(chessBoard.getBlackKingPosition()) + 1;
				     j < chessBoard.getRightBlackRookColumn(); j++) {
					if (chessBoard.getGameBoard()[0][j] != Constants.EMPTY_SQUARE || chessBoard.getSquaresThreatenedByWhite()[0][j]) {
						condition = false;
						break;
					}
				}

				// The squares on the 5th and 6th columns must be empty, or contain the King or the castling Rook.
				if (condition && (chessBoard.getGameBoard()[0][6] != Constants.EMPTY_SQUARE
						&& Math.abs(chessBoard.getGameBoard()[0][6]) != Constants.KING
						&& Math.abs(chessBoard.getGameBoard()[0][6]) != Constants.ROOK
						|| Math.abs(chessBoard.getGameBoard()[0][5]) != Constants.EMPTY_SQUARE
						&& Math.abs(chessBoard.getGameBoard()[0][5]) != Constants.KING
						&& Math.abs(chessBoard.getGameBoard()[0][5]) != Constants.ROOK
						|| Math.abs(chessBoard.getGameBoard()[0][6]) == Constants.ROOK
						&& chessBoard.getRightBlackRookColumn() != 6
						|| Math.abs(chessBoard.getGameBoard()[0][5]) == Constants.ROOK
						&& chessBoard.getRightBlackRookColumn() != 5)) {
					condition = false;
				}

				if (condition) {
					newPosition = chessBoard.getPositionByRowCol(row, chessBoard.getRightBlackRookColumn());
					castlingPositions.add(newPosition);
				}

			}
		}

		return castlingPositions;
	}

	public static Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextKingPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(startingPosition);
		int column = chessBoard.getColumnFromPosition(startingPosition);
		byte king = chessBoard.getGameBoard()[row][column];

		if (Math.abs(king) != Constants.KING) {
			return nextKingPositions;
		}

		// Up position
		if (row - 1 >= 0) {
			int newRow = row - 1;
			int newColumn = column;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare == Constants.EMPTY_SQUARE || king * endSquare <= 0
					&& Math.abs(endSquare) != Constants.KING)
					&& (king > 0
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king < 0
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Down position
		if (row + 1 < chessBoard.getNumOfRows()) {
			int newRow = row + 1;
			int newColumn = column;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare == Constants.EMPTY_SQUARE || king * endSquare <= 0
					&& Math.abs(endSquare) != Constants.KING)
					&& (king > 0
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king < 0
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Right position
		if (column + 1 < chessBoard.getNumOfColumns()) {
			int newRow = row;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare == Constants.EMPTY_SQUARE || king * endSquare <= 0
					&& Math.abs(endSquare) != Constants.KING)
					&& (king > 0
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king < 0
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Left position
		if (column - 1 >= 0) {
			int newRow = row;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare == Constants.EMPTY_SQUARE || king * endSquare <= 0
					&& Math.abs(endSquare) != Constants.KING)
					&& (king > 0
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king < 0
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper right diagonal position
		if (row - 1 >= 0 && column + 1 < 8) {
			int newRow = row - 1;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare == Constants.EMPTY_SQUARE || king * endSquare <= 0
					&& Math.abs(endSquare) != Constants.KING)
					&& (king > 0
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king < 0
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower left diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column - 1 >= 0) {
			int newRow = row + 1;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare == Constants.EMPTY_SQUARE || king * endSquare <= 0
					&& Math.abs(endSquare) != Constants.KING)
					&& (king > 0
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king < 0
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper left diagonal position
		if (row - 1 >= 0 && column - 1 >= 0) {
			int newRow = row - 1;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare == Constants.EMPTY_SQUARE || king * endSquare <= 0
					&& Math.abs(endSquare) != Constants.KING)
					&& (king > 0
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king < 0
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower right diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column + 1 < chessBoard.getNumOfColumns()) {
			int newRow = row + 1;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare == Constants.EMPTY_SQUARE || king * endSquare <= 0
					&& Math.abs(endSquare) != Constants.KING)
					&& (king > 0
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king < 0
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		if (!returnThreats) {
			Set<String> castlingPositions = getCastlingPositions(startingPosition, chessBoard);
			nextKingPositions.addAll(castlingPositions);
		}

		return nextKingPositions;
	}

}
