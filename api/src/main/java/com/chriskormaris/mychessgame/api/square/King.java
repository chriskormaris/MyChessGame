package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;

import java.util.HashSet;
import java.util.Set;

public class King extends ChessPiece {

	public King(Allegiance allegiance) {
		super(allegiance);
	}

	public static Set<String> getCastlingPositions(String position, ChessBoard chessBoard) {
		Set<String> castlingPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		ChessSquare chessSquare = chessBoard.getGameBoard()[row][column];

		if (!chessSquare.isKing()) {
			return castlingPositions;
		}

		String newPosition;

		chessBoard.setThreats();

		if (chessSquare.isWhite()) {
			// White castling queen side
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isLeftWhiteRookMoved() && !chessBoard.isWhiteKingInCheck()
					&& position.equals(chessBoard.getWhiteKingPosition()) && column > 0
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][chessBoard.getLeftWhiteRookColumn()].isRook()
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][chessBoard.getLeftWhiteRookColumn()].isWhite()) {

				boolean condition = true;

				// The squares between the King and the castling Rook must be empty and not threatened by Black.
				for (int j = chessBoard.getLeftWhiteRookColumn() + 1;
				     j < chessBoard.getColumnFromPosition(chessBoard.getWhiteKingPosition()); j++) {
					if (!chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][j].isEmpty()
							|| chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][j]) {
						condition = false;
						break;
					}
				}

				// The squares on the 3rd and 4th columns must be empty, or contain the King or the castling Rook.
				if (condition && (!chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2].isEmpty()
						&& !chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2].isKing()
						&& !chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2].isRook()
						|| !chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3].isEmpty()
						&& !chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3].isKing()
						&& !chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3].isRook()
						|| chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2].isRook()
						&& chessBoard.getLeftWhiteRookColumn() != 2
						|| chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3].isRook()
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
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][chessBoard.getRightWhiteRookColumn()].isRook()
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][chessBoard.getRightWhiteRookColumn()].isWhite()) {

				boolean condition = true;

				// The squares between the King and the castling Rook must be empty and not threatened by Black.
				for (int j = chessBoard.getColumnFromPosition(chessBoard.getWhiteKingPosition()) + 1;
				     j < chessBoard.getRightWhiteRookColumn(); j++) {
					if (!chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][j].isEmpty()
							|| chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][j]) {
						condition = false;
						break;
					}
				}

				// The squares on the 5th and 6th columns must be empty, or contain the King or the castling Rook.
				if (condition && (!chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6].isEmpty()
						&& !chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6].isKing()
						&& !chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6].isRook()
						|| !chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5].isEmpty()
						&& !chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5].isKing()
						&& !chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5].isRook()
						|| chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6].isRook()
						&& chessBoard.getRightWhiteRookColumn() != 6
						|| chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5].isRook()
						&& chessBoard.getRightWhiteRookColumn() != 5)) {
					condition = false;
				}

				if (condition) {
					newPosition = chessBoard.getPositionByRowCol(row, chessBoard.getRightWhiteRookColumn());
					castlingPositions.add(newPosition);
				}
			}
		} else if (chessSquare.isBlack()) {
			// Black castling queen side
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isLeftBlackRookMoved() && !chessBoard.isBlackKingInCheck()
					&& position.equals(chessBoard.getBlackKingPosition()) && column > 0
					&& chessBoard.getGameBoard()[0][chessBoard.getLeftBlackRookColumn()].isRook()
					&& chessBoard.getGameBoard()[0][chessBoard.getLeftBlackRookColumn()].isBlack()) {

				boolean condition = true;

				// The squares between the King and the castling Rook must be empty and not threatened by White.
				for (int j = chessBoard.getLeftBlackRookColumn() + 1;
				     j < chessBoard.getColumnFromPosition(chessBoard.getBlackKingPosition()); j++) {
					if (!chessBoard.getGameBoard()[0][j].isEmpty() || chessBoard.getSquaresThreatenedByWhite()[0][j]) {
						condition = false;
						break;
					}
				}

				// The squares on the 3rd and 4th columns must be empty, or contain the King or the castling Rook.
				if (condition && (!chessBoard.getGameBoard()[0][2].isEmpty()
						&& !chessBoard.getGameBoard()[0][2].isKing() && !chessBoard.getGameBoard()[0][2].isRook()
						|| !chessBoard.getGameBoard()[0][3].isEmpty()
						&& !chessBoard.getGameBoard()[0][3].isKing() && !chessBoard.getGameBoard()[0][3].isRook()
						|| chessBoard.getGameBoard()[0][2].isRook() && chessBoard.getLeftBlackRookColumn() != 2
						|| chessBoard.getGameBoard()[0][3].isRook() && chessBoard.getLeftBlackRookColumn() != 3)) {
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
					&& chessBoard.getGameBoard()[0][chessBoard.getRightBlackRookColumn()].isRook()
					&& chessBoard.getGameBoard()[0][chessBoard.getRightBlackRookColumn()].isBlack()) {

				boolean condition = true;

				// The squares between the King and the castling Rook must be empty and not threatened by White.
				for (int j = chessBoard.getColumnFromPosition(chessBoard.getBlackKingPosition()) + 1;
				     j < chessBoard.getRightBlackRookColumn(); j++) {
					if (!chessBoard.getGameBoard()[0][j].isEmpty() || chessBoard.getSquaresThreatenedByWhite()[0][j]) {
						condition = false;
						break;
					}
				}

				// The squares on the 5th and 6th columns must be empty, or contain the King or the castling Rook.
				if (condition && (!chessBoard.getGameBoard()[0][6].isEmpty()
						&& !chessBoard.getGameBoard()[0][6].isKing() && !chessBoard.getGameBoard()[0][6].isRook()
						|| !chessBoard.getGameBoard()[0][5].isEmpty()
						&& !chessBoard.getGameBoard()[0][5].isKing() && !chessBoard.getGameBoard()[0][5].isRook()
						|| chessBoard.getGameBoard()[0][6].isRook() && chessBoard.getRightBlackRookColumn() != 6
						|| chessBoard.getGameBoard()[0][5].isRook() && chessBoard.getRightBlackRookColumn() != 5)) {
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

	@Override
	public Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextKingPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(startingPosition);
		int column = chessBoard.getColumnFromPosition(startingPosition);
		ChessSquare king = chessBoard.getGameBoard()[row][column];

		if (!(king.isKing())) {
			return nextKingPositions;
		}

		// Up position
		if (row - 1 >= 0) {
			int newRow = row - 1;
			int newColumn = column;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare.isEmpty() || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare.isKing()))
					&& (king.isWhite()
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.isBlack()
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Down position
		if (row + 1 < chessBoard.getNumOfRows()) {
			int newRow = row + 1;
			int newColumn = column;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare.isEmpty() || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare.isKing()))
					&& (king.isWhite()
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.isBlack()
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Right position
		if (column + 1 < chessBoard.getNumOfColumns()) {
			int newRow = row;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare.isEmpty() || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare.isKing()))
					&& (king.isWhite()
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.isBlack()
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Left position
		if (column - 1 >= 0) {
			int newRow = row;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare.isEmpty() || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare.isKing()))
					&& (king.isWhite()
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.isBlack()
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper right diagonal position
		if (row - 1 >= 0 && column + 1 < 8) {
			int newRow = row - 1;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare.isEmpty() || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare.isKing()))
					&& (king.isWhite()
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.isBlack()
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower left diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column - 1 >= 0) {
			int newRow = row + 1;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare.isEmpty() || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare.isKing()))
					&& (king.isWhite()
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.isBlack()
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper left diagonal position
		if (row - 1 >= 0 && column - 1 >= 0) {
			int newRow = row - 1;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare.isEmpty() || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare.isKing()))
					&& (king.isWhite()
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.isBlack()
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower right diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column + 1 < chessBoard.getNumOfColumns()) {
			int newRow = row + 1;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare.isEmpty() || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare.isKing()))
					&& (king.isWhite()
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.isBlack()
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
