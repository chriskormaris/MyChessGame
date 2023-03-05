package com.chriskormaris.mychessgame.api.ai;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
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
			for (int j = 0; j < Constants.NUM_OF_COLUMNS; j++) {
				if (whitePlays() && chessBoard.getGameBoard()[i][j].getAllegiance() == Allegiance.WHITE
						|| blackPlays() && chessBoard.getGameBoard()[i][j].getAllegiance() == Allegiance.BLACK) {
					String randomStartingPosition = chessBoard.getPositionByRowCol(i, j);
					Set<String> randomEndingPositions = chessBoard.getNextPositions(randomStartingPosition);

					if (randomEndingPositions.size() > 0) {
						randomStartingEndingPositions.put(randomStartingPosition, randomEndingPositions);
						// System.out.print("randomStartingPosition: " + randomStartingPosition);
						// System.out.println(" -> " + randomEndingPositions);
					}
				}
			}
		}

		Random r = new Random();
		List<String> keys = new ArrayList<>(randomStartingEndingPositions.keySet());
		if (randomStartingEndingPositions.size() > 0) {
			int randomStartingPositionIndex = r.nextInt(randomStartingEndingPositions.size());
			// System.out.println("randomStartingPositionIndex: " + randomStartingPositionIndex);
			randomAiStartingPosition = keys.get(randomStartingPositionIndex);
		}
		// System.out.println("random starting position: " + randomAiStartingPosition);

		/* STEP 2. Random ending position. */
		Set<String> possibleEndingPositions = randomStartingEndingPositions.get(randomAiStartingPosition);

		// Get a random element from the set.
		int randomEndingPositionIndex = r.nextInt(possibleEndingPositions.size());
		// System.out.println("randomEndingPositionIndex: " + randomEndingPositionIndex);
		int i = 0;
		for (String possibleEndingPosition : possibleEndingPositions) {
			if (i == randomEndingPositionIndex) {
				randomAiEndingPosition = possibleEndingPosition;
				break;
			}
			i++;
		}
		// System.out.println("random ending position: " + randomAiEndingPosition);

		// chessBoard.movePieceFromAPositionToAnother(randomAiStartingPosition, randomAiEndingPosition, true);
		// hideHintPositions(possibleEndingPositions);

		return new Move(randomAiStartingPosition, randomAiEndingPosition);
	}

}
