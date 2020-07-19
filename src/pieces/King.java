package pieces;

import java.util.HashSet;
import java.util.Set;

import chess.Allegiance;
import chess.ChessBoard;
import utilities.Constants;
import utilities.Utilities;

public class King extends ChessPiece {

	public King() {

	}

	public King(Allegiance allegiance) {
		super(allegiance, Constants.KING);
	}
	
	@Override
	public Set<String> getNextPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		// System.out.println("current position: " + position);

		Set<String> nextKingPositions = new HashSet<String>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = Utilities.getRowFromPosition(position);
		int column = Utilities.getColumnFromPosition(position);
		int pieceCode = chessBoard.getGameBoard()[row][column].getPieceCode();

		if (Math.abs(pieceCode) != Constants.KING)
			return nextKingPositions;

		int newRow = 0, newColumn = 0;
		String newPosition;

		// Up position
		if (row + 1 < chessBoard.getNumOfRows()) {
			newRow = row + 1;
			newColumn = column;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if ((endTileCode == Constants.EMPTY || pieceCode * endTileCode < 0 && endTileCode != Constants.WHITE_KING
					&& endTileCode != Constants.BLACK_KING)
					&& (pieceCode == Constants.WHITE_KING
							&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
							|| pieceCode == Constants.BLACK_KING
									&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Down position
		if (row - 1 >= 0) {
			newRow = row - 1;
			newColumn = column;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if ((endTileCode == Constants.EMPTY || pieceCode * endTileCode < 0 && endTileCode != Constants.WHITE_KING
					&& endTileCode != Constants.BLACK_KING)
					&& (pieceCode == Constants.WHITE_KING
							&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
							|| pieceCode == Constants.BLACK_KING
									&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Right position
		if (column + 1 < Constants.NUM_OF_COLUMNS) {
			newRow = row;
			newColumn = column + 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if ((endTileCode == Constants.EMPTY || pieceCode * endTileCode < 0 && endTileCode != Constants.WHITE_KING
					&& endTileCode != Constants.BLACK_KING)
					&& (pieceCode == Constants.WHITE_KING
							&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
							|| pieceCode == Constants.BLACK_KING
									&& chessBoard.getTilesThreatenedByWhite()[newRow][newColumn] == 0)
					|| returnThreats)
				nextKingPositions.add(newPosition);
		}

		// Left position
		if (column - 1 >= 0) {
			newRow = row;
			newColumn = column - 1;
			newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if ((endTileCode == Constants.EMPTY || pieceCode * endTileCode < 0 && endTileCode != Constants.WHITE_KING
					&& endTileCode != Constants.BLACK_KING)
					&& (pieceCode == Constants.WHITE_KING
							&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
							|| pieceCode == Constants.BLACK_KING
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
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if ((endTileCode == Constants.EMPTY || pieceCode * endTileCode < 0 && endTileCode != Constants.WHITE_KING
					&& endTileCode != Constants.BLACK_KING)
					&& (pieceCode == Constants.WHITE_KING
							&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
							|| pieceCode == Constants.BLACK_KING
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
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if ((endTileCode == Constants.EMPTY || pieceCode * endTileCode < 0 && endTileCode != Constants.WHITE_KING
					&& endTileCode != Constants.BLACK_KING)
					&& (pieceCode == Constants.WHITE_KING
							&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
							|| pieceCode == Constants.BLACK_KING
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
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if ((endTileCode == Constants.EMPTY
					|| pieceCode * endTileCode < 0 && endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING)
					&& (pieceCode == Constants.WHITE_KING
							&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
							|| pieceCode == Constants.BLACK_KING
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
			int endTileCode = chessBoard.getGameBoard()[newRow][newColumn].getPieceCode();
			// System.out.println("endTileCode: " + endTileCode);
			if ((endTileCode == Constants.EMPTY
					|| pieceCode * endTileCode < 0 && endTileCode != Constants.WHITE_KING && endTileCode != Constants.BLACK_KING)
					&& (pieceCode == Constants.WHITE_KING
							&& chessBoard.getTilesThreatenedByBlack()[newRow][newColumn] == 0
							|| pieceCode == Constants.BLACK_KING
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
		int pieceCode = startingPositionGameBoard[row][column].getPieceCode();

		if (pieceCode != Constants.WHITE_KING && pieceCode != Constants.BLACK_KING) {
			return castlingPositions;
		}

		int newRow = 0, newColumn = 0;
		String newPosition;

		chessBoard.setThreats();
		// ChessBoard.printChessBoard(chessBoard.getTilesThreatenedByBlack());

		if (pieceCode == Constants.WHITE_KING) {
			// White castling long
			if (!chessBoard.isWhiteKingMoved() && !chessBoard.isLeftWhiteRookMoved() && !chessBoard.isWhiteKingInCheck()
					&& position.equals("E1") && column > 1 && startingPositionGameBoard[0][0].getPieceCode() == Constants.WHITE_ROOK
					&& startingPositionGameBoard[0][1].getPieceCode() == Constants.EMPTY
					&& startingPositionGameBoard[0][2].getPieceCode() == Constants.EMPTY
					&& startingPositionGameBoard[0][3].getPieceCode() == Constants.EMPTY
					&& chessBoard.getTilesThreatenedByBlack()[0][2] == 0
					&& chessBoard.getTilesThreatenedByBlack()[0][3] == 0) {

				newRow = row;
				newColumn = column - 2;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
			// White castling short
			if (pieceCode == Constants.WHITE_KING && !chessBoard.isWhiteKingMoved() && !chessBoard.isWhiteKingInCheck()
					&& !chessBoard.isRightWhiteRookMoved() && position.equals("E1") && column < 6
					&& startingPositionGameBoard[0][7].getPieceCode() == Constants.WHITE_ROOK
					&& startingPositionGameBoard[0][5].getPieceCode() == Constants.EMPTY
					&& startingPositionGameBoard[0][6].getPieceCode() == Constants.EMPTY
					&& chessBoard.getTilesThreatenedByBlack()[0][5] == 0
					&& chessBoard.getTilesThreatenedByBlack()[0][6] == 0) {

				newRow = row;
				newColumn = column + 2;
				newPosition = Utilities.getPositionByRowCol(newRow, newColumn);
				castlingPositions.add(newPosition);
			}
		} else if (pieceCode == Constants.BLACK_KING) {

			// Black castling long
			if (!chessBoard.isBlackKingMoved() && !chessBoard.isLeftBlackRookMoved() && !chessBoard.isBlackKingInCheck()
					&& position.equals('E' + (chessBoard.getNumOfRows() + "")) && column > 1
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][0].getPieceCode() == Constants.BLACK_ROOK
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][1].getPieceCode() == Constants.EMPTY
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][2].getPieceCode() == Constants.EMPTY
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][3].getPieceCode() == Constants.EMPTY
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
					&& column < 6 && startingPositionGameBoard[chessBoard.getNumOfRows() - 1][7].getPieceCode() == Constants.BLACK_ROOK
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][5].getPieceCode() == Constants.EMPTY
					&& startingPositionGameBoard[chessBoard.getNumOfRows() - 1][6].getPieceCode() == Constants.EMPTY
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
