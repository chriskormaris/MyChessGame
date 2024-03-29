package com.chriskormaris.mychessgame.api.piece;

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
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof King)) {
			return castlingPositions;
		}

		int newRow, newColumn;
		String newPosition;

		chessBoard.setThreats();

		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			// White castling long
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isLeftWhiteRookMoved()
					&& !chessBoard.isWhiteKingInCheck() && position.equals("E1") && column > 1
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][0] instanceof Rook
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][0].getAllegiance() == Allegiance.WHITE
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][1] instanceof EmptySquare
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2] instanceof EmptySquare
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3] instanceof EmptySquare
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
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][7] instanceof Rook
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][7].getAllegiance() == Allegiance.WHITE
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5] instanceof EmptySquare
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6] instanceof EmptySquare
					&& !chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][4]
					&& !chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][5]
					&& !chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][6]) {

				newRow = row;
				newColumn = column + 2;
				newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			// Black castling long
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isLeftBlackRookMoved() && !chessBoard.isBlackKingInCheck()
					&& position.equals('E' + (chessBoard.getNumOfRows() + "")) && column > 1
					&& chessBoard.getGameBoard()[0][0] instanceof Rook
					&& chessBoard.getGameBoard()[0][0].getAllegiance() == Allegiance.BLACK
					&& chessBoard.getGameBoard()[0][1] instanceof EmptySquare
					&& chessBoard.getGameBoard()[0][2] instanceof EmptySquare
					&& chessBoard.getGameBoard()[0][3] instanceof EmptySquare
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
					&& column < 6 && chessBoard.getGameBoard()[0][7] instanceof Rook
					&& chessBoard.getGameBoard()[0][7].getAllegiance() == Allegiance.BLACK
					&& chessBoard.getGameBoard()[0][5] instanceof EmptySquare
					&& chessBoard.getGameBoard()[0][6] instanceof EmptySquare
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
		ChessPiece king = chessBoard.getGameBoard()[row][column];

		if (!(king instanceof King)) {
			return nextKingPositions;
		}

		// Up position
		if (row - 1 >= 0) {
			int newRow = row - 1;
			int newColumn = column;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (king.getAllegiance() == Allegiance.WHITE
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.getAllegiance() == Allegiance.BLACK
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Down position
		if (row + 1 < chessBoard.getNumOfRows()) {
			int newRow = row + 1;
			int newColumn = column;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (king.getAllegiance() == Allegiance.WHITE
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.getAllegiance() == Allegiance.BLACK
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Right position
		if (column + 1 < chessBoard.getNumOfColumns()) {
			int newRow = row;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (king.getAllegiance() == Allegiance.WHITE
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.getAllegiance() == Allegiance.BLACK
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Left position
		if (column - 1 >= 0) {
			int newRow = row;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (king.getAllegiance() == Allegiance.WHITE
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.getAllegiance() == Allegiance.BLACK
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper right diagonal position
		if (row - 1 >= 0 && column + 1 < 8) {
			int newRow = row - 1;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (king.getAllegiance() == Allegiance.WHITE
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.getAllegiance() == Allegiance.BLACK
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower left diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column - 1 >= 0) {
			int newRow = row + 1;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (king.getAllegiance() == Allegiance.WHITE
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.getAllegiance() == Allegiance.BLACK
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper left diagonal position
		if (row - 1 >= 0 && column - 1 >= 0) {
			int newRow = row - 1;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (king.getAllegiance() == Allegiance.WHITE
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.getAllegiance() == Allegiance.BLACK
					&& !chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn])
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower right diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column + 1 < chessBoard.getNumOfColumns()) {
			int newRow = row + 1;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || king.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (king.getAllegiance() == Allegiance.WHITE
					&& !chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn]
					|| king.getAllegiance() == Allegiance.BLACK
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
