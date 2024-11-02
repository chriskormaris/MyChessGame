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

		int newRow, newColumn;
		String newPosition;

		chessBoard.setThreats();

		if (chessSquare.isWhite()) {
			// White castling long
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isLeftWhiteRookMoved()
					&& !chessBoard.isWhiteKingInCheck() && position.equals("E1") && column > 1
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][0].isRook()
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][0].isWhite()
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][1].isEmpty()
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2].isEmpty()
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3].isEmpty()
					&& !chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][2]
					&& !chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][3]
					&& !chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][4]) {

				newRow = row;
				newColumn = column - 2;
				newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
			// White castling short
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isWhiteKingInCheck()
					&& !chessBoard.isRightWhiteRookMoved() && position.equals("E1") && column < 6
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][7].isRook()
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][7].isWhite()
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5].isEmpty()
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6].isEmpty()
					&& !chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][4]
					&& !chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][5]
					&& !chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][6]) {

				newRow = row;
				newColumn = column + 2;
				newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
		} else if (chessSquare.isBlack()) {
			// Black castling long
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isLeftBlackRookMoved() && !chessBoard.isBlackKingInCheck()
					&& position.equals('E' + (chessBoard.getNumOfRows() + "")) && column > 1
					&& chessBoard.getGameBoard()[0][0].isRook()
					&& chessBoard.getGameBoard()[0][0].isBlack()
					&& chessBoard.getGameBoard()[0][1].isEmpty()
					&& chessBoard.getGameBoard()[0][2].isEmpty()
					&& chessBoard.getGameBoard()[0][3].isEmpty()
					&& !chessBoard.getSquaresThreatenedByWhite()[0][2]
					&& !chessBoard.getSquaresThreatenedByWhite()[0][3]
					&& !chessBoard.getSquaresThreatenedByWhite()[0][4]) {

				newRow = row;
				newColumn = column - 2;
				newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
			// Black castling short
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isRightBlackRookMoved()
					&& !chessBoard.isBlackKingInCheck() && position.equals('E' + (chessBoard.getNumOfRows() + ""))
					&& column < 6 && chessBoard.getGameBoard()[0][7].isRook()
					&& chessBoard.getGameBoard()[0][7].isBlack()
					&& chessBoard.getGameBoard()[0][5].isEmpty()
					&& chessBoard.getGameBoard()[0][6].isEmpty()
					&& !chessBoard.getSquaresThreatenedByWhite()[0][4]
					&& !chessBoard.getSquaresThreatenedByWhite()[0][5]
					&& !chessBoard.getSquaresThreatenedByWhite()[0][6]) {

				newRow = row;
				newColumn = column + 2;
				newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
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
