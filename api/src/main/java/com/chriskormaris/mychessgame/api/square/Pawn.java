package com.chriskormaris.mychessgame.api.square;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.HashSet;
import java.util.Set;


public class Pawn {

	public static Set<String> getNextPositions(String startingPosition, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> nextPawnPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(startingPosition);
		int column = chessBoard.getColumnFromPosition(startingPosition);
		byte pawn = chessBoard.getGameBoard()[row][column];

		if (Math.abs(pawn) != Constants.PAWN) {
			return nextPawnPositions;
		}

		int newRow = 0;

		if (pawn > 0 && 0 < row && row <= chessBoard.getNumOfRows() - 1
				|| pawn < 0 && row < chessBoard.getNumOfRows() - 1) {

			if (!returnThreats) {
				// One step forward position.
				if (pawn > 0) {
					newRow = row - 1;
				} else if (pawn < 0) {
					newRow = row + 1;
				}

				int newColumn = column;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (endSquare == Constants.EMPTY_SQUARE) {
					nextPawnPositions.add(newPosition);
				}

				// Two steps forward position.
				if (pawn > 0 && row > 1
						&& chessBoard.getGameBoard()[row - 2][column] == Constants.EMPTY_SQUARE
						&& chessBoard.getGameBoard()[row - 1][column] == Constants.EMPTY_SQUARE
						|| pawn < 0 && row < chessBoard.getNumOfRows() - 2
						&& chessBoard.getGameBoard()[row + 2][column] == Constants.EMPTY_SQUARE
						&& chessBoard.getGameBoard()[row + 1][column] == Constants.EMPTY_SQUARE) {

					if (pawn > 0
							&& (row == chessBoard.getNumOfRows() - 2 || row == chessBoard.getNumOfRows() - 1)) {
						newRow = row - 2;
					} else if (pawn < 0 && (row == 1 || row == 0)) {
						newRow = row + 2;
					}
					newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
					nextPawnPositions.add(newPosition);
				}
			}

			// One step diagonally forward left.
			if (column > 0) {
				if (pawn > 0 && row > 0) {
					newRow = row - 1;
				} else if (pawn < 0 && row < chessBoard.getNumOfRows() - 1) {
					newRow = row + 1;
				}
				int newColumn = column - 1;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if ((endSquare != Constants.EMPTY_SQUARE && pawn * endSquare <= 0)
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextPawnPositions.add(newPosition);
				}
			}

			// One step diagonally forward right.
			if (column < chessBoard.getNumOfColumns() - 1) {
				if (pawn > 0 && row > 0) {
					newRow = row - 1;
				} else if (pawn < 0 && row < chessBoard.getNumOfRows() - 1) {
					newRow = row + 1;
				}
				int newColumn = column + 1;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if ((endSquare != Constants.EMPTY_SQUARE && pawn * endSquare <= 0)
						&& Math.abs(endSquare) != Constants.KING || returnThreats) {
					nextPawnPositions.add(newPosition);
				}
			}

			Set<String> enPassantPositions = getEnPassantPositions(startingPosition, chessBoard, returnThreats);
			nextPawnPositions.addAll(enPassantPositions);
		}

		return nextPawnPositions;
	}

	// Implementation of the "en passant" moves.
	public static Set<String> getEnPassantPositions(String position, ChessBoard chessBoard, boolean returnThreats) {
		Set<String> enPassantPositions = new HashSet<>();

		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		byte chessSquare = chessBoard.getGameBoard()[row][column];

		if (Math.abs(chessSquare) != Constants.PAWN) {
			return enPassantPositions;
		}

		int newRow = 0;

		if (chessSquare > 0 && row > 0
				|| chessSquare < 0 && row < chessBoard.getNumOfRows() - 1) {

			// One step diagonally forward left.
			if (column > 0) {
				if (chessSquare > 0) {
					newRow = row - 1;
				} else if (chessSquare < 0) {
					newRow = row + 1;
				}
				int newColumn = column - 1;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (Math.abs(endSquare) != Constants.KING
						&& chessSquare * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| endSquare == Constants.EMPTY_SQUARE && newPosition.equals(chessBoard.getEnPassantPosition())
						|| returnThreats) {
					enPassantPositions.add(newPosition);
				}
			}

			// One step diagonally forward right.
			if (column < chessBoard.getNumOfColumns() - 1) {
				if (chessSquare > 0) {
					newRow = row - 1;
				} else if (chessSquare < 0) {
					newRow = row + 1;
				}
				int newColumn = column + 1;
				String newPosition = chessBoard.getPositionByRowCol(newRow, newColumn);
				byte endSquare = chessBoard.getGameBoard()[newRow][newColumn];

				if (Math.abs(endSquare) != Constants.KING
						&& chessSquare * endSquare < 0 && endSquare != Constants.EMPTY_SQUARE
						|| endSquare == Constants.EMPTY_SQUARE && newPosition.equals(chessBoard.getEnPassantPosition())
						|| returnThreats) {
					enPassantPositions.add(newPosition);
				}
			}

		}

		return enPassantPositions;
	}

