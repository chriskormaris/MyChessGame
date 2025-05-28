package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;

import java.util.HashSet;
import java.util.Set;


public class Pawn extends ChessPiece {

	public Pawn(Allegiance allegiance) {
		super(allegiance);
	}

	@Override
	public Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextPawnPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(startingPosition);
		int column = chessBoard.getColumnFromPosition(startingPosition);
		ChessSquare pawn = chessBoard.getGameBoard()[row][column];

		if (!pawn.isPawn()) {
			return nextPawnPositions;
		}

		int newRow = 0;

		if (pawn.isWhite() && 0 < row && row <= chessBoard.getNumOfRows() - 1
				|| pawn.isBlack() && row < chessBoard.getNumOfRows() - 1) {

			if (!returnThreats) {
				// One step forward position.
				if (pawn.isWhite()) {
					newRow = row - 1;
				} else if (pawn.isBlack()) {
					newRow = row + 1;
				}

				int newColumn = column;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare.isEmpty()) {
					nextPawnPositions.add(newPosition);
				}

				// Two steps forward position.
				if (pawn.isWhite() && row > 1
						&& chessBoard.getGameBoard()[row - 2][column].isEmpty()
						&& chessBoard.getGameBoard()[row - 1][column].isEmpty()
						|| pawn.isBlack() && row < chessBoard.getNumOfRows() - 2
						&& chessBoard.getGameBoard()[row + 2][column].isEmpty()
						&& chessBoard.getGameBoard()[row + 1][column].isEmpty()) {

					if (pawn.isWhite()
							&& (row == chessBoard.getNumOfRows() - 2 || row == chessBoard.getNumOfRows() - 1)) {
						newRow = row - 2;
					} else if (pawn.isBlack() && (row == 1 || row == 0)) {
						newRow = row + 2;
					}
					newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
					nextPawnPositions.add(newPosition);
				}
			}

			// One step diagonally forward left.
			if (column > 0) {
				if (pawn.isWhite() && row > 0) {
					newRow = row - 1;
				} else if (pawn.isBlack() && row < chessBoard.getNumOfRows() - 1) {
					newRow = row + 1;
				}
				int newColumn = column - 1;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if ((!endSquare.isEmpty() && pawn.getAllegiance() != endSquare.getAllegiance())
						&& !endSquare.isKing() || returnThreats) {
					nextPawnPositions.add(newPosition);
				}
			}

			// One step diagonally forward right.
			if (column < chessBoard.getNumOfColumns() - 1) {
				if (pawn.isWhite() && row > 0) {
					newRow = row - 1;
				} else if (pawn.isBlack() && row < chessBoard.getNumOfRows() - 1) {
					newRow = row + 1;
				}
				int newColumn = column + 1;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if ((!endSquare.isEmpty() && pawn.getAllegiance() != endSquare.getAllegiance())
						&& !endSquare.isKing() || returnThreats) {
					nextPawnPositions.add(newPosition);
				}
			}

