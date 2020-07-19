package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.Allegiance;
import chess.ChessBoard;
import utilities.Constants;
import utilities.Utilities;


public class Rook extends ChessPiece {
		
	public Rook() {
		
	}
	
	public Rook(Allegiance allegiance) {
		super(allegiance, Constants.ROOK);
	}
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		
		// System.out.println("current position: " + position);

		Set<String> nextRookPositions = new HashSet<String>();
		
		// First, find the row && the column 
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		int pieceCode = chessBoard.getGameBoard()[row][column].getPieceCode();
		
		if (Math.abs(pieceCode) != Constants.ROOK)
			return nextRookPositions;

		int newRow = 0, newColumn = 0;
		String newPosition;
		
		// Find all the up positions.
		for (int i=row+1; i<chessBoard.getNumOfRows(); i++) {
			if (row < chessBoard.getNumOfRows()) {
				
				newRow = i;
				newColumn = column;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
				// System.out.println("endTileCode: " + endTileCode);
				if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
					&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
					|| returnThreats)
					nextRookPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTileCode < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn].getPieceCode() > 0)
					break;
			}
		}
		
		// Find all the down positions.
		for (int i=row-1; i>=0; i--) {
			if (row > 0) {
				
				newRow = i;
				newColumn = column;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
				// System.out.println("endTileCode: " + endTileCode);
				if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
					&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
					|| returnThreats)
					nextRookPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTileCode < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn].getPieceCode() > 0)
					break;
			}
		}
		
		// Find all the right positions.
		for (int j=column+1; j<Constants.NUM_OF_COLUMNS; j++) {
			if (column < Constants.NUM_OF_COLUMNS) {
				
				newRow = row;
				newColumn = j;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
				// System.out.println("endTileCode: " + endTileCode);
				if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
					&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
					|| returnThreats)
					nextRookPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTileCode < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn].getPieceCode() > 0)
					break;
			}
		}
		
		// Find all the left positions.
		for (int j=column-1; j>=0; j--) {
			if (column > 0) {
				
				newRow = row;
				newColumn = j;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
				// System.out.println("endTileCode: " + endTileCode);
				if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
					&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
					|| returnThreats)
					nextRookPositions.add(newPosition);
				
				// Stop searching for other positions, if another piece is reached.
				if (pieceCode*endTileCode < 0 || pieceCode*chessBoard.getGameBoard()[newRow][newColumn].getPieceCode() > 0)
					break;
			}
		}
		
		return nextRookPositions;
	
	}

}
