package utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import chess.ChessBoard;
import enumeration.Allegiance;
import piece.ChessPiece;
import piece.EmptyTile;
import piece.King;


public class ChessPieceShortestPath {

	
	// It runs the simple BFS algorithm.
	// It returns all the possible paths that match the given starting and ending position, 
	// within the specified "maxDepth".
	public static List<List<BfsPosition>> getSolutionPaths(ChessBoard chessBoard, ChessPiece piece, 
										String startingPosition, String endingPosition, int maxDepth) {
		ChessBoard currentChessBoard = new ChessBoard(chessBoard);

		List<List<BfsPosition>> solutionPaths = new ArrayList<List<BfsPosition>>();
		List<BfsPosition> lastBfsPositions = new ArrayList<BfsPosition>();
		
		int[][] visitedChessBoard = new int[8][8];
		
        LinkedList<BfsPosition> queue = new LinkedList<BfsPosition>();
		
        int depth = 0;
        
		int startingRow = Utilities.getRowFromPosition(startingPosition);
		int startingColumn = Utilities.getColumnFromPosition(startingPosition);
		BfsPosition startingBfsPosition = new BfsPosition(startingPosition, startingRow, startingColumn, depth);
		
		queue.add(startingBfsPosition);
		String currentPosition = null;
		
		while (queue.size() != 0 && depth <= maxDepth) {
			
			if (currentPosition != null) {
				int previousRow = Utilities.getRowFromPosition(currentPosition);
				int previousColumn = Utilities.getColumnFromPosition(currentPosition);
				currentChessBoard.getGameBoard()[previousRow][previousColumn] = new EmptyTile();
			}
			
			// Get the first item of the queue and reBfsPosition it.
			BfsPosition currentBfsPosition = queue.poll();
			
			currentPosition = currentBfsPosition.getPosition();
			depth = currentBfsPosition.getDepth();
			
			int row = currentBfsPosition.getRow();
			int column = currentBfsPosition.getColumn();
			
			// System.out.println("current position: " + currentBfsPosition);
			
			if (currentPosition.equals(endingPosition)) {
				// System.out.println("position reached: " + currentPosition + ", depth: " + depth);
				lastBfsPositions.add(currentBfsPosition);
			}
			
			if (visitedChessBoard[row][column] == 0) {
				visitedChessBoard[row][column] = 1;
				
				Set<String> nextPositions;
				if (currentBfsPosition.getParentBfsPosition() != null) {
					int currentRow = Utilities.getRowFromPosition(currentPosition);
					int currentColumn = Utilities.getColumnFromPosition(currentPosition);
					currentChessBoard.getGameBoard()[currentRow][currentColumn] = piece;
					if (piece instanceof King) {
						if (piece.getAllegiance() == Allegiance.WHITE) {
							currentChessBoard.setWhiteKingPosition(currentPosition);
						} else if (piece.getAllegiance() == Allegiance.BLACK) {
							currentChessBoard.setBlackKingPosition(currentPosition);
						}
					}
					nextPositions = piece.getNextPositions(currentPosition, currentChessBoard, false);
				} else {
					nextPositions = piece.getNextPositions(currentPosition, currentChessBoard, false);
				}
				// System.out.println("nextPositions: " + nextPositions);
			
				for (String candidatePosition: nextPositions) {
					// System.out.println("candidate position: " + candidatePosition + ", depth: " + (depth + 1));
	
					int candidateRow = Utilities.getRowFromPosition(candidatePosition);
					int candidateColumn = Utilities.getColumnFromPosition(candidatePosition);
					BfsPosition candidateBfsPosition = new BfsPosition(candidatePosition, candidateRow, candidateColumn, depth + 1);
					candidateBfsPosition.setParentBfsPosition(currentBfsPosition);
					
					queue.add(candidateBfsPosition);
				}
				
			}
			// System.out.println("depth: " + depth);
			// System.out.println("*********************");
		}
		
		for (BfsPosition lastBfsPosition: lastBfsPositions) {
			List<BfsPosition> solutionPath = ChessPieceShortestPath.backtrack(lastBfsPosition);
			solutionPaths.add(solutionPath);
		}
		
		// System.out.println("depth: " + depth);
		// System.out.println("***************");
		return solutionPaths;
	}
	
	
	// It runs the BFS algorithm.
	// It returns the depth of the shortest path, if exists else it returns -1.
	public static int getMinDepth(ChessBoard chessBoard, ChessPiece piece, 
										String startingPosition, String endingPosition, int maxDepth) {
		ChessBoard currentChessBoard = new ChessBoard(chessBoard);
		int[][] visitedChessBoard = new int[8][8];
		
        LinkedList<BfsPosition> queue = new LinkedList<BfsPosition>();
		
        int depth = 0;
        
		int startingRow = Utilities.getRowFromPosition(startingPosition);
		int startingColumn = Utilities.getColumnFromPosition(startingPosition);
		BfsPosition startingBfsPosition = new BfsPosition(startingPosition, startingRow, startingColumn, depth);
		
		queue.add(startingBfsPosition);
		String currentPosition = null;
		
		while (queue.size() != 0 && depth <= maxDepth) {
			
			if (currentPosition != null) {
				int previousRow = Utilities.getRowFromPosition(currentPosition);
				int previousColumn = Utilities.getColumnFromPosition(currentPosition);
				currentChessBoard.getGameBoard()[previousRow][previousColumn] = new EmptyTile();
			}
			
			// Get the first item of the queue and reBfsPosition it.
			BfsPosition currentBfsPosition = queue.poll();
			
			currentPosition = currentBfsPosition.getPosition();
			depth = currentBfsPosition.getDepth();
			
			int row = currentBfsPosition.getRow();
			int column = currentBfsPosition.getColumn();
			
			// System.out.println("current position: " + currentBfsPosition);
			
			if (currentPosition.equals(endingPosition)) {
				// System.out.println("position reached: " + currentPosition + ", depth: " + depth);
				// System.out.println("The minimum depth is: " + depth);
				return depth;
			}
			
			if (visitedChessBoard[row][column] == 0) {
				visitedChessBoard[row][column] = 1;
				
				Set<String> nextPositions;
				if (currentBfsPosition.getParentBfsPosition() != null) {
					int currentRow = Utilities.getRowFromPosition(currentPosition);
					int currentColumn = Utilities.getColumnFromPosition(currentPosition);
					currentChessBoard.getGameBoard()[currentRow][currentColumn] = piece;
					if (piece instanceof King) {
						if (piece.getAllegiance() == Allegiance.WHITE) {
							currentChessBoard.setWhiteKingPosition(currentPosition);
						} else if (piece.getAllegiance() == Allegiance.BLACK) {
							currentChessBoard.setBlackKingPosition(currentPosition);
						}
					}
					nextPositions = piece.getNextPositions(currentPosition, currentChessBoard, false);
				} else {
					nextPositions = piece.getNextPositions(currentPosition, currentChessBoard, false);
				}
				// System.out.println("nextPositions: " + nextPositions);
			
				for (String candidatePosition: nextPositions) {
					// System.out.println("candidate position: " + candidatePosition + ", depth: " + (depth + 1));
	
					int candidateRow = Utilities.getRowFromPosition(candidatePosition);
					int candidateColumn = Utilities.getColumnFromPosition(candidatePosition);
					BfsPosition candidateBfsPosition = new BfsPosition(candidatePosition, candidateRow, candidateColumn, depth + 1);
					candidateBfsPosition.setParentBfsPosition(currentBfsPosition);
					
					queue.add(candidateBfsPosition);
				}
				
			}
			// System.out.println("depth: " + depth);
			// System.out.println("*********************");
		}
		return -1;
	}
	

