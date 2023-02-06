package com.chriskormaris.mychessgame.api.ai;

import com.chriskormaris.mychessgame.api.chess_board.ChessBoard;
import com.chriskormaris.mychessgame.api.chess_board.Move;
import com.chriskormaris.mychessgame.api.enumeration.Allegiance;
import com.chriskormaris.mychessgame.api.util.Constants;
import com.chriskormaris.mychessgame.api.util.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.chriskormaris.mychessgame.api.util.Constants.NUM_OF_COLUMNS;

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
		if (chessBoard.whitePlays() && whitePlays() && !chessBoard.isWhiteKingInCheck()
				||
				chessBoard.blackPlays() && blackPlays() && !chessBoard.isBlackKingInCheck()) {
			for (int i = 0; i < chessBoard.getNumOfRows(); i++) {
				for (int j = 0; j < NUM_OF_COLUMNS; j++) {
					if (whitePlays() && chessBoard.getGameBoard()[i][j].getAllegiance() == Allegiance.WHITE
							|| blackPlays() && chessBoard.getGameBoard()[i][j].getAllegiance() == Allegiance.BLACK) {
						String randomStartingPosition = Utilities.getPositionByRowCol(i, j, chessBoard.getNumOfRows());
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

		}
		// If the AI King is in check, then get one of the following valid moves.
		else if (chessBoard.whitePlays() && whitePlays() && chessBoard.isWhiteKingInCheck()
				|| chessBoard.blackPlays() && blackPlays() && chessBoard.isBlackKingInCheck()) {
			// System.out.println("blackKingInCheckValidPieceMoves: " + chessBoard.blackKingInCheckValidPieceMoves);
			Random r = new Random();
			List<String> keys = new ArrayList<>();
			int randomStartingPositionIndex = 0;

			if (chessBoard.whitePlays()) {
				keys = new ArrayList<>(chessBoard.getWhiteKingInCheckValidPieceMoves().keySet());
				randomStartingPositionIndex = r.nextInt(chessBoard.getWhiteKingInCheckValidPieceMoves().size());
			} else if (chessBoard.blackPlays()) {
				keys = new ArrayList<>(chessBoard.getBlackKingInCheckValidPieceMoves().keySet());
				randomStartingPositionIndex = r.nextInt(chessBoard.getBlackKingInCheckValidPieceMoves().size());
			}

			randomAiStartingPosition = keys.get(randomStartingPositionIndex);
		}
		// System.out.println("random starting position: " + randomAiStartingPosition);

		/* STEP 2. Random ending position. */
		Set<String> possibleEndingPositions = new HashSet<>();
		if (chessBoard.whitePlays() && !chessBoard.isWhiteKingInCheck()
				|| chessBoard.blackPlays() && !chessBoard.isBlackKingInCheck()) {
			possibleEndingPositions = randomStartingEndingPositions.get(randomAiStartingPosition);
		} else {
			if (chessBoard.whitePlays()) {
				possibleEndingPositions = chessBoard.getWhiteKingInCheckValidPieceMoves().get(randomAiStartingPosition);
			} else if (chessBoard.blackPlays()) {
				possibleEndingPositions = chessBoard.getBlackKingInCheckValidPieceMoves().get(randomAiStartingPosition);
			}
		}

		// Get a random element from the set.
		Random r = new Random();
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