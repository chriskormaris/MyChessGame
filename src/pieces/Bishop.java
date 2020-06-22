package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.ChessBoard;
import utilities.Constants;
import utilities.Utilities;


public class Bishop extends ChessPiece {
	
	public Bishop() {
		
	}
	
	public Bishop(int pieceCode) {
		super(pieceCode);
	}
	
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {

		// System.out.println("current position: " + position);

		Set<String> nextBishopPositions = new HashSet<String>();
		
		// First, find the row && the column 
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		int pieceCode = chessBoard.getGameBoard()[row][column];
		
		if (Math.abs(pieceCode) != Constants.BISHOP)
			return nextBishopPositions;

		int newRow = 0, newColumn = 0;
		String newPosition;
		int counter;
		
		// Find all the upper right diagonal positions.
		counter = 1;
		for (int i=row+1; i<chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows() && column + counter < Constants.NUM_OF_COLUMNS) {
				
				newRow = i;
				newColumn = column + counter;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				// System.out.println("newPosition: " + newPosition);
				int endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile == Constants.EMPTY || pieceCode*endTile < 0
					&& endTile != Constants.WHITE_KING && endTile != Constants.BLACK_KING
					|| returnThreats)
					nextBishopPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTile < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn] > 0)
					break;
			}
			counter++;
		}
		
		// Find all the lower left diagonal positions.
		counter = 1;
		for (int i=row-1; i>=0; i--) {
			if (row >= 0 && column - counter >= 0) {
				
				newRow = i;
				newColumn = column - counter;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				// System.out.println("newPosition: " + newPosition);
				int endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile == Constants.EMPTY || pieceCode*endTile < 0
					&& endTile != Constants.WHITE_KING && endTile != Constants.BLACK_KING
					|| returnThreats)
					nextBishopPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTile < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn] > 0)
					break;
			}
			counter++;
		}
		
		// Find all the upper left diagonal positions.
		counter = 1;
		for (int i=row+1; i<chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows() && column - counter >= 0) {
				
				newRow = i;
				newColumn = column - counter;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				// System.out.println("newPosition: " + newPosition);
				int endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile == Constants.EMPTY || pieceCode*endTile < 0
					&& endTile != Constants.WHITE_KING && endTile != Constants.BLACK_KING
					|| returnThreats)
					nextBishopPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTile < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn] > 0)
					break;
			}
			counter++;
		}
		
		// Find all the lower right diagonal positions.
		counter = 1;
		for (int i=row-1; i>=0; i--) {
			if (row >= 0 && column + counter < 8) {
				
				newRow = i;
				newColumn = column + counter;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				// System.out.println("newPosition: " + newPosition);
				int endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile == Constants.EMPTY || pieceCode*endTile < 0
					&& endTile != Constants.WHITE_KING && endTile != Constants.BLACK_KING
					|| returnThreats)
					nextBishopPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTile < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn] > 0)
					break;
			}
			counter++;
		}
		
		return nextBishopPositions;
	}

}
