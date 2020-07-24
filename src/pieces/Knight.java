package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.ChessBoard;
import enums.Allegiance;
import utilities.Constants;
import utilities.Utilities;


public class Knight extends ChessPiece {
		
	public Knight() {
		
	}
	
	public Knight(Allegiance allegiance) {
		super(allegiance, Constants.KNIGHT);
	}
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		// System.out.println("current position: " + position);
		
		Set<String> nextKnightPositions = new HashSet<String>();
		
		// First, find the row && the column 
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		// System.out.println("row: " + row + ", column: " + column);
		int pieceCode = chessBoard.getGameBoard()[row][column].getPieceCode();
		// System.out.println("piece: " + piece);
		
		if (Math.abs(pieceCode) != Constants.KNIGHT)
			return nextKnightPositions;
		
		// The maximum number of next moves that a knight can have is 8
		// && that situation occurs if the knight is in the center of the chess board.
		// In the sketches below, k designates the position of the knight.

		int newRow, newColumn;
		String newPosition;

		/*
		2 steps forward and 1 step left
		__		
		 |
		 |
		 k
		
		*/
		if (row < chessBoard.getNumOfRows()-2 && column >= 1) {
			newRow = row + 2;
			newColumn = column - 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("case 1 position: " + newPosition);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
				&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
				|| returnThreats)
				nextKnightPositions.add(newPosition);
		}

		
		/*
		2 steps forward and 1 step right
		 __
		 |
		 |
		 k
		
		*/
		if (row < chessBoard.getNumOfRows()-2 && column < Constants.NUM_OF_COLUMNS-1) {
			newRow = row + 2;
			newColumn = column + 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("case 2 position: " + newPosition);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
				&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
				|| returnThreats)
				nextKnightPositions.add(newPosition);
		}
		
		/*
		2 steps backwards and 1 step left
		
		 k
		 |
		 |
		——
		
		*/
		if (row >= 2 && column >= 1) {
			newRow = row - 2;
			newColumn = column - 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("case 3 position: " + newPosition);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
				&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
				|| returnThreats)
				nextKnightPositions.add(newPosition);
		}
		
		/*
		2 steps backwards and 1 step right
		
		 k
		 |
		 |
		 ——
		
		*/
		if (row >= 2 && column < Constants.NUM_OF_COLUMNS-1) {
			newRow = row - 2;
			newColumn = column + 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("case 4 position: " + newPosition);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
				&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
				|| returnThreats)
				nextKnightPositions.add(newPosition);
		}
		
		/*
		2 steps left and 1 step forward
		|__ __ k
		*/
		if (row < chessBoard.getNumOfRows()-1 && column >= 2) {
			newRow = row + 1;
			newColumn = column - 2;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("case 5 position: " + newPosition);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
				&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
				|| returnThreats)
				nextKnightPositions.add(newPosition);
		}
		
		/*
		2 steps right and 1 step forward
		k __ __|
		*/
		if (row < chessBoard.getNumOfRows()-1 && column < Constants.NUM_OF_COLUMNS-2) {
			newRow = row + 1;
			newColumn = column + 2;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("case 6 position: " + newPosition);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
				&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
				|| returnThreats)
				nextKnightPositions.add(newPosition);
		}

		/*
		2 steps left and 1 step backwards
		 __ __ k
		|
		*/
		if (row >= 1 && column >= 2) {
			newRow = row - 1;
			newColumn = column - 2;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("case 7 position: " + newPosition);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
				&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
				|| returnThreats)
				nextKnightPositions.add(newPosition);
		}
		
		/*
		2 steps right and 1 step backwards
		k __ __
		       |
		*/
		if (row >= 1 && column < Constants.NUM_OF_COLUMNS-2) {
			newRow = row - 1;
			newColumn = column + 2;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			// System.out.println("case 8 position: " + newPosition);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if (endTileCode == Constants.EMPTY || pieceCode*endTileCode < 0
				&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
				|| returnThreats)
				nextKnightPositions.add(newPosition);
		}
			
		return nextKnightPositions;
	}

	
}
