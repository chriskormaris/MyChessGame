package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.ChessBoard;
import utilities.Constants;
import utilities.Utilities;


public class Queen extends ChessPiece {
		
	public Queen() {
		
	}
	
	public Queen(int pieceCode) {
		super(pieceCode);
	}
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		// System.out.println("current position: " + position);

		Set<String> nextQueenPositions = new HashSet<String>();
		
		// First, find the row && the column 
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		int pieceCode = chessBoard.getGameBoard()[row][column];
		
		if (Math.abs(pieceCode) != Constants.QUEEN)
			return nextQueenPositions;
		
		int newRow = 0, newColumn = 0;
		String newPosition;
		
		// Find all the up positions.
		for (int i=row+1; i<chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows()) {
				
				newRow = i;
				newColumn = column;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile == Constants.EMPTY || pieceCode*endTile < 0
					&& endTile != Constants.WHITE_KING && endTile != Constants.BLACK_KING
					|| returnThreats)
					nextQueenPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTile < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn] > 0)
					break;
			}
		}
		
		// Find all the down positions.
		for (int i=row-1; i>=0; i--) {
			if (row > 0) {
				
				newRow = i;
				newColumn = column;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile == Constants.EMPTY || pieceCode*endTile < 0
					&& endTile != Constants.WHITE_KING && endTile != Constants.BLACK_KING
					|| returnThreats)
					nextQueenPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTile < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn] > 0)
					break;
			}
		}
		
		// Find all the right positions.
		for (int j=column+1; j<Constants.NUM_OF_COLUMNS; j++) {
			if (column < Constants.NUM_OF_COLUMNS) {
				
				newRow = row;
				newColumn = j;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile == Constants.EMPTY || pieceCode*endTile < 0
					&& endTile != Constants.WHITE_KING && endTile != Constants.BLACK_KING
					|| returnThreats)
					nextQueenPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTile < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn] > 0)
					break;
			}
		}
		
		// Find all the left positions.
		for (int j=column-1; j>=0; j--) {
			if (column > 0) {
				
				newRow = row;
				newColumn = j;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile == Constants.EMPTY || pieceCode*endTile < 0
					&& endTile != Constants.WHITE_KING && endTile != Constants.BLACK_KING
					|| returnThreats)
					nextQueenPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTile < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn] > 0)
					break;
			}
		}
		
		
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
					nextQueenPositions.add(newPosition);
				
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
					nextQueenPositions.add(newPosition);
				
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
					nextQueenPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTile < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn] > 0)
					break;
			}
			counter++;
		}
		
		// Find all the lower right diagonal positions.
		counter = 1;
		for (int i=row-1; i>=0; i--) {
			if (row >= 0 && column + counter < Constants.NUM_OF_COLUMNS) {
				
				newRow = i;
				newColumn = column + counter;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				// System.out.println("newPosition: " + newPosition);
				int endTile = chessBoard.getGameBoard()[newRow][newColumn];
				// System.out.println("endTile: " + endTile);
				if (endTile == Constants.EMPTY || pieceCode*endTile < 0
					&& endTile != Constants.WHITE_KING && endTile != Constants.BLACK_KING
					|| returnThreats)
					nextQueenPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTile < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn] > 0)
					break;
			}
			counter++;
		}
		
		return nextQueenPositions;
	}

	
}
