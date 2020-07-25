package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.ChessBoard;
import enums.Allegiance;
import utilities.Constants;
import utilities.Utilities;


public class Pawn extends ChessPiece {
	
	public Pawn(Allegiance allegiance) {
		super(allegiance, Constants.PAWN);
	}
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {

		// System.out.println("current position: " + position);

		Set<String> nextPawnPositions = new HashSet<String>();

		// First, find the row && the column 
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		int pieceCode = chessBoard.getGameBoard()[row][column].getPieceCode();

		if (Math.abs(pieceCode) != Constants.PAWN)
			return nextPawnPositions;
		
		int newRow = 0, newColumn = 0;
		String newPosition;
		
		if ((pieceCode == Constants.WHITE_PAWN && row < chessBoard.getNumOfRows()-1
			|| pieceCode == Constants.BLACK_PAWN && row > 0)) {
			
			if (!returnThreats) {
				// One step forward position.
				if (pieceCode == Constants.WHITE_PAWN)
					newRow = row + 1;
				else if (pieceCode == Constants.BLACK_PAWN)
					newRow = row - 1;
				newColumn = column;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
				// System.out.println("endTileCode: " + endTileCode);
				if (endTileCode == Constants.EMPTY)
					nextPawnPositions.add(newPosition);
				
				// Two steps forward position.
				if (pieceCode == Constants.WHITE_PAWN && row < chessBoard.getNumOfRows()-2 
						&& chessBoard.getGameBoard()[row+2][column].getPieceCode() == Constants.EMPTY 
						&& chessBoard.getGameBoard()[row+1][column].getPieceCode() == Constants.EMPTY
					|| pieceCode == Constants.BLACK_PAWN && row > 1 
					&& chessBoard.getGameBoard()[row-2][column].getPieceCode() == Constants.EMPTY 
					&& chessBoard.getGameBoard()[row-1][column].getPieceCode() == Constants.EMPTY) {
					
					if (pieceCode == Constants.WHITE_PAWN && row == 1)
						newRow = row + 2;
					else if (pieceCode == Constants.BLACK_PAWN && row == chessBoard.getNumOfRows()-2)
						newRow = row - 2;
					newColumn = column;
					newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
					endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
					// System.out.println("endTileCode: " + endTileCode);
					nextPawnPositions.add(newPosition);
				}
				
			}
			
			// One step diagonally forward left.
			if (column > 0) {
				if (pieceCode == Constants.WHITE_PAWN)
					newRow = row + 1;
				else if (pieceCode == Constants.BLACK_PAWN)
					newRow = row - 1;
				newColumn = column - 1;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
				// System.out.println("endTileCode: " + endTileCode);
				if ((pieceCode*endTileCode < 0
					&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING)
					|| returnThreats)
					nextPawnPositions.add(newPosition);
			}
			
			// One step diagonally forward right.
			if (column < Constants.NUM_OF_COLUMNS - 1) {
				if (pieceCode == Constants.WHITE_PAWN)
					newRow = row + 1;
				else if (pieceCode == Constants.BLACK_PAWN)
					newRow = row - 1;
				newColumn = column + 1;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
				// System.out.println("endTileCode: " + endTileCode);
				if ((pieceCode*endTileCode < 0
					&& endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING)
					|| returnThreats)
					nextPawnPositions.add(newPosition);
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

		Set<String> enPassantPositions = new HashSet<String>();

		// First, find the row && the column 
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		int piece = chessBoard.getGameBoard()[row][column].getPieceCode();
		
		if (piece != Constants.WHITE_PAWN && piece != Constants.BLACK_PAWN)
			return enPassantPositions;
		
		int newRow = 0, newColumn = 0;
		String newPosition;
		
		if ((piece == Constants.WHITE_PAWN && row < chessBoard.getNumOfRows()-1
			|| piece == Constants.BLACK_PAWN && row > 0)) {
			
			// One step diagonally forward left.
			if (column > 0) {
				if (piece == Constants.WHITE_PAWN)
					newRow = row + 1;
				else if (piece == Constants.BLACK_PAWN)
					newRow = row - 1;
				newColumn = column - 1;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
				// System.out.println("endTileCode: " + endTileCode);
				if (endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
					&& (piece*endTileCode < 0
					|| endTileCode == Constants.EMPTY && newPosition.equals(chessBoard.getEnPassantPosition()))
					|| returnThreats)
					enPassantPositions.add(newPosition);
			}
			
			// One step diagonally forward right.
			if (column < Constants.NUM_OF_COLUMNS-1) {
				if (piece == Constants.WHITE_PAWN)
					newRow = row + 1;
				else if (piece == Constants.BLACK_PAWN)
					newRow = row - 1;
				newColumn = column + 1;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
				// System.out.println("endTileCode: " + endTileCode);
				if (endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING
					&& (piece*endTileCode < 0
					|| endTileCode == Constants.EMPTY && newPosition.equals(chessBoard.getEnPassantPosition()))
					|| returnThreats)
					enPassantPositions.add(newPosition);
			}
			
		}
		
		return enPassantPositions;
	}
		

}
