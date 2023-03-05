package com.chriskormaris.mychessgame.api.ai;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.evaluation.Evaluation;
import com.chriskormaris.mychessgame.api.evaluation.SimplifiedEvaluation;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinimaxAlphaBetaPruningAI extends MinimaxAI {

	public MinimaxAlphaBetaPruningAI() {
		super(2, Constants.BLACK, new SimplifiedEvaluation());
	}

	public MinimaxAlphaBetaPruningAI(int maxDepth, boolean aiPlayer, Evaluation evaluation) {
		super(maxDepth, aiPlayer, evaluation);
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
		if ((chessBoard.checkForTerminalState()) || (depth == super.getMaxDepth())) {
			return new Move(chessBoard.getLastMove());
		}
		// The children-moves of the state are calculated
		List<ChessBoard> children = new ArrayList<>(chessBoard.getChildren(Allegiance.WHITE, this));
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
			// Update the "a" of the current max node.
			a = Math.max(a, maxMove.getValue());

			// Beta pruning.
			if (a >= b) {
				return maxMove;
			}
		}
		return maxMove;
	}

	// Min works similarly to max.
	private Move minAlphaBeta(ChessBoard chessBoard, int depth, double a, double b) {
		Random r = new Random();

		if ((chessBoard.checkForTerminalState()) || (depth == super.getMaxDepth())) {
			return new Move(chessBoard.getLastMove());
		}
		List<ChessBoard> children = new ArrayList<>(chessBoard.getChildren(Allegiance.BLACK, this));
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

			// Update the "b" of the current min node.
			b = Math.min(b, minMove.getValue());

			// Alpha pruning.
			if (b <= a) {
				return minMove;
			}
		}
		return minMove;
	}

}