	// It runs the simple BFS algorithm.
	// It returns true if the given chess piece can get from the given starting position to the given ending position, 
	// within the specified "maxDepth".
	public static boolean canGoToPosition(ChessBoard chessBoard, ChessPiece piece, 
										String startingPosition, String endingPosition, int maxDepth) {
		if (getMinDepth(chessBoard, piece, startingPosition, endingPosition, maxDepth) >= 0)
			return true;
		else
			return false;
	}
	
	
	public static List<BfsPosition> backtrack(BfsPosition lastBfsPosition) {
		List<BfsPosition> solutionPath = new ArrayList<BfsPosition>();
		BfsPosition parentBfsPosition = lastBfsPosition;
		while (parentBfsPosition != null) {
			BfsPosition currentBfsPosition = parentBfsPosition;
			solutionPath.add(currentBfsPosition);
			
			parentBfsPosition = currentBfsPosition.getParentBfsPosition();
		}
		Collections.reverse(solutionPath);
		return solutionPath;
	}
	
	
	public static void printSolutions(List<List<BfsPosition>> solutionPaths) {
		
		if (solutionPaths.size() > 0) {
			int pathCounter = 1;
			for (List<BfsPosition> solutionPath: solutionPaths) {
				String pathOutput = getPathOutput(solutionPath);

				if (solutionPaths.size() == 1) {
					System.out.print("only solution path: ");
				} else {
					System.out.print("solution path #" + pathCounter + ": ");
				}
				System.out.println(pathOutput);

				pathCounter++;
			}
			
		}
		
	}
	
	
	public static String getShortestPath(List<List<BfsPosition>> solutionPaths, String maxDepth) {
		
		String pathOutput = "";
		
		if (solutionPaths.size() > 0) {
			int pathCounter = 1;
			List<BfsPosition> shortestPath = solutionPaths.get(0);
			int minDepth = 1000;
			int shortestPathNumber = pathCounter;
			
			// Find the path with the minimum depth, by comparing all paths together.
			for (List<BfsPosition> solutionPath: solutionPaths) {
				if (solutionPath.size() - 1 <= minDepth) {
					shortestPath = solutionPath;
					minDepth = solutionPath.size() - 1;
					shortestPathNumber = pathCounter;
				}
				pathCounter++;
			}

			pathOutput = getPathOutput(shortestPath);
			if (solutionPaths.size() == 1) {
				System.out.print("The shortest path is: ");
			} else {
				System.out.print("The shortest path is path #" + shortestPathNumber + ": ");
			}
			System.out.println(pathOutput);
			
		} else {
			System.out.println("There is no solution for max depth " + maxDepth + ".");
		}
		return pathOutput;
		
	}
	

	public static String getPathOutput(List<BfsPosition> path) {
		String output = "";
		for (int i=0; i<path.size(); i++) {
			if (i < path.size() - 1) {
				output += path.get(i).getPosition() + " -> ";
			} else {
				output += path.get(i).getPosition();
			}
		}
		return output;
	}
	
}
