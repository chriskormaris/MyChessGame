package com.chriskormaris.mychessgame.api.ai;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RandomChoiceAI extends AI {

	public RandomChoiceAI() {
		super(Constants.BLACK);
	}

	public RandomChoiceAI(boolean aiPlayer) {
		super(aiPlayer);
	}

	// Initiates the random move.
	@Override
	public Move getNextMove(ChessBoard chessBoard) {
		String randomAiStartingPosition = "";
		String randomAiEndingPosition = "";

		// This map is used for the Random AI implementation.
		Map<String, Set<String>> randomStartingEndingPositions = new HashMap<>();

		/* STEP 1. Random starting position. */
		for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
			for (int j = 0; j < chessBoard.getNumOfColumns(); j++) {
				if (whitePlays() && chessBoard.getGameBoard()[i][j].isWhite()
						|| blackPlays() && chessBoard.getGameBoard()[i][j].isBlack()) {
					String randomStartingPosition = chessBoard.getPositionByRowCol(i, j);
					Set<String> randomEndingPositions = chessBoard.getNextPositions(randomStartingPosition);

					if (randomEndingPositions.size() > 0) {
						randomStartingEndingPositions.put(randomStartingPosition, randomEndingPositions);
					}
				}
			}
		}

		Random r = new Random();
		List<String> keys = new ArrayList<>(randomStartingEndingPositions.keySet());
		if (randomStartingEndingPositions.size() > 0) {
			int randomStartingPositionIndex = r.nextInt(randomStartingEndingPositions.size());
			randomAiStartingPosition = keys.get(randomStartingPositionIndex);
		}

		/* STEP 2. Random ending position. */
		Set<String> possibleEndingPositions = randomStartingEndingPositions.get(randomAiStartingPosition);

		// Get a random element from the set.
		int randomEndingPositionIndex = r.nextInt(possibleEndingPositions.size());
		int i = 0;
		for (String possibleEndingPosition : possibleEndingPositions) {
			if (i == randomEndingPositionIndex) {
				randomAiEndingPosition = possibleEndingPosition;
				break;
			}
			i++;
		}

		return new Move(randomAiStartingPosition, randomAiEndingPosition);
	}

}