			Set<String> enPassantPositions = getEnPassantPositions(startingPosition, chessBoard, returnThreats);
			nextPawnPositions.addAll(enPassantPositions);
		}

		return nextPawnPositions;
	}

	// Implementation of the "en passant" moves.
	public Set<String> getEnPassantPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> enPassantPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		ChessSquare chessSquare = chessBoard.getGameBoard()[row][column];

		if (!chessSquare.isPawn()) {
			return enPassantPositions;
		}

		int newRow = 0;

		if (chessSquare.isWhite() && row > 0
				|| chessSquare.isBlack() && row < chessBoard.getNumOfRows() - 1) {

			// One step diagonally forward left.
			if (column > 0) {
				if (chessSquare.isWhite()) {
					newRow = row - 1;
				} else if (chessSquare.isBlack()) {
					newRow = row + 1;
				}
				int newColumn = column - 1;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (!endSquare.isKing()
						&& chessSquare.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| endSquare.isEmpty() && newPosition.equals(chessBoard.getEnPassantPosition())
						|| returnThreats) {
					enPassantPositions.add(newPosition);
				}
			}

			// One step diagonally forward right.
			if (column < chessBoard.getNumOfColumns() - 1) {
				if (chessSquare.isWhite()) {
					newRow = row - 1;
				} else if (chessSquare.isBlack()) {
					newRow = row + 1;
				}
				int newColumn = column + 1;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				ChessSquare endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (!endSquare.isKing()
						&& chessSquare.getAllegiance() != endSquare.getAllegiance() && !endSquare.isEmpty()
						|| endSquare.isEmpty() && newPosition.equals(chessBoard.getEnPassantPosition())
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
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		ChessSquare chessSquare = chessBoard.getGameBoard()[row][column];

		if (!chessSquare.isPawn()) {
			return false;
		}

		if (chessSquare.isWhite()) {
			for (int i = row - 1; i >= 0; i--) {
				ChessSquare frontPiece = chessBoard.getGameBoard()[i][column];
				if (frontPiece.isPawn() && frontPiece.isBlack()) {
					return true;
				}
			}
		} else if (chessSquare.isBlack()) {
			for (int i = row + 1; i < chessBoard.getNumOfRows(); i++) {
				ChessSquare frontPiece = chessBoard.getGameBoard()[i][column];
				if (frontPiece.isPawn() && frontPiece.isWhite()) {
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
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		ChessSquare chessSquare = chessBoard.getGameBoard()[row][column];

		if (!chessSquare.isPawn()) {
			return false;
		}

		int numberOfLegalMoves = chessSquare.getNextPositions(position, chessBoard, false).size();
		return numberOfLegalMoves == 0;
	}

	// A Pawn is blocked if
	public boolean isIsolatedPawn(String position, ChessBoard chessBoard) {
		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		ChessSquare chessSquare = chessBoard.getGameBoard()[row][column];

		if (!chessSquare.isPawn()) {
			return false;
		}

		// bottom direction
		if (row < chessBoard.getNumOfRows() - 1) {
			ChessSquare neighbour = chessBoard.getGameBoard()[row + 1][column];
			if (neighbour.isPawn() && chessSquare.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// bottom and right direction
		if (row < chessBoard.getNumOfRows() - 1 && column < chessBoard.getNumOfColumns() - 1) {
			ChessSquare neighbour = chessBoard.getGameBoard()[row + 1][column + 1];
			if (neighbour.isPawn() && chessSquare.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// right direction
		if (column < chessBoard.getNumOfColumns() - 1) {
			ChessSquare neighbour = chessBoard.getGameBoard()[row][column + 1];
			if (neighbour.isPawn() && chessSquare.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// right and top direction
		if (row > 0 && column < chessBoard.getNumOfColumns() - 1) {
			ChessSquare neighbour = chessBoard.getGameBoard()[row - 1][column + 1];
			if (neighbour.isPawn() && chessSquare.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// top direction
		if (row > 0) {
			ChessSquare neighbour = chessBoard.getGameBoard()[row - 1][column];
			if (neighbour.isPawn() && chessSquare.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// left and top direction
		if (row > 0 && column > 0) {
			ChessSquare neighbour = chessBoard.getGameBoard()[row - 1][column - 1];
			if (neighbour.isPawn() && chessSquare.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// left direction
		if (column > 0) {
			ChessSquare neighbour = chessBoard.getGameBoard()[row][column - 1];
			if (neighbour.isPawn() && chessSquare.getAllegiance() == neighbour.getAllegiance()) {
				return false;
			}
		}
		// bottom and left direction
		if (row < chessBoard.getNumOfRows() - 1 && column > 0) {
			ChessSquare neighbour = chessBoard.getGameBoard()[row + 1][column - 1];
			return !neighbour.isPawn() || chessSquare.getAllegiance() != neighbour.getAllegiance();
		}

		return true;
	}

}