	// A Pawn is doubled if it has other Pawns on its front rows.
	public static boolean isDoubledPawn(String position, ChessBoard chessBoard) {
		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		byte chessSquare = chessBoard.getGameBoard()[row][column];

		if (Math.abs(chessSquare) != Constants.PAWN) {
			return false;
		}

		if (chessSquare > 0) {
			for (int i = row - 1; i >= 0; i--) {
				byte frontPiece = chessBoard.getGameBoard()[i][column];
				if (Math.abs(frontPiece) == Constants.PAWN && frontPiece < 0) {
					return true;
				}
			}
		} else if (chessSquare < 0) {
			for (int i = row + 1; i < chessBoard.getNumOfRows(); i++) {
				byte frontPiece = chessBoard.getGameBoard()[i][column];
				if (Math.abs(frontPiece) == Constants.PAWN && frontPiece > 0) {
					return true;
				}
			}
		}

		return false;
	}

	// A Pawn is blocked if
	public static boolean isBlockedPawn(String position, ChessBoard chessBoard) {
		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		byte chessSquare = chessBoard.getGameBoard()[row][column];

		if (Math.abs(chessSquare) != Constants.PAWN) {
			return false;
		}

		int numberOfLegalMoves = PieceUtils.getNextPositions(position, chessBoard, false).size();
		return numberOfLegalMoves == 0;
	}

	// A Pawn is blocked if
	public static boolean isIsolatedPawn(String position, ChessBoard chessBoard) {
		// First, find the row && the column
		// that corresponds to the given position String.
		int row = chessBoard.getRowFromPosition(position);
		int column = chessBoard.getColumnFromPosition(position);
		byte chessSquare = chessBoard.getGameBoard()[row][column];

		if (Math.abs(chessSquare) != Constants.PAWN) {
			return false;
		}

		// bottom direction
		if (row < chessBoard.getNumOfRows() - 1) {
			byte neighbour = chessBoard.getGameBoard()[row + 1][column];
			if (Math.abs(neighbour) == Constants.PAWN && chessSquare * neighbour >= 0) {
				return false;
			}
		}
		// bottom and right direction
		if (row < chessBoard.getNumOfRows() - 1 && column < chessBoard.getNumOfColumns() - 1) {
			byte neighbour = chessBoard.getGameBoard()[row + 1][column + 1];
			if (Math.abs(neighbour) == Constants.PAWN && chessSquare * neighbour >= 0) {
				return false;
			}
		}
		// right direction
		if (column < chessBoard.getNumOfColumns() - 1) {
			byte neighbour = chessBoard.getGameBoard()[row][column + 1];
			if (Math.abs(neighbour) == Constants.PAWN && chessSquare * neighbour >= 0) {
				return false;
			}
		}
		// right and top direction
		if (row > 0 && column < chessBoard.getNumOfColumns() - 1) {
			byte neighbour = chessBoard.getGameBoard()[row - 1][column + 1];
			if (Math.abs(neighbour) == Constants.PAWN && chessSquare * neighbour >= 0) {
				return false;
			}
		}
		// top direction
		if (row > 0) {
			byte neighbour = chessBoard.getGameBoard()[row - 1][column];
			if (Math.abs(neighbour) == Constants.PAWN && chessSquare * neighbour >= 0) {
				return false;
			}
		}
		// left and top direction
		if (row > 0 && column > 0) {
			byte neighbour = chessBoard.getGameBoard()[row - 1][column - 1];
			if (Math.abs(neighbour) == Constants.PAWN && chessSquare * neighbour >= 0) {
				return false;
			}
		}
		// left direction
		if (column > 0) {
			byte neighbour = chessBoard.getGameBoard()[row][column - 1];
			if (Math.abs(neighbour) == Constants.PAWN && chessSquare * neighbour >= 0) {
				return false;
			}
		}
		// bottom and left direction
		if (row < chessBoard.getNumOfRows() - 1 && column > 0) {
			byte neighbour = chessBoard.getGameBoard()[row + 1][column - 1];
			return Math.abs(neighbour) != Constants.PAWN || chessSquare * neighbour <= 0;
		}

		return true;
	}

}
