package com.chriskormaris.mychessgame.api.util;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.square.ChessSquare;
import com.chriskormaris.mychessgame.api.square.EmptySquare;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.Set;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BFS {


	// It runs the BFS algorithm.
	// It returns the depth of the shortest path, if it exists, else it returns -1.
	public static int getMinDepth(
			ChessBoard chessBoard,
			ChessSquare piece,
			String startingPosition,
			String endingPosition,
			int maxDepth
	) {
		ChessBoard currentChessBoard = new ChessBoard(chessBoard);
		int[][] visitedChessBoard = new int[8][8];

		LinkedList<BfsPosition> queue = new LinkedList<>();

		int depth = 0;

		int startingRow = chessBoard.getRowFromPosition(startingPosition);
		int startingColumn = chessBoard.getColumnFromPosition(startingPosition);
		BfsPosition startingBfsPosition = new BfsPosition(startingPosition, startingRow, startingColumn, depth);

		queue.add(startingBfsPosition);
		String currentPosition = null;

		while (!queue.isEmpty() && depth <= maxDepth) {

			if (currentPosition != null) {
				int previousRow = chessBoard.getRowFromPosition(currentPosition);
				int previousColumn = chessBoard.getColumnFromPosition(currentPosition);
				currentChessBoard.getGameBoard()[previousRow][previousColumn] = new EmptySquare();
			}

			// Get the first item of the queue and reBfsPosition it.
			BfsPosition currentBfsPosition = queue.poll();

			currentPosition = currentBfsPosition.getPosition();
			depth = currentBfsPosition.getDepth();

			int row = currentBfsPosition.getRow();
			int column = currentBfsPosition.getColumn();

			if (currentPosition.equals(endingPosition)) {
				return depth;
			}

			if (visitedChessBoard[row][column] == 0) {
				visitedChessBoard[row][column] = 1;

				Set<String> nextPositions;
				if (currentBfsPosition.getParentBfsPosition() != null) {
					int currentRow = chessBoard.getRowFromPosition(currentPosition);
					int currentColumn = chessBoard.getColumnFromPosition(currentPosition);
					currentChessBoard.getGameBoard()[currentRow][currentColumn] = piece;
					if (piece.isKing()) {
						if (piece.isWhite()) {
							currentChessBoard.setWhiteKingPosition(currentPosition);
						} else if (piece.isBlack()) {
							currentChessBoard.setBlackKingPosition(currentPosition);
						}
					}
				}
				nextPositions = piece.getNextPositions(currentPosition, currentChessBoard, false);

				for (String candidatePosition : nextPositions) {
					int candidateRow = chessBoard.getRowFromPosition(candidatePosition);
					int candidateColumn = chessBoard.getColumnFromPosition(candidatePosition);
					BfsPosition candidateBfsPosition = new BfsPosition(
							candidatePosition,
							candidateRow,
							candidateColumn,
							depth + 1
					);
					candidateBfsPosition.setParentBfsPosition(currentBfsPosition);

					queue.add(candidateBfsPosition);
				}

			}
		}
		return -1;
	}


	// It runs the simple BFS algorithm.
	// It returns true if the given ChessBoard piece can get
	// from the given starting position to the given ending position, within the specified "maxDepth".
	public static boolean canGoToPosition(
			ChessBoard chessBoard,
			ChessSquare piece,
			String startingPosition,
			String endingPosition,
			int maxDepth
	) {
		return getMinDepth(chessBoard, piece, startingPosition, endingPosition, maxDepth) >= 0;
	}


}
