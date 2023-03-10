package com.chriskormaris.mychessgame.api.piece;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.util.Constants;

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
					&& chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][2] == 0
					&& chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][3] == 0
					&& chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][4] == 0) {

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
					&& chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][4] == 0
					&& chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][5] == 0
					&& chessBoard.getSquaresThreatenedByBlack()[chessBoard.getNumOfRows() - 1][6] == 0) {

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
					&& chessBoard.getSquaresThreatenedByWhite()[0][2] == 0
					&& chessBoard.getSquaresThreatenedByWhite()[0][3] == 0
					&& chessBoard.getSquaresThreatenedByWhite()[0][4] == 0) {

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
					&& chessBoard.getSquaresThreatenedByWhite()[0][4] == 0
					&& chessBoard.getSquaresThreatenedByWhite()[0][5] == 0
					&& chessBoard.getSquaresThreatenedByWhite()[0][6] == 0) {

				newRow = row;
				newColumn = column + 2;
				newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
		}

		return castlingPositions;
	}

	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextKingPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof King)) {
			return nextKingPositions;
		}

		// Up position
		if (row - 1 >= 0) {
			int newRow = row - 1;
			int newColumn = column;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (chessPiece.getAllegiance() == Allegiance.WHITE
					&& chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn] == 0)
					|| (chessPiece.getAllegiance() == Allegiance.BLACK
					&& chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Down position
		if (row + 1 < chessBoard.getNumOfRows()) {
			int newRow = row + 1;
			int newColumn = column;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (chessPiece.getAllegiance() == Allegiance.WHITE
					&& chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn] == 0)
					|| (chessPiece.getAllegiance() == Allegiance.BLACK
					&& chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Right position
		if (column + 1 < Constants.NUM_OF_COLUMNS) {
			int newRow = row;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (chessPiece.getAllegiance() == Allegiance.WHITE
					&& chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn] == 0)
					|| (chessPiece.getAllegiance() == Allegiance.BLACK
					&& chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Left position
		if (column - 1 >= 0) {
			int newRow = row;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (chessPiece.getAllegiance() == Allegiance.WHITE
					&& chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn] == 0)
					|| (chessPiece.getAllegiance() == Allegiance.BLACK
					&& chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper right diagonal position
		if (row - 1 >= 0 && column + 1 < 8) {
			int newRow = row - 1;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (chessPiece.getAllegiance() == Allegiance.WHITE
					&& chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn] == 0)
					|| (chessPiece.getAllegiance() == Allegiance.BLACK
					&& chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower left diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column - 1 >= 0) {
			int newRow = row + 1;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (chessPiece.getAllegiance() == Allegiance.WHITE
					&& chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn] == 0)
					|| (chessPiece.getAllegiance() == Allegiance.BLACK
					&& chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper left diagonal position
		if (row - 1 >= 0 && column - 1 >= 0) {
			int newRow = row - 1;
			int newColumn = column - 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (chessPiece.getAllegiance() == Allegiance.WHITE
					&& chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn] == 0)
					|| (chessPiece.getAllegiance() == Allegiance.BLACK
					&& chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower right diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column + 1 < Constants.NUM_OF_COLUMNS) {
			int newRow = row + 1;
			int newColumn = column + 1;
			String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
			ChessPiece endSquare = chessBoard.getGameBoard()[newRow][newColumn];
			if ((endSquare instanceof EmptySquare || chessPiece.getAllegiance() != endSquare.getAllegiance()
					&& !(endSquare instanceof King))
					&& (chessPiece.getAllegiance() == Allegiance.WHITE
					&& chessBoard.getSquaresThreatenedByBlack()[newRow][newColumn] == 0)
					|| (chessPiece.getAllegiance() == Allegiance.BLACK
					&& chessBoard.getSquaresThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		if (!returnThreats) {
			Set<String> castlingPositions = getCastlingPositions(position, chessBoard);
			nextKingPositions.addAll(castlingPositions);
		}

		return nextKingPositions;
	}

}
