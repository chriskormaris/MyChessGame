package com.chriskormaris.mychessgame.api.ai;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.enumeration.EvaluationFunction;
import com.chriskormaris.mychessgame.api.util.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class MinimaxAlphaBetaPruningAI extends AI {

	// Variable that holds the maximum depth the MinimaxAi algorithm will reach for this player.
	private int maxDepth;

	private EvaluationFunction evaluationFunction;

	public MinimaxAlphaBetaPruningAI() {
		super(Constants.BLACK);
		this.maxDepth = 2;
		this.evaluationFunction = EvaluationFunction.SIMPLIFIED;
	}

	public MinimaxAlphaBetaPruningAI(int maxDepth, boolean aiPlayer, EvaluationFunction evaluationFunction) {
		super(aiPlayer);
		this.maxDepth = maxDepth;
		this.evaluationFunction = evaluationFunction;
	}

	@Override
	public Move getNextMove(ChessBoard chessBoard) {
		// If White plays, then it wants to maximize the heuristics value.
		if (whitePlays()) {
			return maxAlphaBeta(new ChessBoard(chessBoard), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
		// If Black plays, then it wants to minimize the heuristics value.
		else {
			return minAlphaBeta(new ChessBoard(chessBoard), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
	}

	// The max and min functions are called interchangeably, one after another until a max depth is reached.
	private Move maxAlphaBeta(ChessBoard chessBoard, int depth, double a, double b) {
		Random r = new Random();

		/* If MAX is called on a state that is terminal or after a maximum depth is reached,
		 * then a heuristic is calculated on the state and the move returned. */
		if ((chessBoard.checkForTerminalState()) || (depth == maxDepth)) {
			// System.out.println("max, depth: " + depth + ", lastMove -> " + chessBoard.getLastMove());
			return new Move(chessBoard.getLastMove());
		}
		// The children-moves of the state are calculated
		List<ChessBoard> children = new ArrayList<>(chessBoard.getChildren(Allegiance.WHITE, evaluationFunction));
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
					if (r.nextInt(2) == 0 || move.getValue() == Integer.MIN_VALUE) {
						maxMove.setPositions(child.getLastMove().getPositions());
						maxMove.setValue(move.getValue());
					}
				} else {
					maxMove.setPositions(child.getLastMove().getPositions());
					maxMove.setValue(move.getValue());
				}
			}
			// Update the a of the current max node.
			a = Math.max(a, maxMove.getValue());

			// Beta pruning.
			if (a >= b) {
				// System.out.println("max, depth: " + depth + ", beta pruning move -> " + maxMove);
				return maxMove;
			}
		}
		// System.out.println("max, depth: " + depth + ", maxMove -> " + maxMove);
		return maxMove;
	}

	// Min works similarly to max.
	private Move minAlphaBeta(ChessBoard chessBoard, int depth, double a, double b) {
		Random r = new Random();

		if ((chessBoard.checkForTerminalState()) || (depth == maxDepth)) {
			// System.out.println("min, depth: " + depth + ", lastMove -> " + chessBoard.getLastMove());
			return new Move(chessBoard.getLastMove());
		}
		List<ChessBoard> children = new ArrayList<>(chessBoard.getChildren(Allegiance.BLACK, evaluationFunction));
		if (children.size() == 1) {
			return children.get(0).getLastMove();
		}

		Move minMove = new Move(Integer.MAX_VALUE);
		for (ChessBoard child : children) {
			Move move = maxAlphaBeta(child, depth + 1, a, b);
			if (move.getValue() <= minMove.getValue()) {
				if ((move.getValue() == minMove.getValue())) {
					if (r.nextInt(2) == 0 || move.getValue() == Integer.MAX_VALUE) {
						minMove.setPositions(child.getLastMove().getPositions());
						minMove.setValue(move.getValue());
					}
				} else {
					minMove.setPositions(child.getLastMove().getPositions());
					minMove.setValue(move.getValue());
				}
			}

			// Update the b of the current min node.
			b = Math.min(b, minMove.getValue());

			// Alpha pruning.
			if (b <= a) {
				// System.out.println("min, depth: " + depth + ", alpha pruning move -> " + minMove);
				return minMove;
			}
		}
		// System.out.println("min, depth: " + depth + ", minMove -> " + minMove);
		return minMove;
	}

}
