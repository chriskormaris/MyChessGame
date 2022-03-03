package com.chriskormaris.mychessgame.api.piece;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.utility.Constants;
import com.chriskormaris.mychessgame.api.utility.Utilities;

import java.util.HashSet;
import java.util.Set;


public class Pawn extends ChessPiece {

	public Pawn(Allegiance allegiance) {
		super(allegiance);
	}

	@Override
	public ChessPiece makeCopy() {
		return new Pawn(super.getAllegiance());
	}

	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {

		// System.out.println("current position: " + position);

		Set<String> nextPawnPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof Pawn)) {
			return nextPawnPositions;
		}

		int newRow = 0;

		if (chessPiece.getAllegiance() == Allegiance.WHITE && row < chessBoard.getNumOfRows() - 1
				|| chessPiece.getAllegiance() == Allegiance.BLACK && row > 0) {

			if (!returnThreats) {
				// One step forward position.
				if (chessPiece.getAllegiance() == Allegiance.WHITE)
					newRow = row + 1;
				else if (chessPiece.getAllegiance() == Allegiance.BLACK)
					newRow = row - 1;
				int newColumn = column;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTileCode: " + endTileCode);
				if (endTile instanceof EmptyTile) {
					nextPawnPositions.add(newPosition);
				}

				// Two steps forward position.
				if (chessPiece.getAllegiance() == Allegiance.WHITE && row < chessBoard.getNumOfRows() - 2
						&& chessBoard.getGameBoard()[row + 2][column] instanceof EmptyTile
						&& chessBoard.getGameBoard()[row + 1][column] instanceof EmptyTile
						|| chessPiece.getAllegiance() == Allegiance.BLACK && row > 1
						&& chessBoard.getGameBoard()[row - 2][column] instanceof EmptyTile
						&& chessBoard.getGameBoard()[row - 1][column] instanceof EmptyTile) {

					if (chessPiece.getAllegiance() == Allegiance.WHITE && row == 1)
						newRow = row + 2;
					else if (chessPiece.getAllegiance() == Allegiance.BLACK && row == chessBoard.getNumOfRows() - 2)
						newRow = row - 2;
					newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
					// endTile = chessBoard.getGameBoard()[newRow][newColumn];
					// System.out.println("endTile: " + endTile);
					nextPawnPositions.add(newPosition);
				}

			}

			// One step diagonally forward left.
			if (column > 0) {
				if (chessPiece.getAllegiance() == Allegiance.WHITE)
					newRow = row + 1;
				else if (chessPiece.getAllegiance() == Allegiance.BLACK)
					newRow = row - 1;
				int newColumn = column - 1;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if ((!(endTile instanceof EmptyTile) && chessPiece.getAllegiance() != endTile.getAllegiance())
						&& !(endTile instanceof King) || returnThreats) {
					nextPawnPositions.add(newPosition);
				}
			}

			// One step diagonally forward right.
			if (column < Constants.DEFAULT_NUM_OF_COLUMNS - 1) {
				if (chessPiece.getAllegiance() == Allegiance.WHITE)
					newRow = row + 1;
				else if (chessPiece.getAllegiance() == Allegiance.BLACK)
					newRow = row - 1;
				int newColumn = column + 1;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if ((!(endTile instanceof EmptyTile) && chessPiece.getAllegiance() != endTile.getAllegiance())
						&& !(endTile instanceof King) || returnThreats) {
					nextPawnPositions.add(newPosition);
				}
			}

			Set<String> enPassantPositions = getEnPassantPositions(position, chessBoard, returnThreats);
			// System.out.println("enPassantPositions: " + enPassantPositions);
			nextPawnPositions.addAll(enPassantPositions);

		}

		return nextPawnPositions;
	}

	// Implementation of the "en passant" moves.
	public Set<String> getEnPassantPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		// System.out.println("current position: " + position);

		Set<String> enPassantPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof Pawn)) {
			return enPassantPositions;
		}

		int newRow = 0;

		if (chessPiece.getAllegiance() == Allegiance.WHITE && row < chessBoard.getNumOfRows() - 1
				|| chessPiece.getAllegiance() == Allegiance.BLACK && row > 0) {

			// One step diagonally forward left.
			if (column > 0) {
				if (chessPiece.getAllegiance() == Allegiance.WHITE)
					newRow = row + 1;
				if (chessPiece.getAllegiance() == Allegiance.BLACK)
					newRow = row - 1;
				int newColumn = column - 1;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (!(endTile instanceof King)
						&& chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
						|| endTile instanceof EmptyTile && newPosition.equals(chessBoard.getEnPassantPosition())
						|| returnThreats) {
					enPassantPositions.add(newPosition);
				}
			}

			// One step diagonally forward right.
			if (column < Constants.DEFAULT_NUM_OF_COLUMNS - 1) {
				if (chessPiece.getAllegiance() == Allegiance.WHITE)
					newRow = row + 1;
				if (chessPiece.getAllegiance() == Allegiance.BLACK)
					newRow = row - 1;
				int newColumn = column + 1;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (!(endTile instanceof King)
						&& chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
						|| endTile instanceof EmptyTile && newPosition.equals(chessBoard.getEnPassantPosition())
						|| returnThreats) {
					enPassantPositions.add(newPosition);
				}
			}

		}

		return enPassantPositions;
	}

	// A Pawn is doubled if it has other Pawns on its front rows.
	public boolean isDoubledPawn(String position, ChessBoard chessBoard) {
		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof Pawn)) {
			return false;
		}

		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			for (int i=row+1; i<chessBoard.getNumOfRows(); i++) {
				ChessPiece frontPiece = chessBoard.getGameBoard()[i][column];
				if (frontPiece instanceof Pawn && frontPiece.getAllegiance() == Allegiance.WHITE) {
					return true;
				}
			}
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {
			for (int i=row-1; i>=0; i--) {
				ChessPiece frontPiece = chessBoard.getGameBoard()[i][column];
				if (frontPiece instanceof Pawn && frontPiece.getAllegiance() == Allegiance.BLACK) {
					return true;
				}
			}
		}

		return false;
	}

	// A Pawn is blocked if
	public boolean isBlockedPawn(String position, ChessBoard chessBoard) {
		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof Pawn)) {
			return false;
		}

		int numberOfLegalMoves = chessPiece.getNextPositions(position, chessBoard, false).size();
		return numberOfLegalMoves == 0;
	}

	// A Pawn is blocked if
	public boolean isIsolatedPawn(String position, ChessBoard chessBoard) {
		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof Pawn)) {
			return false;
		}

		// top direction
		if (row < chessBoard.getNumOfRows()-1) {
			ChessPiece neighbour = chessBoard.getGameBoard()[row+1][column];
			if (neighbour instanceof Pawn && chessPiece.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// up and top direction
		if (row < chessBoard.getNumOfRows()-1 && column < Constants.DEFAULT_NUM_OF_COLUMNS-1) {
			ChessPiece neighbour = chessBoard.getGameBoard()[row+1][column+1];
			if (neighbour instanceof Pawn && chessPiece.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// right direction
		if (column < Constants.DEFAULT_NUM_OF_COLUMNS-1) {
			ChessPiece neighbour = chessBoard.getGameBoard()[row][column+1];
			if (neighbour instanceof Pawn && chessPiece.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// right and bottom direction
		if (row > 0 && column < Constants.DEFAULT_NUM_OF_COLUMNS-1) {
			ChessPiece neighbour = chessBoard.getGameBoard()[row-1][column+1];
			if (neighbour instanceof Pawn && chessPiece.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// bottom direction
		if (row > 0) {
			ChessPiece neighbour = chessBoard.getGameBoard()[row-1][column];
			if (neighbour instanceof Pawn && chessPiece.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// left and bottom direction
		if (row > 0 && column > 0) {
			ChessPiece neighbour = chessBoard.getGameBoard()[row-1][column-1];
			if (neighbour instanceof Pawn && chessPiece.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// left direction
		if (column > 0) {
			ChessPiece neighbour = chessBoard.getGameBoard()[row][column-1];
			if (neighbour instanceof Pawn && chessPiece.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// top and left direction
		if (row < chessBoard.getNumOfRows()-1 && column > 0) {
			ChessPiece neighbour = chessBoard.getGameBoard()[row+1][column-1];
			if (neighbour instanceof Pawn && chessPiece.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}

		return true;
	}

}
