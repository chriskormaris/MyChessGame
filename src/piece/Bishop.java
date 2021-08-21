package piece;

import java.util.HashSet;
import java.util.Set;

import chess_board.ChessBoard;
import enumeration.Allegiance;
import utility.Constants;
import utility.Utilities;


public class Bishop extends ChessPiece {
	
	public Bishop(Allegiance allegiance) {
		super(allegiance);
	}

	@Override
	public ChessPiece makeCopy() {
		return new Bishop(super.getAllegiance());
	}

	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {

		// System.out.println("current position: " + position);

		Set<String> nextBishopPositions = new HashSet<>();
		
		// First, find the row && the column 
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		ChessPiece chessPiece = chessBoard.getGameBoard()[row][column];
		
		if (!(chessPiece instanceof Bishop)) {
			return nextBishopPositions;
		}

		int counter;
		
		// Find all the upper right diagonal positions.
		counter = 1;
		for (int i=row+1; i<chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows() && column + counter < Constants.DEFAULT_NUM_OF_COLUMNS) {
				
				int newRow = i;
				int newColumn = column + counter;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				// System.out.println("newPosition: " + newPosition);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King) || returnThreats) {
					nextBishopPositions.add(newPosition);
				}
				
				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
					|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
			counter++;
		}
		
		// Find all the lower left diagonal positions.
		counter = 1;
		for (int i=row-1; i>=0; i--) {
			if (row >= 0 && column - counter >= 0) {
				
				int newRow = i;
				int newColumn = column - counter;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				// System.out.println("newPosition: " + newPosition);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King) || returnThreats) {
					nextBishopPositions.add(newPosition);
				}
				
				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
					|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
			counter++;
		}
		
		// Find all the upper left diagonal positions.
		counter = 1;
		for (int i=row+1; i<chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows() && column - counter >= 0) {
				
				int newRow = i;
				int newColumn = column - counter;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				// System.out.println("newPosition: " + newPosition);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King) || returnThreats) {
					nextBishopPositions.add(newPosition);
				}
				
				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
					|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
			counter++;
		}
		
		// Find all the lower right diagonal positions.
		counter = 1;
		for (int i=row-1; i>=0; i--) {
			if (row >= 0 && column + counter < 8) {
				
				int newRow = i;
				int newColumn = column + counter;
				String newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				// System.out.println("newPosition: " + newPosition);
				ChessPiece endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile instanceof EmptyTile || chessPiece.getAllegiance() != endTile.getAllegiance()
					&& !(endTile instanceof King) || returnThreats) {
					nextBishopPositions.add(newPosition);
				}
				
				// Stop searching for other positions, if another chessPiece is reached.
				if (chessPiece.getAllegiance() != endTile.getAllegiance() && !(endTile instanceof EmptyTile)
					|| chessPiece.getAllegiance() == chessBoard.getGameBoard()[newRow][newColumn].getAllegiance()) {
					break;
				}
			}
			counter++;
		}
		
		return nextBishopPositions;
	}

}
