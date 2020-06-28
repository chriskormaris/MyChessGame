package minimaxAi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chess.ChessBoard;
import chess.Move;
import utilities.Constants;

public class MiniMaxAi {

	// Variable that holds the maximum depth the MiniMaxAi algorithm will reach for this player.
	private int maxDepth;

    // Variable that holds which player played last; the opposite player of the one that plays.
	private boolean aiPlayer;

	public MiniMaxAi() {
		maxDepth = 2;
		aiPlayer = Constants.BLACK;
	}
	
	public MiniMaxAi(int maxDepth, boolean aiPlayer) {
		this.maxDepth = maxDepth;
		this.aiPlayer = aiPlayer;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public boolean getAiPlayer() {
		return aiPlayer;
	}

	public void setAiPlayer(boolean aiPlayer) {
		this.aiPlayer = aiPlayer;
	}
	
    // Initiates the minimax algorithm
	public Move miniMax(ChessBoard chessBoard) {
        // If White plays, then it wants to MAXimize the heuristics value.
        if (aiPlayer == Constants.WHITE) {
            return max(new ChessBoard(chessBoard), 0);
        }
        // If Black plays, then it wants to MINimize the heuristics value.
        else {
            return min(new ChessBoard(chessBoard), 0);
        }
	}

    // The max and min functions are called interchangeably, one after another until a max depth is reached
	public Move max(ChessBoard chessBoard, int depth) {
        Random r = new Random();

        /* If MAX is called on a state that is terminal or after a maximum depth is reached,
         * then a heuristic is calculated on the state and the move returned.
         */
		if ((chessBoard.checkForTerminalState()) || (depth == maxDepth)) {
			Move lastMove = new Move(chessBoard.getLastMove());
			// System.out.println("max, depth: " + depth + ", lastMove -> " + lastMove);
			return lastMove;
		}
        // The children-moves of the state are calculated
        List<ChessBoard> children = new ArrayList<ChessBoard>(chessBoard.getChildren(Constants.WHITE));
        if (children.size() == 1) {
        	return children.get(0).getLastMove();
		}
        
        Move maxMove = new Move(Integer.MIN_VALUE);
		for (ChessBoard child : children) {
            // And for each child min is called, on a lower depth
			Move move = min(child, depth + 1);
            // The child-move with the greatest value is selected and returned by max
			if (move.getValue() >= maxMove.getValue()) {
                if ((move.getValue() == maxMove.getValue())) {
                    // If the heuristic has the same value then we randomly choose one of the two moves
                    if (r.nextInt(2) == 1) {
                        maxMove.setPositions(child.getLastMove().getPositions());
                        maxMove.setValue(move.getValue());
                    }
                } else {
                    maxMove.setPositions(child.getLastMove().getPositions());
                    maxMove.setValue(move.getValue());
                }
			}
		}
		// System.out.println("max, depth: " + depth + ", maxMove -> " + maxMove);
		return maxMove;
	}
	
    // Min works similarly to max
	public Move min(ChessBoard chessBoard, int depth) {
        Random r = new Random();

		if ((chessBoard.checkForTerminalState()) || (depth == maxDepth)) {
			Move lastMove = new Move(chessBoard.getLastMove());
			// System.out.println("min, depth: " + depth + ", lastMove -> " + lastMove);
			return lastMove;
		}
        List<ChessBoard> children = new ArrayList<ChessBoard>(chessBoard.getChildren(Constants.BLACK));
        if (children.size() == 1) {
        	return children.get(0).getLastMove();
		}
        
        Move minMove = new Move(Integer.MAX_VALUE);
		for (ChessBoard child : children) {
			Move move = max(child, depth + 1);
			if(move.getValue() <= minMove.getValue()) {
                if ((move.getValue() == minMove.getValue())) {
                    if (r.nextInt(2) == 0) {
                        minMove.setPositions(child.getLastMove().getPositions());
                        minMove.setValue(move.getValue());
                    }
                } else {
                    minMove.setPositions(child.getLastMove().getPositions());
                    minMove.setValue(move.getValue());
                }
            }
        }
		// System.out.println("min, depth: " + depth + ", minMove -> " + minMove);
        return minMove;
    }
	
	// Initiates the minimax Alpha-Beta Pruning algorithm
	public Move miniMaxAlphaBeta(ChessBoard chessBoard) {
        // If White plays, then it wants to MAXimize the heuristics value.
        if (aiPlayer == Constants.WHITE) {
        	
        	Move maxMove = maxAlphaBeta(new ChessBoard(chessBoard), 0, Double.MAX_VALUE, Integer.MIN_VALUE);
        	// System.out.println("miniMax maxMove -> " + maxMove);
        	
            return maxMove;
        }
        // If Black plays, then it wants to MINimize the heuristics value.
        else {
            return minAlphaBeta(new ChessBoard(chessBoard), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
	}

    // The max and min functions are called interchangeably, one after another until a max depth is reached.
	public Move maxAlphaBeta(ChessBoard chessBoard, int depth, double a, double b) {
        Random r = new Random();
        
        /* If MAX is called on a state that is terminal or after a maximum depth is reached,
         * then a heuristic is calculated on the state and the move returned.
         */
		if ((chessBoard.checkForTerminalState()) || (depth == maxDepth)) {
			Move lastMove = new Move(chessBoard.getLastMove());
			// System.out.println("max, depth: " + depth + ", lastMove -> " + lastMove);
			return lastMove;
		}
        // The children-moves of the state are calculated
        List<ChessBoard> children = new ArrayList<ChessBoard>(chessBoard.getChildren(Constants.WHITE));
        if (children.size() == 1) {
        	return children.get(0).getLastMove();
		}
        
		Move maxMove = new Move(Integer.MIN_VALUE);
		for (ChessBoard child : children) {
		
            // And for each child min is called, on a lower depth
			Move move = minAlphaBeta(child, depth + 1, a, b);
            // The child-move with the greatest value is selected and returned by max
			if (move.getValue() >= maxMove.getValue()) {
                if ((move.getValue() == maxMove.getValue())) {
                    // If the heuristic has the same value then we randomly choose one of the two moves
                    if (r.nextInt(2) == 1) {
                        maxMove.setPositions(child.getLastMove().getPositions());
                        maxMove.setValue(move.getValue());
                    }
                } else {
                    maxMove.setPositions(child.getLastMove().getPositions());
                    maxMove.setValue(move.getValue());
                }
			}
			
			// Beta pruning.
            if (maxMove.getValue() >= b) {
    			// System.out.println("max, depth: " + depth + ", beta pruning move -> " + maxMove);
            	return maxMove; 
            }

            // Update the a of the current max node.
            a = (a > maxMove.getValue()) ? a : maxMove.getValue();
		}
		// System.out.println("max, depth: " + depth + ", maxMove -> " + maxMove);
		return maxMove;
	}

    // Min works similarly to max
	public Move minAlphaBeta(ChessBoard chessBoard, int depth, double a, double b) {
        Random r = new Random();

		if ((chessBoard.checkForTerminalState()) || (depth == maxDepth)) {
			Move lastMove = new Move(chessBoard.getLastMove());
			// System.out.println("min, depth: " + depth + ", lastMove -> " + lastMove);
			return lastMove;
		}
        List<ChessBoard> children = new ArrayList<ChessBoard>(chessBoard.getChildren(Constants.BLACK));
        if (children.size() == 1) {
        	return children.get(0).getLastMove();
		}
        
        Move minMove = new Move(Integer.MAX_VALUE);
		for (ChessBoard child : children) {
			Move move = maxAlphaBeta(child, depth + 1, a, b);
			if(move.getValue() <= minMove.getValue()) {
                if ((move.getValue() == minMove.getValue())) {
                    if (r.nextInt(2) == 0) {
                        minMove.setPositions(child.getLastMove().getPositions());
                        minMove.setValue(move.getValue());
                    }
                } else {
                    minMove.setPositions(child.getLastMove().getPositions());
                    minMove.setValue(move.getValue());
                }
            }
			
			// Alpha pruning.
            if (minMove.getValue() <= a) {
    			// System.out.println("min, depth: " + depth + ", alpha pruning move -> " + minMove);
            	return minMove;
            }
            
            // Update the b of the current min node.
            b = (b < minMove.getValue()) ? b : minMove.getValue();
        }
		// System.out.println("min, depth: " + depth + ", minMove -> " + minMove);
        return minMove;
    }

}
