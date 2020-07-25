package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.ChessBoard;
import enums.Allegiance;
import utilities.Constants;
import utilities.Utilities;

public class King extends ChessPiece {

	public King(Allegiance allegiance) {
		super(allegiance);
	}
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		// System.out.println("current position: " + position);

		Set<String> nextKingPositions = new HashSet<String>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof King))
			return nextKingPositions;

		int newRow = 0, newColumn = 0;
		String newPosition;

		// Up position
		if (row + 1 < chessBoard.getNumOfRows()) {
			newRow = row + 1;
			newColumn = column;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Down position
		if (row - 1 >= 0) {
			newRow = row - 1;
			newColumn = column;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Right position
		if (column + 1 < Constants.NUM_OF_COLUMNS) {
			newRow = row;
			newColumn = column + 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Left position
		if (column - 1 >= 0) {
			newRow = row;
			newColumn = column - 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper right diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column + 1 < Constants.NUM_OF_COLUMNS) {
			newRow = row + 1;
			newColumn = column + 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("newPosition: " + newPosition);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower left diagonal position
		if (row - 1 >= 0 && column - 1 >= 0) {

			newRow = row - 1;
			newColumn = column - 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("newPosition: " + newPosition);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper left diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column - 1 >= 0) {

			newRow = row + 1;
			newColumn = column - 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("newPosition: " + newPosition);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower right diagonal position
		if (row - 1 >= 0 && column + 1 < 8) {
			newRow = row - 1;
			newColumn = column + 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("newPosition: " + newPosition);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		if (!returnThreats) {
			Set<String> castlingPositions = getCastlingPositions(position, chessBoard, chessBoard.getGameBoard());
			// System.out.println("castlingPositions: " + castlingPositions);
			nextKingPositions.addAll(castlingPositions);
		}

		return nextKingPositions;
	}

	public static Set<String> getCastlingPositions(String position, ChessBoard chessBoard,
			ChessPiece[][] startingPositionGameBoard) {
		// System.out.println("current position: " + position);

		Set<String> castlingPositions = new HashSet<String>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = startingPositionGameBoard[row][column];

		if (!(chessPiece instanceof King)) {
			return castlingPositions;
		}

		int newRow = 0, newColumn = 0;
		String newPosition;

		chessBoard.setThreats();
		// ChessBoard.printChessBoard(chessBoard.getTilesThreatenedByBlack());

		if (chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.WHITE) {
			// White castling long
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isLeftWhiteRookMoved() && !chessBoard.isWhiteKingInCheck()
				&& position.equals("E1") && column > 1 
				&& startingPositionGameBoard[0][0] instanceof Rook 
				&& startingPositionGameBoard[0][0].getAllegiance() == Allegiance.WHITE 
				&& startingPositionGameBoard[0][1] instanceof EmptyTile
				&& startingPositionGameBoard[0][2] instanceof EmptyTile
				&& startingPositionGameBoard[0][3] instanceof EmptyTile
				&& chessBoard.getTilesThreatenedByBlack()[0][2] == 0
				&& chessBoard.getTilesThreatenedByBlack()[0][3] == 0) {

				newRow = row;
				newColumn = column - 2;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
			// White castling short
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isWhiteKingInCheck()
					&& !chessBoard.isRightWhiteRookMoved() && position.equals("E1") && column < 6
					&& startingPositionGameBoard[0][7] instanceof Rook 
					&& startingPositionGameBoard[0][7].getAllegiance() == Allegiance.WHITE 
					&& startingPositionGameBoard[0][5] instanceof EmptyTile
					&& startingPositionGameBoard[0][6] instanceof EmptyTile
					&& chessBoard.getTilesThreatenedByBlack()[0][5] == 0
					&& chessBoard.getTilesThreatenedByBlack()[0][6] == 0) {

				newRow = row;
				newColumn = column + 2;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
		} else if (chessPiece instanceof King && chessPiece.getAllegiance() == Allegiance.BLACK) {

			// Black castling long
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isLeftBlackRookMoved() && !chessBoard.isBlackKingInCheck()
					&& position.equals('E' + (chessBoard.getNumOfRows() + "")) && column > 1
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][0] instanceof Rook
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][0].getAllegiance() == Allegiance.BLACK
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][1] instanceof EmptyTile
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][2] instanceof EmptyTile
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][3] instanceof EmptyTile
					&& chessBoard.getTilesThreatenedByWhite()[chessBoard.getNumOfRows() - 1][2] == 0
					&& chessBoard.getTilesThreatenedByWhite()[chessBoard.getNumOfRows() - 1][3] == 0) {

				newRow = row;
				newColumn = column - 2;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
			// Black castling short
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isRightBlackRookMoved()
					&& !chessBoard.isBlackKingInCheck() && position.equals('E' + (chessBoard.getNumOfRows() + ""))
					&& column < 6 && startingPositionGameBoard[chessBoard.getNumOfRows() - 1][7] instanceof Rook
					&& column < 6 && startingPositionGameBoard[chessBoard.getNumOfRows() - 1][7].getAllegiance() == Allegiance.BLACK
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][5] instanceof EmptyTile
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][6] instanceof EmptyTile
					&& chessBoard.getTilesThreatenedByWhite()[chessBoard.getNumOfRows() - 1][5] == 0
					&& chessBoard.getTilesThreatenedByWhite()[chessBoard.getNumOfRows() - 1][6] == 0) {

				newRow = row;
				newColumn = column + 2;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}

		}

		return castlingPositions;
	}

}
