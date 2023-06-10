package com.chriskormaris.mychessgame.api.ai;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.evaluation.Evaluation;
import com.chriskormaris.mychessgame.api.evaluation.SimplifiedEvaluation;
import com.chriskormaris.mychessgame.api.util.Constants;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class MinimaxAI extends AI {

	// Variable that holds the maximum depth the MinimaxAi algorithm will reach for this player.
	private int maxDepth;

	private Evaluation evaluation;

	public MinimaxAI() {
		super(Constants.BLACK);
		this.maxDepth = 2;
		this.evaluation = new SimplifiedEvaluation();
	}

	public MinimaxAI(int maxDepth, boolean aiPlayer, Evaluation evaluation) {
		super(aiPlayer);
		this.maxDepth = maxDepth;
		this.evaluation = evaluation;
	}

	@Override
	public Move getNextMove(ChessBoard chessBoard) {
		Move openingMove = OpeningMoves.getNextMove(chessBoard);
		if (openingMove != null) return openingMove;

		// If White plays, then it wants to maximize the heuristics value.
		if (whitePlays()) {
			return max(new ChessBoard(chessBoard), 0);
		}
		// If Black plays, then it wants to minimize the heuristics value.
		else {
			return min(new ChessBoard(chessBoard), 0);
		}
	}

	// The max and min functions are called interchangeably, one after another until a max depth is reached.
	private Move max(ChessBoard chessBoard, int depth) {
		Random r = new Random();

		/* If MAX is called on a state that is terminal or after a maximum depth is reached,
		 * then a heuristic is calculated on the state and the move returned. */
		if ((chessBoard.checkForTerminalState()) || (depth == maxDepth)) {
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
			Move move = min(child, depth + 1);
			// The child-move with the greatest value is selected and returned by max
			if (move.getValue() >= maxMove.getValue()) {
				if (move.getValue() > maxMove.getValue()
                        // If the heuristic has the same value, then we randomly choose one of the two moves
                        || r.nextInt(2) == 0
						|| move.getValue() == Integer.MIN_VALUE) {
                    maxMove.setPositionStart(child.getLastMove().getPositionStart());
                    maxMove.setPositionEnd(child.getLastMove().getPositionEnd());
                    maxMove.setValue(move.getValue());
				}
			}
		}
		return maxMove;
	}

	// Min works similarly to max.
	private Move min(ChessBoard chessBoard, int depth) {
		Random r = new Random();

		if ((chessBoard.checkForTerminalState()) || (depth == maxDepth)) {
			return new Move(chessBoard.getLastMove());
		}
		List<ChessBoard> children = new ArrayList<>(chessBoard.getChildren(Allegiance.BLACK, this));
		if (children.size() == 1) {
			return children.get(0).getLastMove();
		}

		Move minMove = new Move(Integer.MAX_VALUE);
		for (ChessBoard child : children) {
			Move move = max(child, depth + 1);
			if (move.getValue() <= minMove.getValue()) {
				if (move.getValue() < minMove.getValue()
						|| r.nextInt(2) == 0
						|| move.getValue() == Integer.MAX_VALUE) {
					minMove.setPositionStart(child.getLastMove().getPositionStart());
					minMove.setPositionEnd(child.getLastMove().getPositionEnd());
					minMove.setValue(move.getValue());
				}
			}
		}
		return minMove;
	}

}
