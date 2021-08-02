package piece;

import java.util.HashSet;
import java.util.Set;

import chess.ChessBoard;
import enumeration.Allegiance;
import utility.Constants;
import utility.Utilities;

public class King extends ChessPiece {

	public King(Allegiance allegiance) {
		super(allegiance);
	}
	
	@Override
	public ChessPiece makeCopy() {
		return new King(super.getAllegiance());
	}
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		// System.out.println("current position: " + position);

		Set<String> nextKingPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];

		if (!(chessPiece instanceof King))
			return nextKingPositions;

		// Up position
		if (row + 1 < chessBoard.getNumOfRows()) {
			int newRow = row + 1;
			int newColumn = column;
			String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Down position
		if (row - 1 >= 0) {
			int newRow = row - 1;
			int newColumn = column;
			String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
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
		if (column + 1 < Constants.DEFAULT_NUM_OF_COLUMNS) {
			int newRow = row;
			int newColumn = column + 1;
			String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Left position
		if (column - 1 >= 0) {
			int newRow = row;
			int newColumn = column - 1;
			String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper right diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column + 1 < Constants.DEFAULT_NUM_OF_COLUMNS) {
			int newRow = row + 1;
			int newColumn = column + 1;
			String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("newPosition: " + newPosition);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower left diagonal position
		if (row - 1 >= 0 && column - 1 >= 0) {

			int newRow = row - 1;
			int newColumn = column - 1;
			String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("newPosition: " + newPosition);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Upper left diagonal position
		if (row + 1 < chessBoard.getNumOfRows() && column - 1 >= 0) {

			int newRow = row + 1;
			int newColumn = column - 1;
			String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("newPosition: " + newPosition);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Lower right diagonal position
		if (row - 1 >= 0 && column + 1 < 8) {
			int newRow = row - 1;
			int newColumn = column + 1;
			String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("newPosition: " + newPosition);
			ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
			// System.out.println("endTile: " + endTile);
			if ((endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King))
					&& ((chessPiece.getAllegiance() == Allegiance.WHITE)
						&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
						|| (chessPiece.getAllegiance() == Allegiance.BLACK)
						&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
				|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		if (!returnThreats) {
			Set<String> castlingPositions = getCastlingPositions(position, chessBoard);
			// System.out.println("castlingPositions: " + castlingPositions);
			nextKingPositions.addAll(castlingPositions);
		}

		return nextKingPositions;
	}

	
	public static Set<String> getCastlingPositions(String position, ChessBoard chessBoard) {
		
		Set<String> castlingPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];
		
		
		if (!(chessPiece instanceof King)) {
			return castlingPositions;
		}

		int newRow, newColumn;
		String newPosition;

		chessBoard.setThreats();
		// ChessBoard.printChessBoard(chessBoard.getTilesThreatenedByBlack());

		if (chessPiece.getAllegiance() == Allegiance.WHITE) {
			// White castling long
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isLeftWhiteRookMoved() && !chessBoard.isWhiteKingInCheck()
				&& position.equals("E1") && column > 1 
				&& chessBoard.getGameBoard()[0][0] instanceof Rook 
				&& chessBoard.getGameBoard()[0][0].getAllegiance() == Allegiance.WHITE 
				&& chessBoard.getGameBoard()[0][1] instanceof EmptyTile
				&& chessBoard.getGameBoard()[0][2] instanceof EmptyTile
				&& chessBoard.getGameBoard()[0][3] instanceof EmptyTile
				&& chessBoard.getTilesThreatenedByBlack()[0][2] == 0
				&& chessBoard.getTilesThreatenedByBlack()[0][3] == 0
				&& chessBoard.getTilesThreatenedByBlack()[0][4] == 0) {
				
				// System.out.println("INSIDE 1 WHITE");
				
				newRow = row;
				newColumn = column - 2;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
			// White castling short
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isWhiteKingInCheck()
					&& !chessBoard.isRightWhiteRookMoved() && position.equals("E1") && column < 6
					&& chessBoard.getGameBoard()[0][7] instanceof Rook 
					&& chessBoard.getGameBoard()[0][7].getAllegiance() == Allegiance.WHITE 
					&& chessBoard.getGameBoard()[0][5] instanceof EmptyTile
					&& chessBoard.getGameBoard()[0][6] instanceof EmptyTile
					&& chessBoard.getTilesThreatenedByBlack()[0][4] == 0
					&& chessBoard.getTilesThreatenedByBlack()[0][5] == 0
					&& chessBoard.getTilesThreatenedByBlack()[0][6] == 0) {

				// System.out.println("INSIDE 2 WHITE");
				
				newRow = row;
				newColumn = column + 2;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
		} else if (chessPiece.getAllegiance() == Allegiance.BLACK) {

			// Black castling long
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isLeftBlackRookMoved() && !chessBoard.isBlackKingInCheck()
					&& position.equals('E' + (chessBoard.getNumOfRows() + "")) && column > 1
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][0] instanceof Rook
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][0].getAllegiance() == Allegiance.BLACK
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][1] instanceof EmptyTile
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][2] instanceof EmptyTile
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][3] instanceof EmptyTile
					&& chessBoard.getTilesThreatenedByWhite()[chessBoard.getNumOfRows() - 1][2] == 0
					&& chessBoard.getTilesThreatenedByWhite()[chessBoard.getNumOfRows() - 1][3] == 0
					&& chessBoard.getTilesThreatenedByWhite()[chessBoard.getNumOfRows() - 1][4] == 0) {

				// System.out.println("INSIDE 1 BLACK");
				
				newRow = row;
				newColumn = column - 2;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
			// Black castling short
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isRightBlackRookMoved()
					&& !chessBoard.isBlackKingInCheck() && position.equals('E' + (chessBoard.getNumOfRows() + ""))
					&& column < 6 && chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][7] instanceof Rook
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][7].getAllegiance() == Allegiance.BLACK
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][5] instanceof EmptyTile
					&& chessBoard.getGameBoard()[chessBoard.getNumOfRows() - 1][6] instanceof EmptyTile
					&& chessBoard.getTilesThreatenedByWhite()[chessBoard.getNumOfRows() - 1][4] == 0
					&& chessBoard.getTilesThreatenedByWhite()[chessBoard.getNumOfRows() - 1][5] == 0
					&& chessBoard.getTilesThreatenedByWhite()[chessBoard.getNumOfRows() - 1][6] == 0) {
				
				// System.out.println("INSIDE 2 BLACK");
				
				newRow = row;
				newColumn = column + 2;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}

		}

		return castlingPositions;
	}

}
